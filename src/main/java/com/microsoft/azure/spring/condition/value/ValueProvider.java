package com.microsoft.azure.spring.condition.value;

import com.microsoft.azure.spring.condition.ConditionContext;

/**
 * @author serkan
 */
public interface ValueProvider<V> {

    V getValue(ConditionContext conditionContext) throws Exception;

}
