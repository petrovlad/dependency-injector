package by.petrovlad.injector.impl;

import by.petrovlad.injector.Provider;

public class SingletoneProvider<T> implements Provider<T> {
    @Override
    public T getInstance() {
        return null;
    }
}
