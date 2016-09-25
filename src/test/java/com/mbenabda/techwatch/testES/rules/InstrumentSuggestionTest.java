package com.mbenabda.techwatch.testES.rules;

import com.google.common.base.Joiner;
import com.mbenabda.techwatch.testES.TestEsApp;
import com.mbenabda.techwatch.testES.facts.age.YouthLimitAge;
import com.mbenabda.techwatch.testES.facts.answers.Budget;
import com.mbenabda.techwatch.testES.facts.answers.DateOfBirth;
import com.mbenabda.techwatch.testES.facts.answers.illness.HasBackPain;
import com.mbenabda.techwatch.testES.facts.noise.LoudnessThreshold;
import com.mbenabda.techwatch.testES.facts.suggestion.InstrumentSuggestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestEsApp.class)
public class InstrumentSuggestionTest {

    private static final Logger LOG = LoggerFactory.getLogger(InstrumentSuggestionTest.class);

    @Inject
    KieSession session;

    @Test
    public void suggest_instruments() {
        Arrays.asList(
            new YouthLimitAge(18),
            new LoudnessThreshold(.5f),

            new DateOfBirth(LocalDate.of(1988, 7, 18)),
            new HasBackPain(),
            new Budget(80)
        ).stream()
            .forEach(session::insert);

        session.fireAllRules();

        List<String> suggestions = session
            .getObjects(o -> o instanceof InstrumentSuggestion).stream()
            .map(o -> (InstrumentSuggestion) o)
            .map(suggestion -> suggestion.getInstrument().getCategory() + " " + suggestion.getInstrument().getName())
            .collect(toList());

        LOG.info("\n suggestions : \n" + Joiner.on("\n").join(suggestions));

    }

}
