package by.petrovlad.injector.examples.constructors.toomany;

import by.petrovlad.injector.annotation.Inject;

public class TooManyConstructorsImplB implements TooManyConstructors {

    @Inject
    public TooManyConstructorsImplB(Integer i) {
    }

    @Inject
    public TooManyConstructorsImplB(Object o) {
    }
}