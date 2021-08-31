package by.petrovlad.injector.impl;

import by.petrovlad.injector.Injector;
import by.petrovlad.injector.Provider;
import by.petrovlad.injector.exception.CyclicInjectionException;
import by.petrovlad.injector.util.BindingsMap;
import by.petrovlad.injector.util.ImplClass;
import by.petrovlad.injector.util.Scope;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class InjectorImpl implements Injector {

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
            return resultConstructor.newInstance(args);
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

    private boolean hasCyclicDependencies(ImplClass<?> implClass) {
        return hasCyclicDependencies(implClass, new HashSet<>());
    }

    private boolean hasCyclicDependencies(ImplClass<?> implClass, Set<ImplClass<?>> classTrace) {
        classTrace.add(implClass);

        Constructor<?> constructor = implClass.getConstructor();
        boolean has = false;
        List<ImplClass<?>> dependencies = Arrays
                .stream(constructor.getParameterTypes())
                .map(bindings::get)
                .collect(Collectors.toList());

        for (ImplClass<?> dependency : dependencies) {
            if (classTrace.contains(dependency)) {
                has = true;
                break;
            }
            has = hasCyclicDependencies(dependency, classTrace);
        }
        classTrace.remove(implClass);
        return has;
    }

    @Override
    public <T> void bind(Class<T> intf, Class<? extends T> impl) {
        bindByScope(intf, impl, Scope.SINGLETON);
    }

    @Override
    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) {
        bindByScope(intf, impl, Scope.PROTOTYPE);
    }

    private <T> void bindByScope(Class<T> intf, Class<? extends T> impl, Scope scope) {
        ImplClass<?> implClass = bindings.addBinding(intf, impl, scope);
        if (hasCyclicDependencies(implClass)) {
            throw new CyclicInjectionException(implClass.getImpl().getName());
        }
    }
}
