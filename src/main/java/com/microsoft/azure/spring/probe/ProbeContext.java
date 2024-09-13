package com.microsoft.azure.spring.probe;

import com.microsoft.azure.spring.condition.Condition;

/**
 * @author serkan
 */
public interface ProbeContext {

    default Condition getCondition() {
        return null;
    }
}
