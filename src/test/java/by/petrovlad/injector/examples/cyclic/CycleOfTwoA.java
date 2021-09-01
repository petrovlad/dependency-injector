package by.petrovlad.injector.examples.cyclic;

import by.petrovlad.injector.annotation.Inject;

public class CycleOfTwoA {

    private CycleOfTwoB b;

    @Inject
    public CycleOfTwoA(CycleOfTwoB b) {
        this.b = b;
    }
}