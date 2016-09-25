package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.facts.budget.Budget;
import com.mbenabda.techwatch.testES.facts.budget.IsCheap;
import com.mbenabda.techwatch.testES.facts.budget.IsExpensive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ExpensivenessTest {

    private static final float BUDGET = 100;

    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Before
    public void setUp() {
        assertEquals(0, kie.session().getFactCount());
        kie.session().insert(new Budget(BUDGET));
    }

    @Test
    public void should_infer_instrument_expensiveness() {
        Instrument drum = new Instrument();
        drum.setName("drum");
        drum.setAverageLowEndPrice(BUDGET + 1);

        kie.session().insert(drum);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsExpensive);
        assertArrayEquals(asList(new IsExpensive("drum")).toArray(), inferred.toArray());
    }

    @Test
    public void should_infer_instrument_cheapness() {
        Instrument triangle = new Instrument();
        triangle.setName("triangle");
        triangle.setAverageLowEndPrice(BUDGET - 1);

        kie.session().insert(triangle);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsCheap);
        assertArrayEquals(asList(new IsCheap("triangle")).toArray(), inferred.toArray());
    }

}
