package com.mbenabda.techwatch.testES.facts.noise;

import java.util.Objects;

public class LoudnessThreshold {
    private final Float value;

    public LoudnessThreshold(Float value) {
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoudnessThreshold that = (LoudnessThreshold) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
