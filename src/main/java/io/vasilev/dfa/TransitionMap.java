package io.vasilev.dfa;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

final class TransitionMap {

    private final Map<State, Map<Letter, State>> mapping;

    private TransitionMap(Map<State, Map<Letter, State>> mapping) {
        this.mapping = mapping;
    }

    Optional<State> get(State from, Letter letter) {
        return Optional.ofNullable(mapping.get(from)).flatMap(m -> Optional.ofNullable(m.get(letter)));
    }

    static TransitionMap fromTransitionSet(Set<Transition> set) {
        final var mapping = new HashMap<State, Map<Letter, State>>();
        set.forEach(t -> mapping.computeIfAbsent(t.from(), k -> new HashMap<>()).put(t.letter(), t.to()));
        return new TransitionMap(mapping);
    }
}
