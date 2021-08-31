package by.petrovlad.injector.impl;

import by.petrovlad.injector.Provider;

import java.lang.reflect.Constructor;

public class PrototypeProvider<T> implements Provider<T> {
    private Class<T> impl;

    public PrototypeProvider(Class<T> impl) {
        this.impl = impl;
    }

    public Class<T> getImpl() {
        return impl;
    }

    public void setImpl(Class<T> impl) {
        this.impl = impl;
    }

    @Override
    public T getInstance() {
        Constructor<?>[] constructors = impl.getConstructors();

        return null;
    }
}
