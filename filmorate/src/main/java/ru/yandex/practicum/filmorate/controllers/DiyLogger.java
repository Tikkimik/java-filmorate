package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class DiyLogger {
    @GetMapping("/home")
    public String homePage() {
        log.info("Получен запрос.");
        return "Get: Home";
    }
}

