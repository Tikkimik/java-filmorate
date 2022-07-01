package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    Film film;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController controller;

    @Test
    public void getAll() throws Exception {
        when(controller.getUsers()).thenReturn(List.of(
                new User(1L, "Antonio@mail.ru", "AntonioBanderas777", "Antonio Banderas", LocalDate.of(1970, 5, 29)),
                new User(2L, "Mainim@mail.ru", "login2", "name2", LocalDate.of(1999, 6, 9))
        ));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1,2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Antonio Banderas","name2")));
    }

    @Test
    public void createNormalUser() throws Exception {
//        when(controller.createNewUser(new User("15L", "Stive@Jobs.com", "Stive", LocalDate.of(1960, 1,15)))).thenReturn();
//        when(controller.createNewUser(new User(1L, "Antonio@mail.ru", "AntonioBanderas777", "Antonio Banderas", LocalDate.of(1970, 5, 29))));
//
//when
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1)))
//                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Antonio Banderas","name2")));
    }


    @Test
    void getUsers() {

    }

    @Test
    void createNewUser() {
    }

    @Test
    void updateUser() {
    }
}