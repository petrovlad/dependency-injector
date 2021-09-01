package by.petrovlad.injector.examples.cyclic;

import by.petrovlad.injector.annotation.Inject;

public class ComplexCycleB {

    private ComplexCycleC c;
    private CycleOfTwoA a;

    @Inject
    public ComplexCycleB(ComplexCycleC c, CycleOfTwoA a) {
        this.c = c;
        this.a = a;
    }
}
