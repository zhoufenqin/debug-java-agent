package com.microsoft.azure.spring;

import com.microsoft.azure.spring.condition.Condition;
import com.microsoft.azure.spring.logpoint.LogPointAction;
import com.microsoft.azure.spring.logpoint.LogPointContext;
import com.microsoft.azure.spring.probe.*;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LineNumberAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class DebugClassFileTransformer implements ClassFileTransformer
{
    private static class LogPointDetails
    {
        String className;
        int lineNo;
        String content;
        String conditionExpress;
        public LogPointDetails(String c, int m, String content, String conditionExpress)
        {
            this.className = c;
            this.lineNo = m;
            this.content = content;
            this.conditionExpress = conditionExpress;
        }
    }

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

    List<LogPointDetails> logPointDetails;
    List<MethodDetails> methodDetails;
    private long lastModifiedTime;
    private final Instrumentation instrumentation;

    public DebugClassFileTransformer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        logPointDetails = new ArrayList<>();
        methodDetails = new ArrayList<>();
        String filePath = System.getenv().getOrDefault("FILE_PATH", null); // "/mnt/d/code/github/debug-java-agent/src/main/resources/transform"
        if (filePath == null) {
            System.out.println("Set FILE_PATH to your logpoint file first, demo logpoint file is under resources folder");
        } else {
            loadMethodsFromFile(filePath);
            startFileWatcher(filePath);
        }
    }

    // load class#method#lineno#condition from file, now use byos to store debug info
    private void loadMethodsFromFile(String filePath) {
        logPointDetails.clear();
        methodDetails.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("#");
                if (parts[0].equalsIgnoreCase("insert")) {
                    parseInsertLineLogMethod(Arrays.copyOfRange(parts, 1, parts.length));
                } else if (parts[0].equalsIgnoreCase("around")) {
                    parseAroundLogMethod(Arrays.copyOfRange(parts, 1, parts.length));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseAroundLogMethod(String[] parts) {
        if (parts.length == 2) {
            methodDetails.add(new MethodDetails(parts[0], parts[1]));
        }
    }

    private void parseInsertLineLogMethod(String[] parts) {
        if (parts.length == 3) {
            logPointDetails.add(new LogPointDetails(parts[0], Integer.parseInt(parts[1]), parts[2], ""));
        } else if (parts.length == 4) {
            logPointDetails.add(new LogPointDetails(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3])); // with condition
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
                    Thread.sleep(3000); // Check every 3 second
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void retransformClasses() {
        try {
            System.out.println("=== retransformClasses start ===");
            Class<?>[] classes = instrumentation.getAllLoadedClasses();
            for (Class<?> clazz : classes) {
                String className = clazz.getName().replace("/", ".");

                // ad-hoc, just clean all retransforming class for my specific project. need to rewrite it, use developer's project
                if (logPointDetails.isEmpty() && methodDetails.isEmpty()) {
                    if (className.contains("containerappsalbumapijava")) {
                        System.out.println("Retransforming class: " + className);
                        instrumentation.retransformClasses(clazz);
                    }
                }

                if ((!logPointDetails.isEmpty() && logPointDetails.stream().anyMatch(m -> m.className.equals(className)))
                    || (!methodDetails.isEmpty() && methodDetails.stream().anyMatch(m -> m.className.equals(className)))) {
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
        if (logPointDetails.stream().noneMatch(m -> m.className.equals(dotClassName)) &&
                methodDetails.stream().noneMatch(m -> m.className.equals(dotClassName))) {
            return null;
        }
        try {
            ClassPool cp = ClassPool.getDefault();
            cp.appendClassPath(new LoaderClassPath(loader));
            CtClass cc = cp.get(dotClassName);

            if (methodDetails.stream().anyMatch(m -> m.className.equals(dotClassName))) {
                transformAroundLogs(cp, cc);
            }
            if (logPointDetails.stream().anyMatch(m -> m.className.equals(dotClassName))) {
                transformInsertLogs(cc, loader, className);
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

    private void transformAroundLogs(ClassPool cp, CtClass cc) throws NotFoundException, CannotCompileException {
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
    }

    private void transformInsertLogs(CtClass cc, ClassLoader loader, String className) throws Exception {
        ProbeManager.removeAllProbes();
        String dotClassName = className.replace("/", ".");
        List<LogPointDetails> matchLogPoint = logPointDetails.stream().filter(x -> x.className.equals(dotClassName)).collect(Collectors.toList());

        for (LogPointDetails details : matchLogPoint) {
            AtomicReference<CtMethod> ownerMethodRef = new AtomicReference<>();
            int lineNo = details.lineNo;
            String content = details.content;
            String conditionExpress = details.conditionExpress;
            CtMethod method = getMethodAtLine(cc, lineNo, ownerMethodRef);

            if (method == null) {
                if (ownerMethodRef.get() != null) {
                    System.out.println("Line number" +  lineNo + " class " + className + " is not available");
                    throw new Exception("no line");
                } else {
                    System.out.println(
                            "No method could be found in class " + className +" on line " + lineNo);
                    throw new Exception("no method");
                }
            }
            instrumentMethodProbes(className, loader, cc, method, lineNo, content, conditionExpress);
        }

    }


    private static void instrumentMethodProbes(String className,
                                               ClassLoader classLoader,
                                               CtClass clazz,
                                               CtMethod method,
                                               int lineNo,
                                               String content,
                                               String conditionExpress) throws Exception {
        LineNumberAttribute lineNoAttr = checkWhetherLineIsAvailable(className, method, lineNo);
        LocalVariableAttribute localVarAttr = checkLocalVarMetadata(className, method);

        InternalProbe probe = ProbeManager.getOrPutProbe(className, classLoader, clazz, method, lineNo);
        Condition condition = null;
        if (conditionExpress != null && !conditionExpress.isEmpty()) {
            condition = ProbeManager.getOrPutCondition(conditionExpress, className, classLoader, method, lineNo);
        }
        LogPointContext context = new LogPointContext(probe, "1", conditionExpress, condition, false);
        ProbeAction<LogPointContext> action = new ConditionAwareProbeAction<>(new LogPointAction(context));
        probe.addAction(action);


        Collection<LocalVarMetadata> localVarMetadatas =
                ProbeManager.extractLocalVarMetadata(lineNoAttr, localVarAttr, lineNo);
        String localVariableTraceExpression = generateLocalVariableTraceExpression(localVarMetadatas);
        // E.g. com.microsoft.azure.spring.probe.ProbeBridge.onProbe("examples/azure/containerappsalbumapijava/AlbumController::41", "examples/azure/containerappsalbumapijava/AlbumController::41", examples.azure.containerappsalbumapijava.AlbumController.class, $0, new String[] { "number", "env", "value" }, new Object[] { ($w) number, ($w) env, ($w) value }, "print log");
        String probeExpression =
                ProbeBridge.class.getName() + ".onProbe" +
                        "(" +
                        "\"" +  probe.getId() + "\"" + ", " +
                        "\"" +  ProbeManager.getConditionId(className, lineNo) + "\"" + ", " +
                        clazz.getName() + ".class" + ", " +
                        (Modifier.isStatic(method.getModifiers()) ? "null" : "$0") + ", " +
                        localVariableTraceExpression + ", " +
                        "\"" + content + "\""  +
                        ");";
        try {
            method.insertAt(lineNo+1, probeExpression);
        } catch (CannotCompileException e) {
            System.out.println("Line number" + lineNo + " in class " + className + " is not available");
            throw new Exception("can not instrumentMethodProbes");
        }
    }

    private static CtMethod getMethodAtLine(CtClass clazz, int lineNo) throws NotFoundException {
        return getMethodAtLine(clazz, lineNo, null);
    }

    private static CtMethod getMethodAtLine(CtClass clazz, int lineNo,
                                            AtomicReference<CtMethod> ownerMethodRef) throws NotFoundException {
        for (CtMethod method : clazz.getDeclaredMethods()) {
            MethodInfo methodInfo = method.getMethodInfo();
            CodeAttribute codeAttr = methodInfo.getCodeAttribute();
            if (codeAttr == null) {
                continue;
            }
            int byteCodeLength = codeAttr.getCode().length;
            int startLine = methodInfo.getLineNumber(0);
            int endLine = methodInfo.getLineNumber(byteCodeLength - 1);
            if (lineNo >= startLine && lineNo <= endLine) {
                if (ownerMethodRef != null) {
                    ownerMethodRef.set(method);
                }
                LineNumberAttribute lineNoAttr =
                        (LineNumberAttribute) codeAttr.getAttribute(LineNumberAttribute.tag);
                if (lineNoAttr == null) {
                    continue;
                }
                LineNumberAttribute.Pc nearPc = lineNoAttr.toNearPc(lineNo);
                if (nearPc.line == lineNo) {
                    return method;
                }
            }
        }
        for (CtClass nestedClazz : clazz.getNestedClasses()) {
            CtMethod method = getMethodAtLine(nestedClazz, lineNo);
            if (method != null) {
                return method;
            }
        }
        return null;
    }

    // Eg.
    // localVarNameBuilder: new String[] { "number", "env", "value" }
    // localVarValueBuilder: new Object[] { ($w) number, ($w) env, ($w) value }
    private static String generateLocalVariableTraceExpression(Collection<LocalVarMetadata> localVarMetadataList) {
        StringBuilder localVarNameBuilder = new StringBuilder("new String[] { ");
        StringBuilder localVarValueBuilder = new StringBuilder("new Object[] { ");
        boolean localVarTraced = false;
        if (localVarMetadataList != null && !localVarMetadataList.isEmpty()) {
            for (LocalVarMetadata localVarMetadata : localVarMetadataList) {
                if (localVarTraced) {
                    localVarNameBuilder.append(", ");
                    localVarValueBuilder.append(", ");
                }
                localVarNameBuilder.append("\"" + localVarMetadata.getOriginalName() + "\"");
                localVarValueBuilder.append("($w) " + localVarMetadata.getName());
                localVarTraced = true;
            }
        }
        if (!localVarTraced) {
            return "new String[0], new Object[0]";
        }
        localVarNameBuilder.append(" }");
        localVarValueBuilder.append(" }");
        return localVarNameBuilder.toString() + ", " + localVarValueBuilder.toString();
    }

    private static LineNumberAttribute checkWhetherLineIsAvailable(String className, CtMethod method, int lineNo) throws Exception {
        MethodInfo methodInfo = method.getMethodInfo();
        CodeAttribute codeAttr = methodInfo.getCodeAttribute();
        if (codeAttr == null) {
            String errorMessage =
                    String.format(
                            "No code info could be found in class %s. So probes are not supported",
                            className);
            System.out.println(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        LineNumberAttribute lineNoAttr =
                (LineNumberAttribute) codeAttr.getAttribute(LineNumberAttribute.tag);
        if (lineNoAttr == null) {
            String errorMessage =
                    String.format(
                            "No line number info could be found in class %s. So probes are not supported",
                            className);
            System.out.println(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        LineNumberAttribute.Pc nearPc = lineNoAttr.toNearPc(lineNo);
        if (nearPc.line != lineNo) {
            System.out.println("Line number" + lineNo + " in class " + className + " is not available");
            throw new Exception("line number is not available");
        }
        return lineNoAttr;
    }

    private static LocalVariableAttribute checkLocalVarMetadata(String className, CtMethod method) {
        MethodInfo methodInfo = method.getMethodInfo();
        CodeAttribute codeAttr = methodInfo.getCodeAttribute();
        LocalVariableAttribute localVarAttr =
                (LocalVariableAttribute) codeAttr.getAttribute(LocalVariableAttribute.tag);
        if (localVarAttr == null) {
            String errorMessage =
                    String.format(
                            "No local variable info could be found in class %s. So probes are not supported",
                            className);
            System.out.println(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        return localVarAttr;
    }
}