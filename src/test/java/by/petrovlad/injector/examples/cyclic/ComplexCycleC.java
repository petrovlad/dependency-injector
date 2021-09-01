package by.petrovlad.injector.examples.cyclic;

import by.petrovlad.injector.annotation.Inject;

public class ComplexCycleC {

    private ComplexCycleA a;
    private CycleOfTwoA aa;

    @Inject
    public ComplexCycleC(ComplexCycleA a, CycleOfTwoA aa) {
        this.a = a;
        this.aa = aa;
    }
}
