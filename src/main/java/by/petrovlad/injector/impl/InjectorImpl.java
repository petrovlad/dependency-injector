package by.petrovlad.injector.impl;

import by.petrovlad.injector.Injector;
import by.petrovlad.injector.Provider;
import by.petrovlad.injector.exception.BindingNotFoundException;
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
        singletonCache = new Hashtable<>();
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }

        ImplClass<?> implClass = bindings.get(type);

        if (implClass == null) {
            return null;
        }

        synchronized (bindings) {
            // this logic should be here.
            // because first we need to register candidates (aka BeanDefinitions)
            // and then to check whether registered candidates has cyclic dependencies
            if (hasCyclicDependencies(implClass)) {
                throw new CyclicInjectionException(implClass.getImpl().getName());
            }

            return switch (bindings.get(type).getScope()) {
                case PROTOTYPE -> () -> (T) instantiatePrototype(implClass);
                case SINGLETON -> () -> (T) instantiateSingleton(implClass);
            };
        }
    }

    private synchronized Object instantiatePrototype(ImplClass<?> implClass) {

        Constructor<?> resultConstructor = implClass.getConstructor();

        List<Class<?>> types = Arrays
                .stream(resultConstructor.getParameterTypes())
                .collect(Collectors.toList());

        List<Object> args = new ArrayList<>();

        for (Class<?> type : types) {
            // check if all types has registered implementation
            if (!bindings.containsKey(type)) {
                throw new BindingNotFoundException(type.getName());
            }
            args.add(getProvider(type).getInstance());
        }

        try {
            return resultConstructor.newInstance(args.toArray());
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object instantiateSingleton(ImplClass<?> implClass) {
        // first check if it exists in cache
        Object singleton = singletonCache.get(implClass);
        if (singleton == null) {
            // if doesn't - instantiate as a prototype + add to cache
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
        bindByScope(intf, impl, Scope.PROTOTYPE);
    }

    @Override
    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) {
        bindByScope(intf, impl, Scope.SINGLETON);
    }

    private <T> void bindByScope(Class<T> intf, Class<? extends T> impl, Scope scope) {
        if (intf == null || impl == null || scope == null) {
            throw new IllegalArgumentException();
        }
        ImplClass<?> implClass = new ImplClass<>(impl, scope);
        bindings.addBinding(intf, implClass);
    }
}
