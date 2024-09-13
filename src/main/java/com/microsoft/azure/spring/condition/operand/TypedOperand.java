package com.microsoft.azure.spring.condition.operand;

import com.microsoft.azure.spring.condition.ConditionContext;
import com.microsoft.azure.spring.condition.value.ValueProvider;

/**
 * @author serkan
 */
public abstract class TypedOperand<O extends Operand, V> implements Operand<V> {

    protected final Class<O> operandType;
    protected final Class<V> valueType;
    protected final ValueProvider<V> valueProvider;

    protected TypedOperand(Class<O> operandType, Class<V> valueType,
                           ValueProvider<V> valueProvider) {
        this.operandType = operandType;
        this.valueType = valueType;
        this.valueProvider = valueProvider;
    }

    @Override
    public V getValue(ConditionContext conditionContext) throws Exception {
        V value = valueProvider.getValue(conditionContext);
        if (value == null || valueType.isAssignableFrom(value.getClass())) {
            return value;
        }
        return null;
    }

    @Override
    public final boolean eq(Operand operand, ConditionContext conditionContext) throws Exception {
        Object value = operand.getValue(conditionContext);
        if (value == null || valueType.isAssignableFrom(value.getClass())) {
            return isEQ((V) value, conditionContext);
        }
        return false;
    }

    protected boolean isEQ(V value, ConditionContext conditionContext) throws Exception {
        return false;
    }

    @Override
    public final boolean ne(Operand operand, ConditionContext conditionContext) throws Exception {
        Object value = operand.getValue(conditionContext);
        if (value == null || valueType.isAssignableFrom(value.getClass())) {
            return isNE((V) value, conditionContext);
        }
        return false;
    }

    protected boolean isNE(V value, ConditionContext conditionContext) throws Exception {
        return false;
    }

    @Override
    public final boolean lt(Operand operand, ConditionContext conditionContext) throws Exception {
        Object value = operand.getValue(conditionContext);
        if (value == null || valueType.isAssignableFrom(value.getClass())) {
            return isLT((V) value, conditionContext);
        }
        return false;
    }

    protected boolean isLT(V value, ConditionContext conditionContext) throws Exception {
        return false;
    }

    @Override
    public final boolean le(Operand operand, ConditionContext conditionContext) throws Exception {
        Object value = operand.getValue(conditionContext);
        if (value == null || valueType.isAssignableFrom(value.getClass())) {
            return isLE((V) value, conditionContext);
        }
        return false;
    }

    protected boolean isLE(V value, ConditionContext conditionContext) throws Exception {
        return false;
    }

    @Override
    public final boolean gt(Operand operand, ConditionContext conditionContext) throws Exception {
        Object value = operand.getValue(conditionContext);
        if (value == null || valueType.isAssignableFrom(value.getClass())) {
            return isGT((V) value, conditionContext);
        }
        return false;
    }

    protected boolean isGT(V value, ConditionContext conditionContext) throws Exception {
        return false;
    }

    @Override
    public final boolean ge(Operand operand, ConditionContext conditionContext) throws Exception {
        Object value = operand.getValue(conditionContext);
        if (value == null || valueType.isAssignableFrom(value.getClass())) {
            return isGE((V) value, conditionContext);
        }
        return false;
    }

    protected boolean isGE(V value, ConditionContext conditionContext) throws Exception {
        return false;
    }

}
