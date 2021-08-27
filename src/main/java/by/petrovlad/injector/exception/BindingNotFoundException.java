package by.petrovlad.injector.exception;

public class BindingNotFoundException extends RuntimeException {

    public BindingNotFoundException() {
        super();
    }

    public BindingNotFoundException(String message) {
        super(message);
    }
}
