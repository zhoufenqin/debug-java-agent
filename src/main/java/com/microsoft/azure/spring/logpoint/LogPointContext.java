package com.microsoft.azure.spring.logpoint;

import com.microsoft.azure.spring.condition.Condition;
import com.microsoft.azure.spring.probe.Probe;
import com.microsoft.azure.spring.probe.ProbeContext;

/**
 * @author yasin
 */
public class LogPointContext implements ProbeContext {

    final Probe probe;
    final String id;
    final String conditionExpression;
    final Condition condition;
    volatile boolean disabled;

    public LogPointContext(Probe probe, String id, String conditionExpression,
                           Condition condition, boolean disabled) {
        this.probe = probe;
        this.id = id;
        this.conditionExpression = conditionExpression;
        this.condition = condition;
        this.disabled = disabled;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

}
