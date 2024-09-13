package com.microsoft.azure.spring.condition;

/**
 * @author serkan
 */
public interface VariableInfoProvider {

    VariableInfo getVariableInfo(String variableName);

}
