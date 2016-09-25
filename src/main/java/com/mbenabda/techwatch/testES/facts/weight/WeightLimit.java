package com.mbenabda.techwatch.testES.facts.weight;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class WeightLimit {
    private Float value;

    public WeightLimit(final Float value) {
        setValue(value);
    }

    public Float getValue() {
        return value;
    }

    public void setValue(final Float value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightLimit that = (WeightLimit) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
