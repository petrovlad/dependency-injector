package by.petrovlad.injector.examples.constructors.notfound;

import by.petrovlad.injector.annotation.Inject;

public class ConstructorNotFoundImplA {

    ConstructorNotFoundImplA() {
    }

    @Inject
    ConstructorNotFoundImplA(Object o) {

    }
}