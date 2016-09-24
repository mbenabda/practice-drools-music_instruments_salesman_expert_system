package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.facts.Person;
import com.mbenabda.techwatch.testES.facts.age.IsOld;
import com.mbenabda.techwatch.testES.facts.age.IsYoung;
import com.mbenabda.techwatch.testES.facts.age.YouthLimitAge;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class AgeTest {

    private static final int MAJORITY = 18;

    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Before
    public void setUp() {
        assertEquals(0, kie.session().getFactCount());
        kie.session().insert(new YouthLimitAge(MAJORITY));
    }
    @Test
    public void should_infer_underage() {
        Person underAgePerson = new Person(LocalDate.now().minusYears(MAJORITY));

        kie.session().insert(underAgePerson);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsYoung);
        assertEquals(3, kie.session().getFactCount());
        assertEquals(1, inferred.size());
    }

    @Test
    public void should_infer_majority() {
        Person majorPerson = new Person(LocalDate.now().minusYears(MAJORITY + 1));
        kie.session().insert(majorPerson);

        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsOld);
        assertEquals(3, kie.session().getFactCount());
        assertEquals(1, inferred.size());
    }
}
