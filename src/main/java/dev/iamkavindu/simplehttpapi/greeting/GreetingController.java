package dev.iamkavindu.simplehttpapi.greeting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/hello-world")
    ResponseEntity<SuccessResponse> greetHello(@RequestParam(value = "name", required = false) String name) {
        var greetingsTo = greetingService.greet(name);
        return ResponseEntity.ok(greetingsTo);
    }
}
