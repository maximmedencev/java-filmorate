package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class FilmControllerTests {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new FilmController()).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void shouldThrowValidationExceptionWhenNameIsEmptyWhenCreate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content("{\n" +
                                "    \"name\": \"\",\n" +
                                "    \"description\": \"Film description\",\n" +
                                "    \"releaseDate\": \"2020-12-12\",\n" +
                                "    \"duration\": 30\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowValidationExceptionWhenDescriptionLengthOver200SymbolsWhenCreate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content("{\n" +
                                "    \"name\": \"Film name\",\n" +
                                "    \"description\": \"" + "a".repeat(201) + "\",\n" +
                                "    \"releaseDate\": \"2020-12-12\",\n" +
                                "    \"duration\": 30\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowValidationExceptionForEarlyDateWhenCreate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content("{\n" +
                                "    \"name\": \"Film name\",\n" +
                                "    \"description\": \"Film description\",\n" +
                                "    \"releaseDate\": \"1800-12-12\",\n" +
                                "    \"duration\": 30\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowValidationExceptionWhenDurationLessThanZeroWhenCreate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .content("{\n" +
                                "    \"name\": \"Film name\",\n" +
                                "    \"description\": \"Film description\",\n" +
                                "    \"releaseDate\": \"2020-12-12\",\n" +
                                "    \"duration\": -30\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowValidationExceptionWhenNameIsEmptyWhenUpdate()
            throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/films")
                .content("{\n" +
                        "    \"name\": \"Film name\",\n" +
                        "    \"description\": \"Film description\",\n" +
                        "    \"releaseDate\": \"2020-12-12\",\n" +
                        "    \"duration\": 30\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .content("{\n" +
                                "    \"name\": \"\",\n" +
                                "    \"description\": \"Film description\",\n" +
                                "    \"releaseDate\": \"2020-12-12\",\n" +
                                "    \"duration\": 30\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowValidationExceptionWhenDescriptionLengthOver200SymbolsWhenUpdate()
            throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/films")
                .content("{\n" +
                        "    \"name\": \"Film name\",\n" +
                        "    \"description\": \"Film description\",\n" +
                        "    \"releaseDate\": \"2020-12-12\",\n" +
                        "    \"duration\": 30\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .content("{\n" +
                                "    \"name\": \"Film name\",\n" +
                                "    \"description\": \"" + "a".repeat(201) + "\",\n" +
                                "    \"releaseDate\": \"2020-12-12\",\n" +
                                "    \"duration\": 30\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowValidationExceptionForEarlyDateWhenUpdate()
            throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/films")
                .content("{\n" +
                        "    \"name\": \"Film name\",\n" +
                        "    \"description\": \"Film description\",\n" +
                        "    \"releaseDate\": \"2020-12-12\",\n" +
                        "    \"duration\": 30\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .content("{\n" +
                                "    \"name\": \"Film name\",\n" +
                                "    \"description\": \"Film description\",\n" +
                                "    \"releaseDate\": \"1800-12-12\",\n" +
                                "    \"duration\": 30\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowValidationExceptionWhenDurationLessThanZeroWhenUpdate()
            throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/films")
                .content("{\n" +
                        "    \"name\": \"Film name\",\n" +
                        "    \"description\": \"Film description\",\n" +
                        "    \"releaseDate\": \"2020-12-12\",\n" +
                        "    \"duration\": 30\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .content("{\n" +
                                "    \"name\": \"Film name\",\n" +
                                "    \"description\": \"Film description\",\n" +
                                "    \"releaseDate\": \"2000-12-12\",\n" +
                                "    \"duration\": -30\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
