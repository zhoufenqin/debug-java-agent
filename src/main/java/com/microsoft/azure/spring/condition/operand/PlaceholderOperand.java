package com.microsoft.azure.spring.condition.operand;


import com.microsoft.azure.spring.condition.ConditionContext;
import com.microsoft.azure.spring.condition.ConditionUtils;
import com.microsoft.azure.spring.condition.value.PlaceholderValueProvider;
import com.microsoft.azure.spring.condition.value.ValueProvider;

/**
 * @author serkan
 */
public class PlaceholderOperand<V> implements Operand<V> {

    private final String placeholderName;
    private final PlaceholderValueProvider<V> placeholderValueProvider;
    private final Operand<V> placeHolderOperand;

    public PlaceholderOperand(String placeholderName,
                              PlaceholderValueProvider<V> placeholderValueProvider) {
        this.placeholderName = placeholderName;
        this.placeholderValueProvider = placeholderValueProvider;
        this.placeHolderOperand = createPlaceholderOperand(placeholderValueProvider);
    }

    private Operand createPlaceholderOperand(PlaceholderValueProvider<V> placeholderValueProvider) {
        Class<V> placeholderValueType = placeholderValueProvider.getValueType();
        if (ConditionUtils.isBooleanType(placeholderValueType)) {
            return new BooleanOperand((ValueProvider<Boolean>) placeholderValueProvider);
        } else if (ConditionUtils.isCharacterType(placeholderValueType)) {
            return new CharacterOperand((ValueProvider<Character>) placeholderValueProvider);
        } else if (ConditionUtils.isNumberType(placeholderValueType)) {
            return new NumberOperand((ValueProvider<Number>) placeholderValueProvider);
        } else if (ConditionUtils.isStringType(placeholderValueType)) {
            return new StringOperand((ValueProvider<String>) placeholderValueProvider);
        } else {
            return new ObjectOperand(placeholderValueProvider);
        }
    }

    public Class<V> getPlaceholderValueType() {
        return placeholderValueProvider.getValueType();
    }

    @Override
    public V getValue(ConditionContext conditionContext) throws Exception {
        return placeholderValueProvider.getValue(conditionContext);
    }

    @Override
    public boolean eq(Operand operand, ConditionContext conditionContext) throws Exception {
        if (placeHolderOperand == null) {
            return false;
        }
        return placeHolderOperand.eq(operand, conditionContext);
    }

    @Override
    public boolean ne(Operand operand, ConditionContext conditionContext) throws Exception {
        if (placeHolderOperand == null) {
            return false;
        }
        return placeHolderOperand.ne(operand, conditionContext);
    }

    @Override
    public boolean lt(Operand operand, ConditionContext conditionContext) throws Exception {
        if (placeHolderOperand == null) {
            return false;
        }
        return placeHolderOperand.lt(operand, conditionContext);
    }

    @Override
    public boolean le(Operand operand, ConditionContext conditionContext) throws Exception {
        if (placeHolderOperand == null) {
            return false;
        }
        return placeHolderOperand.le(operand, conditionContext);
    }

    @Override
    public boolean gt(Operand operand, ConditionContext conditionContext) throws Exception {
        if (placeHolderOperand == null) {
            return false;
        }
        return placeHolderOperand.gt(operand, conditionContext);
    }

    @Override
    public boolean ge(Operand operand, ConditionContext conditionContext) throws Exception {
        if (placeHolderOperand == null) {
            return false;
        }
        return placeHolderOperand.ge(operand, conditionContext);
    }

}
