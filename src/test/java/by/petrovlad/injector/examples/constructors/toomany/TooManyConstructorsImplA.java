package by.petrovlad.injector.examples.constructors.toomany;

import by.petrovlad.injector.annotation.Inject;

public class TooManyConstructorsImplA implements TooManyConstructors {

    @Inject
    public TooManyConstructorsImplA() {
    }

    @Inject
    public TooManyConstructorsImplA(Object o) {
    }
}