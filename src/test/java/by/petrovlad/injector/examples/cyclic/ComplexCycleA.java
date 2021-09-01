package by.petrovlad.injector.examples.cyclic;

import by.petrovlad.injector.annotation.Inject;

public class ComplexCycleA {

    private ComplexCycleB b;
    private CycleOfTwoA a;

    @Inject
    public ComplexCycleA(ComplexCycleB b, CycleOfTwoA a) {
        this.b = b;
        this.a = a;
    }
}
