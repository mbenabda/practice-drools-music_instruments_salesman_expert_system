package com.mbenabda.techwatch.testES.facts.age;

import java.util.Objects;

public class YouthLimitAge {

    private final short value;

    public YouthLimitAge(short value) {
        this.value = value;
    }

    public YouthLimitAge(int value) {
        this(Integer.valueOf(value).shortValue());
    }

    public short getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YouthLimitAge that = (YouthLimitAge) o;
        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
