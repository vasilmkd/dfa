package io.vasilev.dfa;

public record Transition(State from, State to, Letter letter) {
}
