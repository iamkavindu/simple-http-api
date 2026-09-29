package dev.iamkavindu.simplehttpapi.greeting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GreetingExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GreetingExceptionHandler.class);

    @ExceptionHandler(GreetingException.class)
    public ResponseEntity<ErrorResponse> handleGreetingException(GreetingException ex) {
        LOG.warn("Rejected greeting request: {}", ex.getMessage());
        var errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
