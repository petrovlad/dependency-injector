package by.petrovlad.injector.util;

public class Binding {
    private Class<?> intf;
    private Class<?> impl;
    private Scope scope;

    public Binding() {}

    public Binding(Class<?> intf, Class<?> impl, Scope scope) {
        this.intf = intf;
        this.impl = impl;
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Class<?> getIntf() {
        return intf;
    }

    public void setIntf(Class<?> intf) {
        this.intf = intf;
    }

    public Class<?> getImpl() {
        return impl;
    }

    public void setImpl(Class<?> impl) {
        this.impl = impl;
    }
}
