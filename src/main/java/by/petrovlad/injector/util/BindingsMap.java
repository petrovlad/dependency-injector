package by.petrovlad.injector.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class BindingsMap {
    // key - intf, value - pair impl+scope
    private Map<Class<?>, ImplClass<?>> bindings;

    public void setBindings(Map<Class<?>, ImplClass<?>> bindings) {
        this.bindings = bindings;
    }

    public Map<Class<?>, ImplClass<?>> getBindings() {
        return bindings;
    }

    public BindingsMap() {
        this.bindings = new Hashtable<>();
    }

    public BindingsMap(HashMap<Class<?>, ImplClass<?>> bindings) {
        this.bindings = bindings;
    }

    public <T> ImplClass<?> addBinding(Class<T> intf, ImplClass<?> impl) {
        if (intf == null || impl == null) {
            throw new IllegalArgumentException();
        }
        return bindings.put(intf, impl);
    }

    public <T> ImplClass<?> addBinding(Class<T> intf, Class<? extends T> impl, Scope scope) {
        if (intf == null || impl == null || scope == null) {
            throw new IllegalArgumentException();
        }
        return bindings.put(intf, new ImplClass<>(impl, scope));
    }

    public boolean containsKey(Class<?> intf) {
        return bindings.containsKey(intf);
    }

    public ImplClass<?> get(Class<?> key) {
        return bindings.get(key);
    }
}
