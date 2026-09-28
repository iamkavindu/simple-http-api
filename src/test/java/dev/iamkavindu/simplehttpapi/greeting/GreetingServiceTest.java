package dev.iamkavindu.simplehttpapi.greeting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class GreetingServiceTest {
    private final GreetingService greetingService = new GreetingService();

    @ParameterizedTest
    @CsvSource({"alice, Alice", "Alice, Alice", "mike, Mike", "Mike, Mike", "a, A", "M, M"})
    void should_greet_names_starting_with_a_to_m_capitalized(String name, String expected) {
        assertThat(greetingService.greet(name)).isEqualTo(new SuccessResponse("Hello " + expected));
    }

    @ParameterizedTest
    @CsvSource({"mcKenzie, McKenzie", "aLICE, ALICE", "ALICE, ALICE"})
    void should_only_capitalize_first_letter_and_keep_the_rest(String name, String expected) {
        assertThat(greetingService.greet(name)).isEqualTo(new SuccessResponse("Hello " + expected));
    }

    @ParameterizedTest
    @ValueSource(strings = {"nancy", "Nancy", "zoe", "Z"})
    void should_reject_names_starting_with_n_to_z(String name) {
        assertInvalid(name);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void should_reject_null_empty_or_blank_names(String name) {
        assertInvalid(name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1bob", "_alice", "-mike", "Émile", "ábel"})
    void should_reject_names_not_starting_with_plain_ascii_letter(String name) {
        assertInvalid(name);
    }

    @Test
    void should_trim_surrounding_whitespace() {
        assertThat(greetingService.greet("  bob  ")).isEqualTo(new SuccessResponse("Hello Bob"));
    }

    @Test
    void should_decide_on_first_letter_after_trimming() {
        assertInvalid("  zoe");
    }

    private void assertInvalid(String name) {
        assertThatThrownBy(() -> greetingService.greet(name))
                .isInstanceOf(GreetingException.class)
                .hasMessage("Invalid Input");
    }
}
