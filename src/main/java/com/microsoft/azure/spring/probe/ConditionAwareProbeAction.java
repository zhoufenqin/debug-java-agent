package com.microsoft.azure.spring.probe;

import com.microsoft.azure.spring.condition.Condition;
import com.microsoft.azure.spring.condition.ConditionContext;

public class ConditionAwareProbeAction<C extends ProbeContext> extends DelegatedProbeAction<C> {


    public ConditionAwareProbeAction(ProbeAction<C> action) {
        super(action);
    }

    protected boolean checkWhetherConditionOk(Probe probe, Class<?> clazz, Object obj, Object[] localVarValues) throws Exception {
        C context = getContext();
        if (context != null) {
            Condition condition = context.getCondition();
            if (condition != null) {
                ConditionContext conditionContext = new ConditionContext(clazz, obj, localVarValues);
                return condition.evaluate(conditionContext);
            }
        }
        return true;
    }

    @Override
    public void onProbe(Probe probe, Class<?> clazz, Object obj, String methodName,
                        String[] localVarNames, Object[] localVarValues, String printLogs) throws Exception {
        if (!checkWhetherConditionOk(probe, clazz, obj, localVarValues)) {
            return;
        }
        super.onProbe(probe, clazz, obj, methodName, localVarNames, localVarValues, printLogs);
    }

}
