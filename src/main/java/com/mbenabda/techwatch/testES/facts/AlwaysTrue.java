package com.mbenabda.techwatch.testES.facts;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class AlwaysTrue {
    boolean value = true;
    public AlwaysTrue() { }

    public boolean isValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlwaysTrue that = (AlwaysTrue) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("value", value)
            .toString();
    }
}
