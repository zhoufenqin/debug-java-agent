package com.microsoft.azure.spring.condition.operand;

import com.microsoft.azure.spring.condition.ConditionContext;
import com.microsoft.azure.spring.condition.value.ValueProvider;

import java.util.Objects;

/**
 * @author serkan
 */
public class StringOperand extends TypedOperand<StringOperand, String> {

    public StringOperand(ValueProvider<String> valueProvider) {
        super(StringOperand.class, String.class, valueProvider);
    }

    @Override
    protected boolean isEQ(String value, ConditionContext conditionContext) throws Exception {
        String curValue = getValue(conditionContext);
        return Objects.equals(curValue, value);
    }

    @Override
    protected boolean isNE(String value, ConditionContext conditionContext) throws Exception {
        String curValue = getValue(conditionContext);
        return !Objects.equals(curValue, value);
    }

}
