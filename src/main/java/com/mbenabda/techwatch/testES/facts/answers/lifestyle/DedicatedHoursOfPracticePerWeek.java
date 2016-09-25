package com.mbenabda.techwatch.testES.facts.answers.lifestyle;

import java.util.Objects;

public class DedicatedHoursOfPracticePerWeek {

    private final int value;

    public DedicatedHoursOfPracticePerWeek(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DedicatedHoursOfPracticePerWeek age = (DedicatedHoursOfPracticePerWeek) o;
        return value == age.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
