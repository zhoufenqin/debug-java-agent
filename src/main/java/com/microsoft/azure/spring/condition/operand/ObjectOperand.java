package com.microsoft.azure.spring.condition.operand;

import com.microsoft.azure.spring.condition.ConditionContext;
import com.microsoft.azure.spring.condition.value.ValueProvider;

import java.util.Objects;

/**
 * @author serkan
 */
public class ObjectOperand<V> implements Operand<V> {

    private final ValueProvider<V> valueProvider;

    public ObjectOperand(ValueProvider<V> valueProvider) {
        this.valueProvider = valueProvider;
    }

    @Override
    public V getValue(ConditionContext conditionContext) throws Exception {
        return valueProvider.getValue(conditionContext);
    }

    @Override
    public boolean eq(Operand operand, ConditionContext conditionContext) throws Exception {
        Object value1 = getValue(conditionContext);
        Object value2 = operand.getValue(conditionContext);
        return Objects.equals(value1, value2);
    }

    @Override
    public boolean ne(Operand operand, ConditionContext conditionContext) throws Exception {
        Object value1 = getValue(conditionContext);
        Object value2 = operand.getValue(conditionContext);
        return !Objects.equals(value1, value2);
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
