package com.mbenabda.techwatch.testES.rules;

import com.mbenabda.techwatch.testES.domain.Genre;
import com.mbenabda.techwatch.testES.facts.suggestion.GenreSuggestion;
import com.mbenabda.techwatch.testES.facts.age.YouthLimitAge;
import com.mbenabda.techwatch.testES.facts.answers.DateOfBirth;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class GenreSuggestionTest {


    private static final int MAJORITY = 18;
    private static final LocalDate _1940 = LocalDate.of(1940, 01, 01);
    private static final LocalDate _1965 = LocalDate.of(1965, 01, 01);
    private static final LocalDate _1985 = LocalDate.of(1985, 01, 01);
    private static final Long GENRE_ID = 1L;

    @Rule
    public final StatefulKieSessionRule kie = new StatefulKieSessionRule();

    @Before
    public void setUp() {
        assertEquals(0, kie.session().getFactCount());
        kie.session().insert(new YouthLimitAge(MAJORITY));
    }
    @Test
    public void people_probably_like_genres_that_were_popular_when_they_were_young_adults() {
        Genre genre = new Genre();
        genre.setId(GENRE_ID);
        genre.setGoldenAgeStartingYear(_1965.getYear());
        genre.setGoldenAgeEndingYear(_1985.getYear());

        kie.session().insert(new DateOfBirth(_1940));
        kie.session().insert(genre);
        kie.session().fireAllRules();

        Optional<GenreSuggestion> first = kie.session().getObjects(o -> o instanceof GenreSuggestion).stream().map(o -> (GenreSuggestion) o).findFirst();
        assertEquals(new GenreSuggestion(genre), first.get());
    }

}
