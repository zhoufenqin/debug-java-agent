package com.microsoft.azure.spring.condition;

/**
 * @author serkan
 */
public interface Condition {

    boolean evaluate(ConditionContext conditionContext) throws Exception;

}
