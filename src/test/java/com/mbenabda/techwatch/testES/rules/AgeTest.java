package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.facts.age.IsOld;
import com.mbenabda.techwatch.testES.facts.age.IsYoung;
import com.mbenabda.techwatch.testES.facts.age.YouthLimitAge;
import com.mbenabda.techwatch.testES.facts.answers.Age;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
        kie.session().insert(new Age(MAJORITY));
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsYoung);
        assertEquals(1, inferred.size());
    }

    @Test
    public void should_infer_majority() {
        kie.session().insert(new Age(MAJORITY + 1));

        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsOld);
        assertEquals(1, inferred.size());
    }
}
