package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.facts.answers.illness.HasBackPain;
import com.mbenabda.techwatch.testES.facts.weight.IsHeavy;
import com.mbenabda.techwatch.testES.facts.weight.WeightLimit;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

public class BackPainTest {

    private static final Float WEIGHT_LIMIT = 5f;
    private static final Long TRIANGLE_ID = 1L;
    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Test
    public void should_lower_the_weight_limit_when_back_issues() {
        kie.session().fireAllRules();
        Float previousLimit = currentWeightLimit().get().getValue();
        kie.session().insert(new HasBackPain());
        kie.session().fireAllRules();
        assertThat(currentWeightLimit().get().getValue(), lessThan(previousLimit));
    }

    @Test
    public void should_set_a_low_weight_limit_when_back_issues() {
        kie.session().insert(new HasBackPain());
        kie.session().fireAllRules();
        assertEquals(WEIGHT_LIMIT, currentWeightLimit().get().getValue());
    }

    @Test
    public void should_not_be_too_heavy() {
        Instrument triangle = new Instrument();
        triangle.setId(TRIANGLE_ID);
        triangle.setAverageWeight(WEIGHT_LIMIT - 1);

        kie.session().insert(triangle);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsHeavy);
        assertArrayEquals(new IsHeavy[0], inferred.toArray());
    }

    private Optional<WeightLimit> currentWeightLimit() {
        return kie.session().getObjects(o -> o instanceof WeightLimit).stream().findFirst().map(o -> (WeightLimit) o);
    }

}
