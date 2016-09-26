package com.mbenabda.techwatch.testES.rules;

import com.google.common.base.Joiner;
import com.mbenabda.techwatch.testES.TestEsApp;
import com.mbenabda.techwatch.testES.domain.Genre;
import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.facts.PlaysInstrument;
import com.mbenabda.techwatch.testES.facts.answers.Budget;
import com.mbenabda.techwatch.testES.facts.answers.DateOfBirth;
import com.mbenabda.techwatch.testES.facts.answers.IsNomad;
import com.mbenabda.techwatch.testES.facts.answers.LikesGenre;
import com.mbenabda.techwatch.testES.facts.answers.lifestyle.DedicatedHoursOfPracticePerWeek;
import com.mbenabda.techwatch.testES.facts.answers.lifestyle.LivesInAHouse;
import com.mbenabda.techwatch.testES.facts.answers.lifestyle.LivesInAnAppartment;
import com.mbenabda.techwatch.testES.facts.suggestion.InstrumentSuggestion;
import com.mbenabda.techwatch.testES.repository.GenreRepository;
import com.mbenabda.techwatch.testES.repository.InstrumentRepository;
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
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestEsApp.class)
public class Simulation {

    private static final Logger LOG = LoggerFactory.getLogger(Simulation.class);

    @Inject
    KieSession session;

    @Inject
    InstrumentRepository instruments;

    @Inject
    GenreRepository genres;

    private DedicatedHoursOfPracticePerWeek howMuchHoursPerWeekCouldYouDedicateToPractice() {
        return new DedicatedHoursOfPracticePerWeek(35);
    }

    private Object whereDoYouLive() {
//        return new LivesInAHouse();
        return new LivesInAnAppartment();
    }

    private Collection<PlaysInstrument> doYouPlayAnyInstruments() {
        Instrument mpc = instruments.findByCategoryAndName("ELECTRONIC", "MPC");

        return Arrays.asList(
            new PlaysInstrument(mpc.getId())
        );
    }

    private Budget howMuchWouldYouSpendInAStudyInstrument() {
        return new Budget(150);
    }

    private Collection<?> doYouHaveAnyHealthIssue() {
        return Arrays.asList(
//            new HasBackPain()
        );
    }

    private DateOfBirth howOldAreYou() {
        return new DateOfBirth(LocalDate.of(1988, 7, 18));
    }


    private Collection<Genre> pickGenresYouLikeFromSuggestedGenres() {
        return Arrays.asList(
            "pop",
            "soul",
            "blues",
            "ska/raggae",
            "a capella"
        ).stream()
        .map(genres::findByCode)
        .collect(toList());
    }

    private Optional<IsNomad> doYouTravelALot() {
//        return Optional.of(new IsNomad());
        return Optional.empty();
    }


    @Test
    public void suggest_instruments() {
        Arrays.asList(
            howOldAreYou(),
            howMuchWouldYouSpendInAStudyInstrument()
        )
            .stream()
            .forEach(session::insert);

        doYouHaveAnyHealthIssue()
            .stream()
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

        session.insert("suggest");

        session.fireAllRules();

        LOG.info(
            "\n\nsuggestions : \n---------------\n{}\n\n",
            Joiner.on("\n").join(
                suggestedInstruments()
                .map(instrument -> instrument.getCategory() + " " + instrument.getName())
                .collect(toList())
            )
        );

    }

    private Stream<Instrument> suggestedInstruments() {
        return sessionObjectsOfType(InstrumentSuggestion.class)
            .map(suggestion -> suggestion.getInstrument());
    }

    private <T> Stream<T> sessionObjectsOfType(Class<T> type) {
        return session
            .getObjects(type::isInstance).stream()
            .map(type::cast);
    }

}
