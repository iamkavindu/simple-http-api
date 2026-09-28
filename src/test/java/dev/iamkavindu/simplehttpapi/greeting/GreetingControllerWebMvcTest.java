package dev.iamkavindu.simplehttpapi.greeting;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GreetingController.class)
class GreetingControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GreetingService greetingService;

    @Test
    void should_return_hello_with_name() throws Exception {
        when(greetingService.greet("alice")).thenReturn(new SuccessResponse("Hello Alice"));

        mockMvc.perform(get("/hello-world").param("name", "alice"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                  "message": "Hello Alice"
                }
                """));
    }

    @Test
    void should_return_400_when_service_rejects_name() throws Exception {
        when(greetingService.greet("zoe")).thenThrow(new GreetingException("Invalid Input"));
        mockMvc.perform(get("/hello-world").param("name", "zoe"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                {
                  "error": "Invalid Input"
                }
                """));
    }

    @Test
    void should_pass_null_to_service_when_name_is_missing() throws Exception {
        when(greetingService.greet(null)).thenThrow(new GreetingException("Invalid Input"));
        mockMvc.perform(get("/hello-world"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                  {
                    "error": "Invalid Input"
                  }
                  """));
    }
}
