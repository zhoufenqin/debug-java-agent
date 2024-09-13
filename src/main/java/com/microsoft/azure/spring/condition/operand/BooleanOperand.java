package com.microsoft.azure.spring.condition.operand;


import com.microsoft.azure.spring.condition.ConditionContext;
import com.microsoft.azure.spring.condition.value.ValueProvider;

import java.util.Objects;

/**
 * @author serkan
 */
public class BooleanOperand extends TypedOperand<BooleanOperand, Boolean> {

    public BooleanOperand(ValueProvider<Boolean> valueProvider) {
        super(BooleanOperand.class, Boolean.class, valueProvider);
    }

    @Override
    protected boolean isEQ(Boolean value, ConditionContext conditionContext) throws Exception {
        Boolean curValue = getValue(conditionContext);
        return Objects.equals(curValue, value);
    }

    @Override
    protected boolean isNE(Boolean value, ConditionContext conditionContext) throws Exception {
        Boolean curValue = getValue(conditionContext);
        return !Objects.equals(curValue, value);
    }

}
