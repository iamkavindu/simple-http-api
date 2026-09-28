package dev.iamkavindu.simplehttpapi.greeting;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GreetingExceptionHandlerTest {
    private final GreetingExceptionHandler handler = new GreetingExceptionHandler();

    @Test
    void should_map_greeting_exception_to_400_with_error_body() {
        var response = handler.handleGreetingException(new GreetingException("Invalid Input"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(new ErrorResponse("Invalid Input"));
    }
}
