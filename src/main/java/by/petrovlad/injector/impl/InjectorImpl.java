package by.petrovlad.injector.impl;

import by.petrovlad.injector.Injector;
import by.petrovlad.injector.Provider;
import by.petrovlad.injector.util.BindingsMap;
import by.petrovlad.injector.util.ImplClass;
import by.petrovlad.injector.util.Scope;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InjectorImpl implements Injector {

    // если несколько конструкторов, то бросать исключение при регистрации или при создании?
    // fail fast же

    private final BindingsMap bindings;
    private final Map<ImplClass<?>, Object> singletonCache;

    public InjectorImpl() {
        bindings = new BindingsMap();
        singletonCache = new HashMap<>();
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        ImplClass<?> implClass = bindings.get(type);

        if (implClass == null) {
            return null;
        }

        return switch (bindings.get(type).getScope()) {
            case PROTOTYPE -> () -> (T) instantiatePrototype(implClass);
            case SINGLETON-> () -> (T) instantiateSingleton(implClass);
        };
    }

    private Object instantiatePrototype(ImplClass<?> implClass) {

        Constructor<?> resultConstructor = implClass.getConstructor();

        Object[] args = Arrays
                .stream(resultConstructor.getParameterTypes())
                .map(type -> instantiatePrototype(bindings.get(type)))
                .toArray();
        try {
            return resultConstructor.newInstance();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object instantiateSingleton(ImplClass<?> implClass) {
        // чекнуть, есть ли в кеше
        Object singleton = singletonCache.get(implClass);
        if (singleton == null) {
        // если нет - создать и добавить в кеш
            singleton = instantiatePrototype(implClass);
            singletonCache.put(implClass, singleton);
        }

        return singleton;
    }

    @Override
    public <T> void bind(Class<T> intf, Class<? extends T> impl) {
        bindings.addBinding(intf, impl, Scope.PROTOTYPE);
    }

    @Override
    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) {
        bindings.addBinding(intf, impl, Scope.SINGLETON);
    }
}
