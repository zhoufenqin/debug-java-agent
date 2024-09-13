package com.microsoft.azure.spring.condition.value;


import com.microsoft.azure.spring.condition.ConditionContext;

/**
 * @author serkan
 */
public class ConstantValueProvider<V> implements ValueProvider<V> {

    private final V value;

    public ConstantValueProvider(V value) {
        this.value = value;
    }

    @Override
    public V getValue(ConditionContext conditionContext) {
        return value;
    }

}
