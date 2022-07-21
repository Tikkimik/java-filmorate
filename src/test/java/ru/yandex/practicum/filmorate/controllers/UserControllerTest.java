package ru.yandex.practicum.filmorate.controllers;

import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.TestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import org.springframework.web.client.RequestCallback;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest(classes = FilmorateApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    User user;

    @LocalServerPort
    Integer port;

    @Autowired
    TestRestTemplate restTemplate;


    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setId(1L);
        user.setEmail("Stive@apple.com");
        user.setLogin("apple");
        user.setName("Steve");
        user.setBirthday(LocalDate.of(1975, 5, 25));
    }

    @Test
    public void testGetAllFilms (){
        ResponseEntity<List> result = restTemplate.getForEntity("http://localhost:" + port + "/users", List.class);
        System.out.println(result);
        assertNotNull(result, "Обьекты не возвращаются");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPostUser (){
        ResponseEntity<User> result = restTemplate.postForEntity("http://localhost:" + port + "/users", user, User.class);
        System.out.println(result);
        assertEquals(user.getName(), result.getBody().getName(), "Возвращается неверное имя");
        assertEquals(user.getLogin(), result.getBody().getLogin(), "Возвращается неверный логин");
        assertEquals(user.getEmail(), result.getBody().getEmail(), "Возвращается неверное имя почты");
        assertEquals(user.getBirthday(), result.getBody().getBirthday(), "Возвращеается неверная дата рождения");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPostFailLoginUser (){
        User failLogin = new User();
        failLogin.setLogin("dolore ullamco");
        failLogin.setEmail("mail.ru");
        failLogin.setBirthday(LocalDate.of(2446, 8, 20));
        ResponseEntity<User> result = restTemplate.postForEntity("http://localhost:" + port + "/users", failLogin, User.class);
        System.out.println(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPostFailEmailUser (){
        User failEmail = new User();
        failEmail.setLogin("dolore ullamco");
        failEmail.setName("");
        failEmail.setEmail("mail.ru");
        failEmail.setBirthday(LocalDate.of(1980, 8, 20));
        ResponseEntity<User> result = restTemplate.postForEntity("http://localhost:" + port + "/users", failEmail, User.class);
        System.out.println(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    @Test
    public void testPostFailBirthdayUser (){
        User failBirthday = new User();
        failBirthday.setLogin("dolore");
        failBirthday.setName("");
        failBirthday.setEmail("test@mail.ru");
        failBirthday.setBirthday(LocalDate.of(2446, 8, 20));
        ResponseEntity<User> result = restTemplate.postForEntity("http://localhost:" + port + "/users", failBirthday, User.class);
        System.out.println(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Возвращается не верный Http статус код");
    }

    RequestCallback requestCallback(final User updatedInstance) {
        return clientHttpRequest -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(clientHttpRequest.getBody(), updatedInstance);
            clientHttpRequest.getHeaders().add(
                    HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            clientHttpRequest.getHeaders();
        };
    }

    @Test
    public void Tested (){
        ResponseEntity<User> response = restTemplate
                .exchange("http://localhost:" + port + "/users", HttpMethod.POST, new HttpEntity<>(user), User.class);
        System.out.println(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        User updatedInstance = new User();
        updatedInstance.setName("asd");
        updatedInstance.setId(response.getBody().getId());
        restTemplate.execute(
                "http://localhost:" + port + "/users",
                HttpMethod.PUT,
                requestCallback(updatedInstance),
                clientHttpResponse -> null);

        ResponseEntity<List> result = restTemplate.getForEntity("http://localhost:" + port + "/users", List.class);
        System.out.println(result);

    }

    @Test
    public void testPut() {
        ResponseEntity<User> response = restTemplate
                .exchange("http://localhost:" + port + "/users", HttpMethod.POST, new HttpEntity<>(user), User.class);
        System.out.println(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        User updatedInstance = new User();
        updatedInstance.setId(1L);
        updatedInstance.setName("buka");
        String resourceUrl = "http://localhost:" + port + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> requestUpdate = new HttpEntity<>(updatedInstance, headers);
        restTemplate.exchange(resourceUrl, HttpMethod.PUT, requestUpdate, User.class);
        System.out.println(response);

        ResponseEntity<List> result = restTemplate.getForEntity("http://localhost:" + port + "/users", List.class);
        System.out.println(result);
    }

    @Test
    public void testPutUser (){
        User userPut = new User();
        userPut.setId(10L);
        userPut.setName("misha");
        userPut.setEmail("misha@ya.ru");
        userPut.setLogin("9lMiXalIll");
        userPut.setBirthday(LocalDate.of(2001, 9, 11));

        ResponseEntity<User> result1 = restTemplate.postForEntity("http://localhost:" + port + "/users", userPut, User.class);
        System.out.println(result1);

        userPut.setId(10L);
        userPut.setName("Andreaz");
        userPut.setLogin("777LolKekCheburek");

        restTemplate.exchange("http://localhost:" + port + "/users", HttpMethod.PUT, new HttpEntity<>(userPut), User.class);

        ResponseEntity<List> result = restTemplate.getForEntity("http://localhost:" + port + "/users", List.class);
        System.out.println(result);
        assertNotNull(result, "Обьекты не возвращаются");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Возвращается не верный Http статус код");
    }
}