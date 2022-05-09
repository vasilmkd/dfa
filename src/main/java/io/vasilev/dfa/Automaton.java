package io.vasilev.dfa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public final class Automaton implements Serializable {

    private final State initialState;
    private final State finalState;
    private final Set<State> states;
    private final Set<Transition> transitions;
    private transient TransitionMap map;

    public Automaton(State initialState, State finalState, Set<State> states, Set<Transition> transitions) {
        this.initialState = initialState;
        this.finalState = finalState;
        this.states = states;
        this.transitions = transitions;
        this.map = TransitionMap.fromTransitionSet(transitions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Automaton a) {
            return initialState.equals(a.initialState) &&
                    finalState.equals(a.finalState) &&
                    states.equals(a.states) &&
                    transitions.equals(a.transitions);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialState, finalState, states, transitions);
    }

    public boolean accept(String word) {
        return loop(initialState, word);
    }

    private boolean loop(State current, String remaining) {
        if (remaining.isEmpty()) return current.equals(finalState);
        final var letter = new Letter(remaining.charAt(0));
        final var next = map.get(current, letter);
        return next.map(s -> loop(s, remaining.substring(1))).orElse(false);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = TransitionMap.fromTransitionSet(transitions);
    }
}
