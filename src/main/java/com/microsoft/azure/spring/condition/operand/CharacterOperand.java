package com.microsoft.azure.spring.condition.operand;


import com.microsoft.azure.spring.condition.ConditionContext;
import com.microsoft.azure.spring.condition.value.ValueProvider;

import java.util.Objects;

/**
 * @author serkan
 */
public class CharacterOperand extends TypedOperand<CharacterOperand, Character> {

    public CharacterOperand(ValueProvider<Character> valueProvider) {
        super(CharacterOperand.class, Character.class, valueProvider);
    }

    @Override
    protected boolean isEQ(Character value, ConditionContext conditionContext) throws Exception {
        Character curValue = getValue(conditionContext);
        return Objects.equals(curValue, value);
    }

    @Override
    protected boolean isNE(Character value, ConditionContext conditionContext) throws Exception {
        Character curValue = getValue(conditionContext);
        return !Objects.equals(curValue, value);
    }

}
