package by.petrovlad.injector.exception;

public class CyclicInjectionException extends RuntimeException {
    public CyclicInjectionException() {
        super();
    }

    public CyclicInjectionException(String message) {
        super(message);
    }
}
