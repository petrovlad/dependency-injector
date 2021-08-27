package by.petrovlad.injector.exception;

public class TooManyConstructorsException extends RuntimeException {

    public TooManyConstructorsException() {
        super();
    }

    public TooManyConstructorsException(String message) {
        super(message);
    }

}
