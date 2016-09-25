package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.facts.answers.Budget;
import com.mbenabda.techwatch.testES.facts.budget.CanAfford;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BudgetTest {
    private static final float BUDGET = 100;
    private static final long TRIANGLE_ID = 1L;
    private static final Long DRUM_ID = 2L;

    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Before
    public void setUp() {
        assertEquals(0, kie.session().getFactCount());
        kie.session().insert(new Budget(BUDGET));
    }

    @Test
    public void cannot_afford_instruments_over_budget() {
        Instrument drum = new Instrument();
        drum.setId(DRUM_ID);
        drum.setAverageLowEndPrice(BUDGET + 1);

        kie.session().insert(drum);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof CanAfford);
        assertArrayEquals(new CanAfford[0], inferred.toArray());
    }

    @Test
    public void can_afford_instrument_under_budget() {
        Instrument triangle = new Instrument();
        triangle.setId(TRIANGLE_ID);
        triangle.setAverageLowEndPrice(BUDGET - 1);

        kie.session().insert(triangle);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof CanAfford);
        assertArrayEquals(asList(new CanAfford(TRIANGLE_ID)).toArray(), inferred.toArray());
    }

}
