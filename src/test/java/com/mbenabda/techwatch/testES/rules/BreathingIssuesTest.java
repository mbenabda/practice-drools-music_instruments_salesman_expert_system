package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.facts.BannishCategory;
import com.mbenabda.techwatch.testES.facts.answers.illness.HasBreathingIssues;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;

public class BreathingIssuesTest {
    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Test
    public void should_not_suggest_woodwinds_when_breathing_issues() {
        kie.session().insert(new HasBreathingIssues());
        kie.session().fireAllRules();
        Collection<?> inferred = kie.session().getObjects(o -> o instanceof BannishCategory);
        assertArrayEquals(new BannishCategory[] { new BannishCategory("WOODWINDS") }, inferred.toArray());
    }

}
