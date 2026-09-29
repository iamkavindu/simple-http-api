package dev.iamkavindu.simplehttpapi.greeting;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GreetingService {
    private static final Logger LOG = LoggerFactory.getLogger(GreetingService.class);

    public SuccessResponse greet(String name) {
        var validatedName = validate(name);
        return new SuccessResponse("Hello " + StringUtils.capitalize(validatedName));
    }

    private String validate(String input) {
        if (input == null || input.isBlank()) {
            LOG.debug("Validation failed: name is blank");
            throw new GreetingException("Invalid Input");
        }

        var trimmedInput = input.strip();

        char firstLetter = trimmedInput.toLowerCase(Locale.ROOT).charAt(0);
        if (firstLetter >= 'a' && firstLetter <= 'm') {
            return trimmedInput;
        }
        LOG.debug("Validation failed: name must start with a-m, got '{}'", firstLetter);
        throw new GreetingException("Invalid Input");
    }
}
