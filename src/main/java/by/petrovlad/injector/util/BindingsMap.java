package by.petrovlad.injector.util;

import java.util.HashMap;

public class BindingsMap {
    // key - intf, value - pair impl+scope
    private HashMap<Class<?>, ImplClass<?>> bindings;

    public void setBindings(HashMap<Class<?>, ImplClass<?>> bindings) {
        this.bindings = bindings;
    }

    public HashMap<Class<?>, ImplClass<?>> getBindings() {
        return bindings;
    }

    public BindingsMap() {
        this.bindings = new HashMap<>();
    }

    public BindingsMap(HashMap<Class<?>, ImplClass<?>> bindings) {
        this.bindings = bindings;
    }

    public <T> void addBinding(Class<T> intf, Class<? extends T> impl, Scope scope) {
        bindings.put(intf, new ImplClass<>(impl, scope));
    }

    public boolean containsKey(Class<?> intf) {
        return bindings.containsKey(intf);
    }

    public ImplClass<?> get(Class<?> key) {
        return bindings.get(key);
    }
}
