package by.petrovlad.injector.util;

import by.petrovlad.injector.annotation.Inject;
import by.petrovlad.injector.exception.ConstructorNotFoundException;
import by.petrovlad.injector.exception.TooManyConstructorsException;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ImplClass<T> {

    private Class<T> impl;

    // saved working constructor, so
    // 1. we don't need to determine it every time
    // 2. fail fast (on registration)
    private Constructor<?> constructor;

    private Scope scope;

    private ImplClass() {
    }

    public ImplClass(Class<T> impl, Scope scope) {
        this.impl = impl;
        this.scope = scope;
        this.constructor = determineWorkingConstructor(impl);
    }

    public ImplClass(Class<T> impl, Constructor<T> constructor, Scope scope) {
        this.impl = impl;
        this.constructor = constructor;
        this.scope = scope;
    }

    private static Constructor<?> determineWorkingConstructor(Class<?> impl) {
        Constructor<?>[] annotatedConstructors = getAnnotatedConstructors(impl);

        if (annotatedConstructors.length > 1) {
            throw new TooManyConstructorsException(impl.getName());
        }

        if (annotatedConstructors.length == 1) {
            return annotatedConstructors[0];
        }

        // if we went down here, take default constructor
        try {
            return impl.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new ConstructorNotFoundException(impl.getName());
        }

    }

    private static Constructor<?>[] getAnnotatedConstructors(Class<?> impl) {
        return Arrays.stream(impl.getConstructors())
                .filter((constructor1 -> constructor1.isAnnotationPresent(Inject.class)))
                .toArray(Constructor<?>[]::new);
    }

    public Class<T> getImpl() {
        return impl;
    }

    public void setImpl(Class<T> impl) {
        this.impl = impl;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }
}
