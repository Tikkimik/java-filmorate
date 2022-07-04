package ru.yandex.practicum.filmorate.model;

public class GeneratorId {
    private long id;

    public GeneratorId() {
        this.id = 1;
    }

    public long generateId () {
        return id++;
    }
}
