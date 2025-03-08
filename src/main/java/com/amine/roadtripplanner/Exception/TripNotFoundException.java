package com.amine.roadtripplanner.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TripNotFoundException extends RuntimeException{
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public TripNotFoundException(String message) {
        super(message);
    }

    public TripNotFoundException(String field, String value) {
        super(String.format("User not found with %s: %s", field, value));
    }
}
