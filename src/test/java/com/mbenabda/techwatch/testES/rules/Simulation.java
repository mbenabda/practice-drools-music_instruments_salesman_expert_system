package com.mbenabda.techwatch.testES.rules;

import com.google.common.base.Joiner;
import com.mbenabda.techwatch.testES.TestEsApp;
import com.mbenabda.techwatch.testES.domain.Genre;
import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.facts.PlaysInstrument;
import com.mbenabda.techwatch.testES.facts.age.YouthLimitAge;
import com.mbenabda.techwatch.testES.facts.answers.Budget;
import com.mbenabda.techwatch.testES.facts.answers.DateOfBirth;
import com.mbenabda.techwatch.testES.facts.answers.LikesGenre;
import com.mbenabda.techwatch.testES.facts.answers.illness.HasBackPain;
import com.mbenabda.techwatch.testES.facts.answers.IsNomad;
import com.mbenabda.techwatch.testES.facts.answers.lifestyle.DedicatedHoursOfPracticePerWeek;
import com.mbenabda.techwatch.testES.facts.answers.lifestyle.LivesInAnAppartment;
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
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestEsApp.class)
public class Simulation {

    private static final Logger LOG = LoggerFactory.getLogger(Simulation.class);

    @Inject
    KieSession session;


    private DedicatedHoursOfPracticePerWeek howMuchHoursPerWeekCouldYouDedicateToPractice() {
        return new DedicatedHoursOfPracticePerWeek(2);
    }

    private Object whereDoYouLive() {
        return new LivesInAnAppartment();
    }

    private Collection<PlaysInstrument> doYouPlayAnyInstruments() {
        List<Instrument> suggestedInstruments = sessionObjectsOfType(Instrument.class)
            .collect(toList());

        return suggestedInstruments.isEmpty()
            ? Collections.emptyList()
            : Arrays.asList(new PlaysInstrument(suggestedInstruments.get(0).getId()));
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

    private Optional<IsNomad> doYouTravelALot() {
        return Optional.of(new IsNomad());
    }


    @Test
    public void suggest_instruments() {
        Arrays.asList(
            howOldAreYou(),
            doYouHaveAnyHealthIssue(),
            howMuchWouldYouSpendInAStudyInstrument()
        ).stream()
            .forEach(session::insert);

        doYouTravelALot().ifPresent(session::insert);

        session.fireAllRules();

        pickGenresYouLikeFromSuggestedGenres()
            .stream()
            .map(genre -> new LikesGenre(genre.getId()))
            .forEach(session::insert);

        session.fireAllRules();

        session.insert(whereDoYouLive());

        doYouPlayAnyInstruments().stream()
            .forEach(session::insert);

        session.insert(howMuchHoursPerWeekCouldYouDedicateToPractice());

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
        return sessionObjectsOfType(InstrumentSuggestion.class)
            .map(suggestion -> suggestion.getInstrument());
    }

    private <T> Stream<T> sessionObjectsOfType(Class<T> type) {
        return session
            .getObjects(type::isInstance).stream()
            .map(type::cast);
    }

}
