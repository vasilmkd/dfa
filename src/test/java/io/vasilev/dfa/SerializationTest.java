package io.vasilev.dfa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

final class SerializationTest {

    @Test
    @DisplayName("Letter serialization")
    void testLetterSerialization() {
        testSerialization(SerializationTest::generateLetter, Letter.class);
    }

    @Test
    @DisplayName("State serialization")
    void testStateSerialization() {
        testSerialization(SerializationTest::generateState, State.class);
    }

    @Test
    @DisplayName("Transition serialization")
    void testTransitionSerialization() {
        testSerialization(SerializationTest::generateTransition, Transition.class);
    }

    @Test
    @DisplayName("Automaton serialization")
    void testAutomatonSerialization() {
        testSerialization(SerializationTest::generateAutomaton, Automaton.class);
    }

    @Test
    @DisplayName("Deserialized automaton can test words")
    void testAutomatonDeserialization() {
        final var initialState = new State(0);
        final var finalState = new State(1);
        final var letter = new Letter('a');
        final var transition = new Transition(initialState, finalState, letter);
        final var automaton =
                new Automaton(initialState, finalState, Set.of(initialState, finalState), Set.of(transition));

        assertTrue(automaton.accept("a"));
        assertFalse(automaton.accept("b"));

        final var serialized = serialize(automaton);
        final var deserialized = deserialize(serialized, Automaton.class);

        assertNotNull(deserialized);
        assertEquals(automaton, deserialized);
        assertTrue(deserialized.accept("a"));
        assertFalse(deserialized.accept("b"));
    }

    private static <T extends Serializable> void testSerialization(Supplier<T> supplier, Class<T> cls) {
        final var expected = supplier.get();
        final var serialized = serialize(expected);
        final var deserialized = deserialize(serialized, cls);
        assertEquals(expected, deserialized);
    }

    private static <T extends Serializable> byte[] serialize(T obj) {
        try (final var baos = new ByteArrayOutputStream(); final var oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    private static <T extends Serializable> T deserialize(byte[] bytes, Class<T> cls) {
        try (final var bais = new ByteArrayInputStream(bytes); final var ois = new ObjectInputStream(bais)) {
            final var o = ois.readObject();
            return cls.cast(o);
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private static char generateLetterFromAlphabet() {
        final var alphabet = "abcdefghijklmnopqrstuvwxyz";
        final var rng = new Random();
        return alphabet.charAt(rng.nextInt(alphabet.length()));
    }

    private static int generateNonNegativeInteger() {
        final var rng = new Random();
        return rng.nextInt(Integer.MAX_VALUE);
    }

    private static Letter generateLetter() {
        final var c = generateLetterFromAlphabet();
        return new Letter(c);
    }

    private static State generateState() {
        final var n = generateNonNegativeInteger();
        return new State(n);
    }

    private static Transition generateTransition() {
        final var from = generateState();
        final var to = generateState();
        final var letter = generateLetter();
        return new Transition(from, to, letter);
    }

    private static Automaton generateAutomaton() {
        final var initialState = generateState();
        final var finalState = generateState();
        final var letter = generateLetter();
        final var transition = new Transition(initialState, finalState, letter);
        return new Automaton(initialState, finalState, Set.of(initialState, finalState), Set.of(transition));
    }
}
