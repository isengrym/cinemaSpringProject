package ua.klieshchunov.spring.cinemaSpringProject.service.exceptions;

public class TicketAlreadyExistsException extends Exception {
    public TicketAlreadyExistsException(String message) {
        super(message);
    }

    public TicketAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
