package by.petrovlad.injector.exception;

public class ConstructorNotFoundException extends RuntimeException {

    public ConstructorNotFoundException() {
        super();
    }

    public ConstructorNotFoundException(String message) {
        super(message);
    }
}
