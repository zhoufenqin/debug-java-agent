package com.microsoft.azure.spring.probe;

import java.util.Objects;

/**
 * @author serkan
 */
public class InternalProbe extends BaseProbe implements MutableProbe {

    final String ownerClassName;
    final String methodId;

    InternalProbe(String ownerClassName,
                  String id, String className, int lineNo, String methodName,
                  String methodId) {
        super(id, className, lineNo, methodName);
        this.ownerClassName = ownerClassName;
        this.methodId = methodId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Override
    public <A extends ProbeAction> A addAction(A action) {
        return (A) actions.putIfAbsent(action.id(), action);
    }

    @Override
    public <A extends ProbeAction> A replaceAction(A action) {
        return (A) actions.replace(action.id(), action);
    }

    @Override
    public <A extends ProbeAction> A removeAction(String id) {
        return (A) actions.remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternalProbe that = (InternalProbe) o;
        return lineNo == that.lineNo &&
                Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, lineNo);
    }

}
