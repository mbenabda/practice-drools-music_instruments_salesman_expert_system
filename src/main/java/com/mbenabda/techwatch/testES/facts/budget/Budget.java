package com.mbenabda.techwatch.testES.facts.budget;

import java.util.Objects;

public class Budget {
    private final float value;

    public Budget(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget that = (Budget) o;
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
