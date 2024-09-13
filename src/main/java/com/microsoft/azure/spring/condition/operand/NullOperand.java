package com.microsoft.azure.spring.condition.operand;

import com.microsoft.azure.spring.condition.ConditionContext;

/**
 * @author serkan
 */
public class NullOperand implements Operand {

    @Override
    public Object getValue(ConditionContext conditionContext) {
        return null;
    }

    @Override
    public boolean eq(Operand operand, ConditionContext conditionContext) throws Exception {
        return operand.getValue(conditionContext) == null;
    }

    @Override
    public boolean ne(Operand operand, ConditionContext conditionContext) throws Exception {
        return operand.getValue(conditionContext) != null;
    }

    @Override
    public boolean lt(Operand operand, ConditionContext conditionContext) {
        return false;
    }

    @Override
    public boolean le(Operand operand, ConditionContext conditionContext) {
        return false;
    }

    @Override
    public boolean gt(Operand operand, ConditionContext conditionContext) {
        return false;
    }

    @Override
    public boolean ge(Operand operand, ConditionContext conditionContext) {
        return false;
    }

}
