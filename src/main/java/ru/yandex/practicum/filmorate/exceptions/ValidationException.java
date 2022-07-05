package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable exception) {
        super(message, exception);
    }
}
