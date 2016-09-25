package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.facts.loudness.IsLoud;
import com.mbenabda.techwatch.testES.facts.loudness.IsSilent;
import com.mbenabda.techwatch.testES.facts.loudness.LoudnessThreshold;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LoudnessTest {

    private static final float THRESHOLD = .5f;

    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Before
    public void setUp() {
        assertEquals(0, kie.session().getFactCount());
        kie.session().insert(new LoudnessThreshold(THRESHOLD));
    }

    @Test
    public void should_infer_instrument_loudness() {
        Instrument drum = new Instrument();
        drum.setName("drum");
        drum.setLoudness(THRESHOLD + 1);

        kie.session().insert(drum);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsLoud);
        assertEquals(3, kie.session().getFactCount());
        assertArrayEquals(asList(new IsLoud("drum")).toArray(), inferred.toArray());
    }

    @Test
    public void should_infer_instrument_silent() {
        Instrument triangle = new Instrument();
        triangle.setName("triangle");
        triangle.setLoudness(THRESHOLD - 1);

        kie.session().insert(triangle);
        kie.session().fireAllRules();

        Collection<?> inferred = kie.session().getObjects(o -> o instanceof IsSilent);
        assertEquals(3, kie.session().getFactCount());
        assertArrayEquals(asList(new IsSilent("triangle")).toArray(), inferred.toArray());
    }

}
