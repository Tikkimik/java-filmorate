package ru.yandex.practicum.filmorate.exceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException() {
        super("Illegal Id value");
    }
}
