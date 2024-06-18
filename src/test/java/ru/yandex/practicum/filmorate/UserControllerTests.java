package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTests {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController()).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsEmptyWhenCreate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content("{\n" +
                                "    \"email\": \"\",\n" +
                                "    \"login\": \"Lucky\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"2020-12-12\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsWrongWhenCreate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content("{\n" +
                                "    \"email\": \"wrongemail@\",\n" +
                                "    \"login\": \"Lucky\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"2020-12-12\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsEmptyWhenCreate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content("{\n" +
                                "    \"email\": \"user@mail.com\",\n" +
                                "    \"login\": \"\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"2020-12-12\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowExceptionWhenLoginContainsSpacesWhenCreate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content("{\n" +
                                "    \"email\": \"user@mail.com\",\n" +
                                "    \"login\": \"Login with spaces\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"2020-12-12\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowExceptionWhenBirthDayIsInFutureWhenCreate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content("{\n" +
                                "    \"email\": \"user@mail.com\",\n" +
                                "    \"login\": \"Lucky\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"" + LocalDate
                                .now()
                                .plusDays(1)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsEmptyWhenUpdate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content("{\n" +
                        "    \"email\": \"user@mail.com\",\n" +
                        "    \"login\": \"Lucky\",\n" +
                        "    \"name\": \"Bill\",\n" +
                        "    \"birthday\":\"2020-12-12\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .content("{\n" +
                                "    \"id\": \"1\",\n" +
                                "    \"email\": \"\",\n" +
                                "    \"login\": \"Lucky\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"2020-12-12\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsWrongWhenUpdate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content("{\n" +
                        "    \"email\": \"user@mail.com\",\n" +
                        "    \"login\": \"Lucky\",\n" +
                        "    \"name\": \"Bill\",\n" +
                        "    \"birthday\":\"2020-12-12\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .content("{\n" +
                                "    \"id\": \"1\",\n" +
                                "    \"email\": \"wrongemail@\",\n" +
                                "    \"login\": \"Lucky\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"2020-12-12\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsEmptyWhenUpdate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content("{\n" +
                        "    \"email\": \"user@mail.com\",\n" +
                        "    \"login\": \"Lucky\",\n" +
                        "    \"name\": \"Bill\",\n" +
                        "    \"birthday\":\"2020-12-12\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .content("{\n" +
                                "    \"id\": \"1\",\n" +
                                "    \"email\": \"user@mail.com\",\n" +
                                "    \"login\": \"\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"2020-12-12\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowExceptionWhenLoginContainsSpacesWhenUpdate()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content("{\n" +
                        "    \"email\": \"user@mail.com\",\n" +
                        "    \"login\": \"Lucky\",\n" +
                        "    \"name\": \"Bill\",\n" +
                        "    \"birthday\":\"2020-12-12\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .content("{\n" +
                                "    \"id\": \"1\",\n" +
                                "    \"email\": \"user@mail.com\",\n" +
                                "    \"login\": \"login with spaces\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"2020-12-12\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldThrowExceptionWhenBirthDayIsInFutureWhenUpdate()
            throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content("{\n" +
                        "    \"email\": \"user@mail.com\",\n" +
                        "    \"login\": \"Lucky\",\n" +
                        "    \"name\": \"Bill\",\n" +
                        "    \"birthday\":\"2020-12-12\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .content("{\n" +
                                "    \"id\": \"1\",\n" +
                                "    \"email\": \"user@mail.com\",\n" +
                                "    \"login\": \"Lucky\",\n" +
                                "    \"name\": \"Bill\",\n" +
                                "    \"birthday\":\"" + LocalDate
                                .now()
                                .plusDays(1)
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
