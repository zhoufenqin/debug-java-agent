package com.microsoft.azure.spring.logpoint;

import com.microsoft.azure.spring.probe.Probe;
import com.microsoft.azure.spring.probe.ProbeAction;

/**
 * @author yasin
 */
public class LogPointAction implements ProbeAction<LogPointContext> {


    public static final String ACTION_ID = "logpoint";

    private final LogPointContext context;

    public LogPointAction(LogPointContext context) {
        this.context = context;
    }

    @Override
    public String id() {
        return ACTION_ID;
    }

    @Override
    public boolean isDisabled() {
        return context.disabled;
    }


    @Override
    public LogPointContext getContext() {
        return context;
    }

    @Override
    public void onProbe(Probe probe,
                        Class<?> clazz, Object obj, String methodName,
                        String[] localVarNames, Object[] localVarValues, String printLogs) {
        try {
            StringBuilder logMessageBuilder = new StringBuilder();
            logMessageBuilder.
                    append("On logpoint in method ").
                    append(probe.getClassName()).
                    append(".").
                    append(methodName).
                    append(" on line ").
                    append(probe.getLineNo()).
                    append("\n");
            for (int i = 0; i < localVarNames.length; i++) {
                logMessageBuilder.
                        append("\t- ").
                        append(localVarNames[i]).
                        append(": ").
                        append(localVarValues[i]).
                        append("\n");
            }
            System.out.println(logMessageBuilder.toString());

            System.out.println(printLogs);


        } catch (Throwable t) {
            System.out.println(
                    String.format(
                            "Logpoint failed in class %s on line %d",
                            probe.getClassName(), probe.getLineNo()));
        }
    }
}
