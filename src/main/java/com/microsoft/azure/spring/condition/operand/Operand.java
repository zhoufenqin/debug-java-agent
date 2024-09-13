package com.microsoft.azure.spring.condition.operand;


import com.microsoft.azure.spring.condition.ConditionContext;

/**
 * @author serkan
 */
public interface Operand<V> {

    V getValue(ConditionContext conditionContext) throws Exception;

    default boolean eq(Operand operand, ConditionContext conditionContext) throws Exception {
        return false;
    }

    default boolean ne(Operand operand, ConditionContext conditionContext) throws Exception {
        return false;
    }

    default boolean lt(Operand operand, ConditionContext conditionContext) throws Exception {
        return false;
    }

    default boolean le(Operand operand, ConditionContext conditionContext) throws Exception {
        return false;
    }

    default boolean gt(Operand operand, ConditionContext conditionContext) throws Exception {
        return false;
    }

    default boolean ge(Operand operand, ConditionContext conditionContext) throws Exception {
        return false;
    }

}
