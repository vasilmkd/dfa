package io.vasilev.dfa;

import java.io.Serializable;

public record Transition(State from, State to, Letter letter) implements Serializable {
}
