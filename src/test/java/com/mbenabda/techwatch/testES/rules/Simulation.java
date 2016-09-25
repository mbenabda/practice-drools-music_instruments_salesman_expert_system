package com.mbenabda.techwatch.testES.rules;

import com.google.common.base.Joiner;
import com.mbenabda.techwatch.testES.TestEsApp;
import com.mbenabda.techwatch.testES.domain.Genre;
import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.facts.age.YouthLimitAge;
import com.mbenabda.techwatch.testES.facts.answers.Budget;
import com.mbenabda.techwatch.testES.facts.answers.DateOfBirth;
import com.mbenabda.techwatch.testES.facts.answers.LikesGenre;
import com.mbenabda.techwatch.testES.facts.answers.illness.HasBackPain;
import com.mbenabda.techwatch.testES.facts.noise.LoudnessThreshold;
import com.mbenabda.techwatch.testES.facts.suggestion.GenreSuggestion;
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
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestEsApp.class)
public class Simulation {

    private static final Logger LOG = LoggerFactory.getLogger(Simulation.class);

    @Inject
    KieSession session;

    @Test
    public void suggest_instruments() {
        Arrays.asList(
            new YouthLimitAge(18),
            new LoudnessThreshold(.5f),

            howOldAreYou(),
            doYouHaveAnyHealthIssue(),
            howMuchWouldYouSpendInAStudyInstrument()
        ).stream()
            .forEach(session::insert);

        session.fireAllRules();

        pickGenresYouLikeFromSuggestedGenres()
            .stream()
            .map(genre -> new LikesGenre(genre.getId()))
            .forEach(session::insert);

        session.fireAllRules();

        LOG.info(
            "\n suggestions : {}\n",
            Joiner.on("\n").join(
                suggestInstruments()
                .map(instrument -> instrument.getCategory() + " " + instrument.getName())
                .collect(toList())
            )
        );

    }

    private Stream<Instrument> suggestInstruments() {
        return session
            .getObjects(o -> o instanceof InstrumentSuggestion).stream()
            .map(o -> (InstrumentSuggestion) o)
            .map(suggestion -> suggestion.getInstrument());
    }

    private Budget howMuchWouldYouSpendInAStudyInstrument() {
        return new Budget(80);
    }

    private Collection<?> doYouHaveAnyHealthIssue() {
        return Arrays.asList(
            new HasBackPain()
        );
    }

    private DateOfBirth howOldAreYou() {
        return new DateOfBirth(LocalDate.of(1988, 7, 18));
    }

    private Collection<Genre> pickGenresYouLikeFromSuggestedGenres() {
        return sessionObjectsOfType(GenreSuggestion.class)
            .map(suggestion -> suggestion.getGenre())
            .collect(toList());
    }

    private <T> Stream<T> sessionObjectsOfType(Class<T> type) {
        return session
            .getObjects(type::isInstance).stream()
            .map(type::cast);
    }

}
