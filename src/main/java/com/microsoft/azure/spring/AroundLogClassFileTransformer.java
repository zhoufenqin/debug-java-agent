package com.microsoft.azure.spring;

import javassist.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AroundLogClassFileTransformer implements ClassFileTransformer
{
    private static class MethodDetails
    {
        String className;
        String methodName;
        public MethodDetails(String c, String m)
        {
            this.className = c;
            this.methodName = m;
        }
    }

    List<MethodDetails> methodDetails;
    private long lastModifiedTime;
    private final Instrumentation instrumentation;

    public AroundLogClassFileTransformer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        methodDetails = new ArrayList<>();
        String filePath = System.getenv().getOrDefault("FILE_PATH", null);//"/mnt/d/code/github/debug-java-agent/src/main/resources/methods"
        if (filePath == null) {
            System.out.println("Set FILE_PATH to your logpoint file first, demo methods file is under resources folder");
        } else {
            loadMethodsFromFile(filePath);
            startFileWatcher(filePath);
        }
    }

    private void loadMethodsFromFile(String filePath) {
        methodDetails.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("#");
                if (parts.length == 2) {
                    methodDetails.add(new MethodDetails(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startFileWatcher(String filePath) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            Path path = Paths.get(filePath);
            while (true) {
                try {
                    long currentModifiedTime = Files.getLastModifiedTime(path).toMillis();
                    if (currentModifiedTime != lastModifiedTime) {
                        System.out.println("Thread ID: " + Thread.currentThread().getId() + " - File content changed, reloading methods to log");
                        loadMethodsFromFile(filePath);
                        lastModifiedTime = currentModifiedTime;
                        retransformClasses();
                    }
                    Thread.sleep(3000); // Check every second
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e)
                {
                    System.out.println("error !!!");
                    e.printStackTrace();
                }
            }
        });
    }
    private void retransformClasses() {
        try {
            System.out.println("=== retransformClasses ===");
            Class<?>[] classes = instrumentation.getAllLoadedClasses();
            for (Class<?> clazz : classes) {
                String className = clazz.getName().replace("/", ".");

                if (methodDetails.isEmpty()) {
                    if (className.contains("containerappsalbumapijava")) {
                        System.out.println("Retransforming class: " + className);
                        instrumentation.retransformClasses(clazz);
                    }
                }

                if (!methodDetails.isEmpty() && methodDetails.stream().anyMatch(m -> m.className.equals(className))) {
                    System.out.println("Retransforming class: " + className);
                    instrumentation.retransformClasses(clazz);
                }
            }
            System.out.println("=== retransformClasses finish ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className == null || loader == null) {
            return null;
        }

        String dotClassName = className.replace("/", ".");

        if (dotClassName.startsWith("java.") || dotClassName.startsWith("javax.")) {
            return null; // Skip JDK classes
        }
        byte[] bytecode = classfileBuffer;

        if (methodDetails.stream().noneMatch(m -> m.className.equals(dotClassName)))
        {
            return bytecode;
        }
        try {
            ClassPool cp = ClassPool.getDefault();
            cp.appendClassPath(new LoaderClassPath(loader));
            CtClass cc = cp.get(dotClassName);

            for (CtMethod method : cc.getDeclaredMethods()) {

                String methodName = method.getName();
                if (methodDetails.stream().noneMatch(m -> m.methodName.equals(methodName))){
                    continue;
                }
                method.addLocalVariable("startTime", cp.get("java.time.Instant"));
                method.addLocalVariable("endTime", cp.get("java.time.Instant"));
                method.addLocalVariable("duration", cp.get("java.time.Duration"));

                method.insertBefore("{ startTime = java.time.Instant.now(); " +
                        "System.out.println(\"[===== START =====] Method: " + methodName + " Start Time: \" + " +
                        "java.time.format.DateTimeFormatter.ISO_INSTANT.format(startTime)); }");

                method.insertAfter("{ endTime = java.time.Instant.now(); " +
                        "duration = java.time.Duration.between(startTime, endTime); " +
                        "System.out.println(\"[===== END =====] Method: " + methodName + " End Time: \" + " +
                        "java.time.format.DateTimeFormatter.ISO_INSTANT.format(endTime) + " +
                        "\" Total Time: \" + duration.toMillis() + \" milliseconds\"); }");
            }

            bytecode = cc.toBytecode();
            cc.detach();

            return bytecode;
        } catch (NotFoundException e) {
            System.err.println("Class not found: " + dotClassName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // If transformation fails, return null to not modify the class
    }
}