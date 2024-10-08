package com.microsoft.azure.spring.condition.value;

/**
 * @author serkan
 */
public interface PlaceholderValueProvider<V> extends TypeAwareValueProvider<V> {

    default boolean isEnabled() {
        return true;
    }

    String getPlaceholderName();

}
