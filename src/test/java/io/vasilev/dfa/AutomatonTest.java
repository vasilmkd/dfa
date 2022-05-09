package io.vasilev.dfa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class AutomatonTest {

    @ParameterizedTest
    @MethodSource("arguments")
    @DisplayName("Test automaton from assignment")
    void testAutomaton(String word, boolean expected) {
        final var state0 = new State(0);
        final var state1 = new State(1);
        final var state2 = new State(2);
        final var state3 = new State(3);

        final var states = Set.of(state0, state1, state2, state3);

        final var transitions = Set.of(
                new Transition(state0, state1, new Letter('a')),
                new Transition(state0, state2, new Letter('b')),
                new Transition(state1, state2, new Letter('d')),
                new Transition(state1, state3, new Letter('c')),
                new Transition(state3, state1, new Letter('a')),
                new Transition(state2, state3, new Letter('f'))
        );

        final var automaton = new Automaton(state0, state2, states, transitions);
        assertEquals(expected, automaton.accept(word));
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("", false),
                Arguments.of("b", true),
                Arguments.of("ba", false),
                Arguments.of("ad", true),
                Arguments.of("adfad", true),
                Arguments.of("adfacacacacad", true),
                Arguments.of("adfacacacacab", false),
                Arguments.of("xyz", false),
                Arguments.of("bfad", true),
                Arguments.of("bfac", false),
                Arguments.of("bfacacacacacacacacad", true),
                Arguments.of("bfacacacacacacacacadb", false)
        );
    }
}
