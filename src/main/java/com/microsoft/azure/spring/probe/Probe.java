package com.microsoft.azure.spring.probe;

import java.util.Collection;

public interface Probe {

    String getId();

    String getClassName();

    int getLineNo();

    String getMethodName();

    <A extends ProbeAction> A getAction(String id);

    Collection<ProbeAction> actions();

    boolean hasAnyAction();

    boolean isRemoved();

}
