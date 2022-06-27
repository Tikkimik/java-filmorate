package ru.yandex.practicum.filmorate.model;

public class GeneratorId {
    long id;

    public GeneratorId() {
        this.id = 1;
    }

    public long generateId () {
        return id++;
    }
}
