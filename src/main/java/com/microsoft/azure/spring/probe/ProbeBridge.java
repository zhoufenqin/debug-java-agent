package com.microsoft.azure.spring.probe;

public final class ProbeBridge {

    public static void onProbe(String probeId, String conditionId, Class<?> clazz, Object obj,
                               String[] localVarNames, Object[] localVarValues, String printLogs) {
        InternalProbe probe = ProbeManager.getProbe(probeId);
        if (probe == null || probe.isRemoved()) {
            System.out.println("No probe could be found with id {}");
            return;
        }
        try {
            logProbe(probe, clazz, obj,localVarNames, localVarValues);

            handleOnProbe(probe, clazz, obj, localVarNames, localVarValues, printLogs);
        } catch (Throwable t) {
            System.out.println(
                    String.format(
                            "Probe handling failed in class %s on line %d",
                            probe.getClassName(), probe.getLineNo()));
        }
    }
    private static void logProbe(Probe probe, Class<?> clazz, Object object,
                                 String[] localVarNames, Object[] localVarValues) {
        StringBuilder logMessageBuilder = new StringBuilder();
        logMessageBuilder.
                append("On probe in class ").
                append(probe.getClassName()).
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
//        System.out.println(logMessageBuilder.toString());
    }


    private static void handleOnProbe(Probe probe, Class<?> clazz, Object object,
                                      String[] localVarNames, Object[] localVarValues, String printLogs) throws Exception {
        for (ProbeAction action : probe.actions()) {
//            System.out.printf(
//                    "Running action %s on probe in class %s on line %d ...\n",
//                    action, probe.getClassName(), probe.getLineNo());
            try {
                if (action.isDisabled()) {
                    System.out.printf(
                            "Skipping action %s on probe in class %s on line %d as it is disabled",
                            action, probe.getClassName(), probe.getLineNo());
                    continue;
                }

                action.onProbe(probe, clazz, object, probe.getMethodName(), localVarNames, localVarValues, printLogs);
            } catch (Throwable t) {
                System.out.println(
                        String.format(
                                "Error occurred while running action %s on probe in class %s on line %d",
                                action, probe.getClassName(), probe.getLineNo()));
                throw new Exception("handle probe error");
            }
        }
    }
}
