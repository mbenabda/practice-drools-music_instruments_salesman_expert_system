package com.mbenabda.techwatch.testES.facts.answers;

import org.apache.commons.lang3.Validate;

import java.time.LocalDate;
import java.util.Objects;

public class DateOfBirth {
    private LocalDate value;

    public DateOfBirth(LocalDate value) {
        Validate.notNull(value);
        this.value = value;
    }

    public LocalDate getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateOfBirth that = (DateOfBirth) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
