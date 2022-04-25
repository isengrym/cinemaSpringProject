package ua.klieshchunov.spring.cinemaSpringProject.service.exceptions;

public class NoFreePlacesException extends RuntimeException {
    public NoFreePlacesException(String message) {
        super(message);
    }

    public NoFreePlacesException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFreePlacesException(Throwable cause) {
        super(cause);
    }
}
