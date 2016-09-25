package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.facts.weight.IsHeavy;
import com.mbenabda.techwatch.testES.facts.weight.WeightLimit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WeightTest {
    private static final Float WEIGHT_LIMIT = 50F;
    private static final Long PIANO_ID = 1L;
    private static final Long TRIANGLE_ID = 2L;

    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Before
    public void setUp() {
        assertEquals(0, kie.session().getFactCount());
    }

    @Test
    public void unless_specified_otherwise_the_weight_should_be_limited_to_a_reasonable_weight_by_default() {
        kie.session().fireAllRules();
        assertEquals(WEIGHT_LIMIT, kie.session().getObjects(o -> o instanceof WeightLimit).stream().findFirst().map(o -> (WeightLimit) o).get().getValue());

    }

    @Test
    public void should_be_too_heavy_when_weight_is_over_the_limit() {
        Instrument piano = new Instrument();
        piano.setId(PIANO_ID);
        piano.setAverageWeight(200f);

        kie.session().insert(piano);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsHeavy);
        assertArrayEquals(asList(new IsHeavy(PIANO_ID)).toArray(), inferred.toArray());
    }

    @Test
    public void should_not_be_too_heavy_when_weight_is_under_the_limit() {
        Instrument triangle = new Instrument();
        triangle.setId(TRIANGLE_ID);
        triangle.setAverageWeight(.5f);

        kie.session().insert(triangle);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsHeavy);
        assertArrayEquals(new IsHeavy[0], inferred.toArray());
    }
}
