package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
public class Mpa {
    @NotBlank
    @JsonProperty("id")
    int id;
    @NotBlank
    @JsonProperty("name")
    String name;
}
