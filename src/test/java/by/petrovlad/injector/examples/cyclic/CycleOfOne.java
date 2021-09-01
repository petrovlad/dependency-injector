package by.petrovlad.injector.examples.cyclic;

import by.petrovlad.injector.annotation.Inject;

public class CycleOfOne {

    private CycleOfOne a;

    @Inject
    public CycleOfOne(CycleOfOne cycleOfOne) {
        this.a = cycleOfOne;
    }
}
