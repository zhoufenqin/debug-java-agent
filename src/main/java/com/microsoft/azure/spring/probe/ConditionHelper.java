package com.microsoft.azure.spring.probe;

import com.microsoft.azure.spring.condition.Condition;
import com.microsoft.azure.spring.condition.ConditionFactory;
import com.microsoft.azure.spring.condition.VariableInfo;
import com.microsoft.azure.spring.condition.VariableInfoProvider;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LineNumberAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.util.Collection;

/**
 * @author serkan
 */
public final class ConditionHelper {
    private ConditionHelper() {
    }

    public static Condition getCondition(String conditionExpression,
                                         String className, ClassLoader classLoader,
                                         CtMethod method, int lineNo) throws Exception {
        try {
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
            Collection<LocalVarMetadata> localVarMetadatas =
                    ProbeManager.extractLocalVarMetadata(lineNoAttr, localVarAttr, lineNo);
            VariableInfoProvider variableInfoProvider = variableName -> {
                int i = 0;
                int localVarMetadataIdx = -1;
                LocalVarMetadata localVarMetadata = null;
                for (LocalVarMetadata lvm : localVarMetadatas) {
                    if (lvm.getOriginalName().equals(variableName)) {
                        localVarMetadataIdx = i;
                        localVarMetadata = lvm;
                        break;
                    }
                    i++;
                }
                if (localVarMetadata == null) {
                    return null;
                }
                Class localVarType;
                try {
                    localVarType = getLocalVariableType(localVarMetadata.getTypeSignature(), classLoader);
                } catch (ClassNotFoundException e) {
                    System.out.println(String.format(
                            "Unable to find class represented by type descriptor {} which checking type of variable {}",
                            localVarMetadata.getTypeSignature(), localVarMetadata.getName()));
                    return null;
                }
                return new VariableInfo(localVarType, localVarMetadataIdx);
            };
            return ConditionFactory.createConditionFromExpressionByANTLR(conditionExpression, variableInfoProvider);
        } catch (Throwable t) {
            throw t;
        }
    }


    private static Class getLocalVariableType(String localVarTypeSignature, ClassLoader classLoader)
            throws ClassNotFoundException {
        switch (localVarTypeSignature) {
            case "Z":
                return boolean.class;
            case "Ljava/lang/Boolean;":
                return Boolean.class;
            case "B":
                return byte.class;
            case "Ljava/lang/Byte;":
                return Byte.class;
            case "C":
                return char.class;
            case "Ljava/lang/Character;":
                return Character.class;
            case "S":
                return short.class;
            case "Ljava/lang/Short;":
                return Short.class;
            case "I":
                return int.class;
            case "Ljava/lang/Integer;":
                return Integer.class;
            case "F":
                return float.class;
            case "Ljava/lang/Float;":
                return Float.class;
            case "J":
                return long.class;
            case "Ljava/lang/Long;":
                return Long.class;
            case "D":
                return double.class;
            case "Ljava/lang/Double;":
                return Double.class;
            case "Ljava/lang/String;":
                return String.class;
            default:
                // No need to try to find and get actual class
                return Object.class; //ClassUtils.getClassWithException(classLoader, localVarTypeSignature, false);
        }
    }

}
