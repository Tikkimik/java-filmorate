package ru.yandex.practicum.filmorate.exceptions;

public class InvalidReleaseDate extends RuntimeException {
    public InvalidReleaseDate(String message) {
        super(message);
    }
}
