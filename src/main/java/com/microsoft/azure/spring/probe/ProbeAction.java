package com.microsoft.azure.spring.probe;

/**
 * @author serkan
 */
public interface ProbeAction<C extends ProbeContext> {

    String id();

    C getContext();

    boolean isDisabled();

    void onProbe(Probe probe,
                 Class<?> clazz, Object obj, String methodName,
                 String[] localVarNames, Object[] localVarValues, String printLogs) throws Exception;

    default C unwrap(Class<C> clazz) {
        if (clazz.isAssignableFrom(getClass())) {
            return clazz.cast(this);
        } else {
            return null;
        }
    }

}
