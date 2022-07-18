package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.TestConfig;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest(classes = FilmorateApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {

    Film film;

    @LocalServerPort
    Integer port;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    public void beforeEach() {
        film = new Film();
        film.setId(1L);
        film.setReleaseDate(LocalDate.of(1985, 7, 3));
        film.setDuration(116);
        film.setDescription("Семнадцатилетний Марти МакФлай пришел вчера домой пораньше. На 30 лет раньше");
        film.setName("Назад в будущее");
    }

    @Test
    public void testGetAllFilms (){
        ResponseEntity<List> result = restTemplate.getForEntity("http://localhost:" + port + "/films", List.class);
        System.out.println(result);
        assertNotNull(result, "Обьекты не возвращаются");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPostFilm (){
        ResponseEntity<Film> result = restTemplate.postForEntity("http://localhost:" + port + "/films", film, Film.class);
        System.out.println(result);
        assertEquals(film.getDescription(), result.getBody().getDescription(), "Возвращается неверное описание фильма");
        assertEquals(film.getReleaseDate(), result.getBody().getReleaseDate(), "Возвращается неверная дата релиза");
        assertEquals(film.getDuration(), result.getBody().getDuration(), "Возвращается неверная продолжительнось фильма");
        assertEquals(film.getName(), result.getBody().getName(), "Возвращемый неверное имя фильма");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPostFailNameFilm (){
        film.setName("");
        ResponseEntity<Film> result = restTemplate.postForEntity("http://localhost:" + port + "/films", film, Film.class);
        System.out.println(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPostFailDescriptionFilm (){
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.");
        ResponseEntity<Film> result = restTemplate.postForEntity("http://localhost:" + port + "/films", film, Film.class);
        System.out.println(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPostFailReleaseDateFilm (){
        film.setReleaseDate(LocalDate.of(1890, 3, 25));
        ResponseEntity<Film> result = restTemplate.postForEntity("http://localhost:" + port + "/films", film, Film.class);
        System.out.println(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPostFailDurationFilm (){
        film.setDuration(-200);
        ResponseEntity<Film> result = restTemplate.postForEntity("http://localhost:" + port + "/films", film, Film.class);
        System.out.println(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPutFilm (){
        restTemplate.postForEntity("http://localhost:" + port + "/films", film, Film.class);
        film.setId(1L);
        restTemplate.put("http://localhost:" + port + "/films", film, Film.class);
        ResponseEntity<List> result = restTemplate.getForEntity("http://localhost:" + port + "/films", List.class);
        System.out.println(result);
        assertNotNull(result, "Обьекты не возвращаются");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPutFailIdFilm (){
        restTemplate.postForEntity("http://localhost:" + port + "/films", film, Film.class);
        film.setId(-1L);
        restTemplate.put("http://localhost:" + port + "/films", film, Film.class);
        ResponseEntity<List> result = restTemplate.getForEntity("http://localhost:" + port + "/films", List.class);
        System.out.println(result);
        assertFalse(result.getBody().contains(film), "Возвращается измененный обьект");
        assertNotNull(result, "Обьекты не возвращаются");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Возвращается не верный Http статус код");
    }













}