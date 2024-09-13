package com.microsoft.azure.spring.probe;

import com.microsoft.azure.spring.condition.Condition;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.LineNumberAttribute;
import javassist.bytecode.LocalVariableAttribute;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class ProbeManager {
    private static final Map<String, InternalProbe> probeMap = new ConcurrentHashMap<>();
    private static final Map<String, Condition> conditionMap = new ConcurrentHashMap<>();

    public static <P extends Probe> P getProbe(String probeId) {
        return (P) probeMap.get(probeId);
    }

    public static synchronized InternalProbe getOrPutProbe(String className,
                                                           ClassLoader classLoader,
                                                           CtClass clazz,
                                                           CtMethod method,
                                                           int lineNo) throws Exception {

        boolean instrumented = false;
        String methodId = null;
        boolean added = false;
        InternalProbe probe = null;

        try {
            try {
                methodId = generateMethodId(method);

                String ownerClassName = method.getDeclaringClass().getName();
                probe = new InternalProbe(ownerClassName,
                        generateProbeId(className, lineNo), className, lineNo, method.getName(), methodId);
                InternalProbe existingProbe = probeMap.putIfAbsent(probe.getId(), probe);
                added = existingProbe == null;
                if (!added) {
                    System.out.println("Probe has been already added in class" + className +" on line " + lineNo);
                    return existingProbe;
                }
                probeMap.put(probe.getId(), probe);
                return probe;
            } finally {
                clazz.defrost();
//                clazz.detach();

                System.out.println(String.format("Completed putting probe to class %s on line %d", className, lineNo));
            }
        } catch (Throwable t) {
            if (added && !instrumented && methodId != null && probe != null) {
                // Revert
                probeMap.remove(probe.getId());
            }
            throw new Exception("Error occurred while putting probe to class");
        }
    }

    private static String generateMethodId(CtMethod method) {
        return method.getDeclaringClass().getName() + "." + method.getLongName();
    }

    static String generateProbeId(String className, int lineNo) {
        return className + "::" + lineNo;
    }

    public static void removeAllProbes() throws NotFoundException {
        for (String id : probeMap.keySet()) {
            removeProbe(id, false);
        }
        probeMap.clear();
    }
    public static synchronized void removeProbe(String id, boolean ifEmpty) throws NotFoundException {
        System.out.printf("Removing probe with id %s\n", id);

        InternalProbe probe = probeMap.get(id);
        if (probe == null) {
            System.out.printf(
                    "No probe could be found with id %s\n",
                    id);
            throw new NotFoundException("NO_PROBE_EXIST");
        }

        if (ifEmpty && probe.hasAnyAction()) {
            System.out.printf("Probe with id %s has action so skipping removing\n", id);
            return;
        }

        probeMap.remove(id);

        boolean uninstrumented = false;
        String methodId = probe.methodId;
        boolean removed = false;

        try {
            removed = probeMap.remove(id) != null;

            probe.setRemoved(true);
        } catch (Throwable t) {
            if (removed && !uninstrumented && methodId != null && probe != null) {
                // Add back
                probeMap.put(id, probe);
            }
            System.out.printf(
                    "Error occurred while removing probe with id %s: %s\n",
                    id, t.getMessage());
        }
    }

    /**
    * E.g.
    * result = {TreeSet@4418}  size = 3
         0 = {LocalVarMetadata@4421}
          startPc = 0
          idx = 1
          name = "number"
          originalName = "number"
          typeSignature = "I"
         1 = {LocalVarMetadata@4422}
          startPc = 3
          idx = 2
          name = "env"
          originalName = "env"
          typeSignature = "Ljava/lang/String;"
         2 = {LocalVarMetadata@4423}
          startPc = 5
          idx = 3
          name = "value"
          originalName = "value"
          typeSignature = "I"
    */
    public static Collection<LocalVarMetadata> extractLocalVarMetadata(LineNumberAttribute lineNoAttr,
                                                                       LocalVariableAttribute localVarAttr,
                                                                       int line) {
        int lineStartPc = lineNoAttr.toStartPc(line);
        Set<LocalVarMetadata> localVarMetadatas = new TreeSet<>();
        if (lineStartPc >= 0) {
            for (int i = 0; i < localVarAttr.tableLength(); i++) {
                int localVarStartPc = localVarAttr.startPc(i);
                int localVarEndPc = localVarStartPc + localVarAttr.codeLength(i);
                if (localVarStartPc <= lineStartPc && localVarEndPc > lineStartPc) {
                    String localVariableName = localVarAttr.variableName(i);
                    if ("this".equals(localVariableName) || "$this".equals(localVariableName)) {
                        continue;
                    }
                    if (localVariableName != null && !localVariableName.startsWith("___")) {
                        String originalLocalVariableName = localVariableName;
                        LocalVarMetadata localVarMetadata =
                                new LocalVarMetadata(localVarStartPc, i, localVariableName,
                                        originalLocalVariableName, localVarAttr.signature(i));
                        localVarMetadatas.add(localVarMetadata);
                    }
                }
            }
        }
        return localVarMetadatas;
    }


    public static String getConditionId(String className, int lineNo) {
        return className + "::" + lineNo;
    }
    public static Condition getOrPutCondition(String conditionExpression,
                                              String className, ClassLoader classLoader,
                                              CtMethod method, int lineNo) throws Exception {
        Condition condition = ConditionHelper.getCondition(conditionExpression, className, classLoader, method, lineNo);
        String id = getConditionId(className, lineNo);
        if (conditionMap.get(id) == null) {
            conditionMap.put(id, condition);
        }
        return condition;
    }

    public static Condition getCondition(String id) {
        return conditionMap.get(id);
    }
}
