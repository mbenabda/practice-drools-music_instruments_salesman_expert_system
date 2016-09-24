package com.mbenabda.techwatch.testES.facts.age;

import java.util.Objects;

public final class Age {
    private final short value;

    public Age(final short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Age age = (Age) o;
        return value == age.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
