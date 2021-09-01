package by.petrovlad.injector.examples.cyclic;

import by.petrovlad.injector.annotation.Inject;

public class CycleOfTwoB {

    private CycleOfTwoA a;

    @Inject
    public CycleOfTwoB(CycleOfTwoA a) {
        this.a = a;
    }
}
