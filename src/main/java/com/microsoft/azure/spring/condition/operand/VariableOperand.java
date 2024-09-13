package com.microsoft.azure.spring.condition.operand;


import com.microsoft.azure.spring.condition.ConditionContext;
import com.microsoft.azure.spring.condition.ConditionUtils;
import com.microsoft.azure.spring.condition.VariableInfo;
import com.microsoft.azure.spring.condition.VariableInfoProvider;
import com.microsoft.azure.spring.condition.value.VariableValueProvider;
/**
 * @author serkan
 */
public class VariableOperand<T> implements Operand<T> {

    private static final NullOperand NULL_OPERAND = new NullOperand();

    private final String variableName;
    private final VariableValueProvider variableValueProvider;
    private final String propPath;
    private final Operand<T> variableOperand;

    public VariableOperand(String variableName, VariableInfoProvider variableInfoProvider) throws Exception {
        this(variableName, variableInfoProvider, null);
    }

    public VariableOperand(String variableName, VariableInfoProvider variableInfoProvider,
                           String propPath) throws Exception {
        this.variableName = variableName;
        this.propPath = propPath;
        VariableInfo variableInfo = variableInfoProvider.getVariableInfo(variableName);
        if (variableInfo == null) {
            throw new Exception("UNABLE_TO_FIND_TYPE_OF_VARIABLE_FOR_CONDITION");
        }
        Class variableType = variableInfo.getType();
        if (variableType == null) {
            throw new Exception("UNABLE_TO_FIND_METADATA_OF_VARIABLE_FOR_CONDITION");
        }
        this.variableValueProvider = new VariableValueProvider<>(variableInfo.getIndex(), propPath);
        this.variableOperand = createVariableOperand(variableType, variableValueProvider, propPath);
    }

    private static Operand createVariableOperand(Class variableType,
                                                 VariableValueProvider variableValueProvider,
                                                 String propPath) {
        if (ConditionUtils.isBooleanType(variableType)) {
            return new BooleanOperand(variableValueProvider);
        } else if (ConditionUtils.isCharacterType(variableType)) {
            return new CharacterOperand(variableValueProvider);
        } else if (ConditionUtils.isNumberType(variableType)) {
            return new NumberOperand(variableValueProvider);
        } else if (ConditionUtils.isStringType(variableType)) {
            return new StringOperand(variableValueProvider);
        } else if (propPath == null || "".equals(propPath)) {
            return new ObjectOperand(variableValueProvider);
        } else {
            return null;
        }
    }

    public String getVariableName() {
        return variableName;
    }

    public String getPropPath() {
        return propPath;
    }

    @Override
    public T getValue(ConditionContext conditionContext) throws Exception {
        return (T) variableValueProvider.getValue(conditionContext);
    }

    private Operand getVariableOperand(ConditionContext conditionContext) throws Exception {
        if (variableOperand != null) {
            return variableOperand;
        }
        Object variableValue = variableValueProvider.getValue(conditionContext);
        if (variableValue == null) {
            return NULL_OPERAND;
        }
        // TODO Cache variable operand
        return createVariableOperand(variableValue.getClass(), variableValueProvider, null);
    }

    @Override
    public boolean eq(Operand operand, ConditionContext conditionContext) throws Exception {
        Operand varOperand = getVariableOperand(conditionContext);
        if (varOperand == null) {
            return false;
        }
        return varOperand.eq(operand, conditionContext);
    }

    @Override
    public boolean ne(Operand operand, ConditionContext conditionContext) throws Exception {
        Operand varOperand = getVariableOperand(conditionContext);
        if (varOperand == null) {
            return false;
        }
        return varOperand.ne(operand, conditionContext);
    }

    @Override
    public boolean lt(Operand operand, ConditionContext conditionContext) throws Exception {
        Operand varOperand = getVariableOperand(conditionContext);
        if (varOperand == null) {
            return false;
        }
        return varOperand.lt(operand, conditionContext);
    }

    @Override
    public boolean le(Operand operand, ConditionContext conditionContext) throws Exception {
        Operand varOperand = getVariableOperand(conditionContext);
        if (varOperand == null) {
            return false;
        }
        return varOperand.le(operand, conditionContext);
    }

    @Override
    public boolean gt(Operand operand, ConditionContext conditionContext) throws Exception {
        Operand varOperand = getVariableOperand(conditionContext);
        if (varOperand == null) {
            return false;
        }
        return varOperand.gt(operand, conditionContext);
    }

    @Override
    public boolean ge(Operand operand, ConditionContext conditionContext) throws Exception {
        Operand varOperand = getVariableOperand(conditionContext);
        if (varOperand == null) {
            return false;
        }
        return varOperand.ge(operand, conditionContext);
    }

}
