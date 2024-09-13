package com.microsoft.azure.spring.condition.value;

/**
 * @author serkan
 */
public interface TypeAwareValueProvider<V> extends ValueProvider<V> {

    Class<V> getValueType();

}
