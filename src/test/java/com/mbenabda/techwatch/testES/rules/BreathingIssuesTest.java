package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.facts.BlacklistedKindOfInstruments;
import com.mbenabda.techwatch.testES.facts.answers.illness.HasBreathingIssues;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertArrayEquals;

public class BreathingIssuesTest {
    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Test
    public void should_not_suggest_woodwinds_when_breathing_issues() {
        kie.session().insert(new HasBreathingIssues());
        kie.session().fireAllRules();
        Collection<?> inferred = kie.session().getObjects(o -> o instanceof BlacklistedKindOfInstruments);
        assertArrayEquals(blackList("WOODWIND", "BRASS"), inferred.toArray());
    }

    private BlacklistedKindOfInstruments[] blackList(String... kindOfInstruments) {
        List<BlacklistedKindOfInstruments> list = Arrays
            .asList(kindOfInstruments).stream()
            .map(BlacklistedKindOfInstruments::new)
            .collect(toList());
        return list.toArray(new BlacklistedKindOfInstruments[list.size()]);
    }

}
