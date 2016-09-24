package com.mbenabda.techwatch.testES.facts;

import org.apache.commons.lang3.Validate;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Person {
    private LocalDate dateOfBirth;

    public Person(LocalDate dateOfBirth) {
        Validate.notNull(dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    public short getAge() {
        Integer age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age.shortValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(dateOfBirth, person.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateOfBirth);
    }
}
