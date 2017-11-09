package com.mastermind.model.entities.types;

import java.util.*;

public class Combination {
    private int size;
    private List<Integer> elements;

    public Combination(int size) {
        this.size = size;
        elements = new ArrayList<>(size);
        elements.addAll(Collections.nCopies(size, null));
    }

    public Combination(Integer... elements) {
        this.size = elements.length;
        this.elements = new ArrayList<>(size);
        this.elements.addAll(Arrays.asList(elements));
    }

    public boolean isComplete() {
        return elements.stream().noneMatch(Objects::isNull);
    }

    public int getSize() {
        return size;
    }

    public List<Integer> getElements() {
        return elements;
    }

    public Integer setElement(int index, Integer element) {
        return elements.set(index, element);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Combination that = (Combination) o;
        // TODO Complete deep comparison
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}
