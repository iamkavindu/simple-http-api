package dev.iamkavindu.simplehttpapi.greeting;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GreetingIntegrationTest {
    private static final String INVALID_INPUT = """
                                          { "error": "Invalid Input" }
                                          """;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({"alice, Alice", "Mike, Mike"})
    void should_greet_valid_names(String name, String expected) throws Exception {
        mockMvc.perform(get("/hello-world").param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          { "message": "Hello %s" }
                          """.formatted(expected)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"nancy", "Zoe", "", "   ", "1bob"})
    void should_reject_invalid_names(String name) throws Exception {
        mockMvc.perform(get("/hello-world").param("name", name))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(INVALID_INPUT));
    }

    @Test
    void should_reject_missing_name_param() throws Exception {
        mockMvc.perform(get("/hello-world"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(INVALID_INPUT));
    }
}
