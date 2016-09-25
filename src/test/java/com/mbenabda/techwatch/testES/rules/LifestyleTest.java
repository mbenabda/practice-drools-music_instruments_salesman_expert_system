package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.facts.age.YouthLimitAge;
import com.mbenabda.techwatch.testES.facts.answers.Age;
import com.mbenabda.techwatch.testES.facts.answers.IsNomad;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.Assert.*;

public class LifestyleTest {
    private static final Logger LOG = LoggerFactory.getLogger(AgeTest.class);

    private static final int YOUTH_LIMIT = 30;

    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Before
    public void setUp() {
        assertEquals(0, kie.session().getFactCount());
        kie.session().insert(new YouthLimitAge(YOUTH_LIMIT));
    }

    @Test
    public void young_people_tend_to_travel_a_lot() {
        kie.session().insert(new Age(YOUTH_LIMIT - 1));
        kie.session().fireAllRules();

        assertTrue(sessionObjectOfType(IsNomad.class).isPresent());
    }

    @Test
    public void mature_people_are_more_sedentary() {
        kie.session().insert(new Age(YOUTH_LIMIT + 1));
        kie.session().fireAllRules();

        assertFalse(sessionObjectOfType(IsNomad.class).isPresent());
    }

    private <T> Optional<T> sessionObjectOfType(Class<T> type) {
        return kie.session()
            .getObjects(type::isInstance).stream()
            .findFirst()
            .map(type::cast);
    }
}
