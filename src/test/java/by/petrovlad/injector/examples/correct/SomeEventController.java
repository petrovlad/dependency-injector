package by.petrovlad.injector.examples.correct;

import by.petrovlad.injector.annotation.Inject;

public class SomeEventController {

    private EventService eventService;

    @Inject
    public SomeEventController(EventService eventService) {
        this.eventService = eventService;
    }
}
