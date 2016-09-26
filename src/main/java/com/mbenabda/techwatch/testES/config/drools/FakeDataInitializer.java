package com.mbenabda.techwatch.testES.config.drools;

import com.mbenabda.techwatch.testES.domain.Genre;
import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.repository.GenreRepository;
import com.mbenabda.techwatch.testES.repository.InstrumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
public class FakeDataInitializer {
    private final GenreRepository genreRepository;
    private final InstrumentRepository instrumentRepository;

    public FakeDataInitializer(GenreRepository genreRepository, InstrumentRepository instrumentRepository) {
        this.genreRepository = genreRepository;
        this.instrumentRepository = instrumentRepository;
    }

    public void init() {
        instrumentRepository.save(Arrays.asList(
//                       category    , kind       , name        , weight , volume , price , loudness , requiredHoursOfPracticePerWeek
            instrument( "ACOUSTIC"   , "KEYBOARD" , "piano"     , 200f   , 4.8f   , 400   , .3f      , 8) ,
            instrument( "ACOUSTIC"   , "WOODWIND" , "accordion" , 5f     , 0.135f , 75    , .6f      , 4) ,
            instrument( "ACOUSTIC"   , "STRING"   , "guitar"    , 2f     , .11f   , 50    , .3f      , 2) ,
            instrument( "ACOUSTIC"   , "STRING"   , "violin"    , 1f     , .7f    , 100   , .3f      , 5) ,
            instrument( "ACOUSTIC"   , "STRING"   , "viola"     , 1.5f   , .9f    , 120   , .3f      , 5) ,
            instrument( "ACOUSTIC"   , "STRING"   , "cello"     , 8f     , .22f   , 175   , .2f      , 5) ,
            instrument( "ACOUSTIC"   , "STRING"   , "bass"      , 2f     , .11f   , 100   , .1f      , 4) ,
            instrument( "ACOUSTIC"   , "BRASS"    , "trumpet"   , 1.5f   , .016f  , 150   , .7f      , 8) ,
            instrument( "ACOUSTIC"   , "DRUM"     , "triangle"  , .25f   , .0003f , 12    , .1f      , 1) ,
            instrument( "ACOUSTIC"   , "DRUM"     , "drumkit"   , 85f    , 4.5f   , 250   , .9f      , 4) ,
            instrument( "ACOUSTIC"   , "DRUM"     , "djembe"    , 30f    , .3f    , 150   , .7f      , 2) ,
            instrument( "ACOUSTIC"   , "VOICE"    , "voice"     , 0      , 0      , 0     , .4f      , 7) ,
            instrument( "ELECTRONIC" , "KEYBOARD" , "keyboard"  , 4f     , .22f   , 40    , .3f      , 5) ,
            instrument( "ELECTRONIC" , "DRUM"     , "MPC"       , 3f     , .7f    , 300   , 2        , 5) ,
            instrument( "ELECTRONIC" , "STRING"   , "guitar"    , 2f     , .11f   , 150   , .7f      , 5)
        ));

        /*
            genre popularity over time derived from
            http://public.tableau.com/views/TracingtheLifecycleofEachMusicGenreintheU_S_/GenrePopularity?:embed=y&:loadOrderID=1&:display_count=yes
         */
        genreRepository.save(Arrays.asList(
            genre("jazz"         , 1958 , 1970),
            genre("folk"         , 1969 , 1984),
            genre("pop"          , 1985 , 2016),
            genre("hip hop"      , 2002 , 2016),
            genre("country"      , 1998 , 2016),
            genre("rock & roll"  , 1958 , 1980),
            genre("rock"         , 1962 , 2013),
            genre("r&b"          , 1982 , 2016),
            genre("soul"         , 1958 , 2000),
            genre("house/trance" , 1990 , 1998),
            genre("latin"        , 1998 , 2003),
            genre("blues"        , 1958 , 1977),
            genre("metal"        , 1998 , 2012),
            genre("punk"         , 2002 , 2013),
            genre("ska/raggae"   , 1989 , 1997),
            genre("dancehall"    , 2001 , 2007),
            genre("a capella"    , 1958 , 2016)
        ));


        // http://www.pakmusic.org/programs/private-lessons/instruments/
        Arrays.asList(
            instrument("ACOUSTIC"   , "piano"     ),
            instrument("ELECTRONIC" , "keyboard"  ),
            instrument("ACOUSTIC"   , "accordion" ),
            instrument("ACOUSTIC"   , "cello"     ),
            instrument("ACOUSTIC"   , "bass"      ),
            instrument("ACOUSTIC"   , "guitar"    ),
            instrument("ELECTRONIC" , "guitar"    ),
            instrument("ACOUSTIC"   , "trumpet"   ),
            instrument("ACOUSTIC"   , "drumkit"   )
        ).stream()
            .forEach(genreOfCode("jazz")::addCharacteristicInstruments);

        Arrays.asList(
            instrument("ACOUSTIC"   , "piano"     ),
            instrument("ELECTRONIC" , "keyboard"  ),
            instrument("ACOUSTIC"   , "accordion" ),
            instrument("ACOUSTIC"   , "violin"    ),
            instrument("ACOUSTIC"   , "viola"     ),
            instrument("ACOUSTIC"   , "bass"      ),
            instrument("ACOUSTIC"   , "guitar"    ),
            instrument("ELECTRONIC" , "guitar"    ),
            instrument("ACOUSTIC"   , "voice"     )
        ).stream()
            .forEach(genreOfCode("folk")::addCharacteristicInstruments);

        Arrays.asList(
            instrument("ACOUSTIC"   , "bass"      ),
            instrument("ACOUSTIC"   , "guitar"    ),
            instrument("ELECTRONIC" , "guitar"    ),
            instrument("ACOUSTIC"   , "voice"     )
        ).stream()
            .forEach(genreOfCode("pop")::addCharacteristicInstruments);

        Arrays.asList(
            instrument("ACOUSTIC"   , "voice" )
        ).stream()
            .forEach(genreOfCode("a capella")::addCharacteristicInstruments);

        Arrays.asList(
            instrument("ACOUSTIC"   , "guitar" ),
            instrument("ACOUSTIC"   , "bass" ),
            instrument("ACOUSTIC"   , "voice" ),
            instrument("ELECTRONIC" , "keyboard"  ),
            instrument("ACOUSTIC"   , "trumpet"   ),
            instrument("ACOUSTIC"   , "drumkit"   )
        ).stream()
            .forEach(genreOfCode("ska/raggae")::addCharacteristicInstruments);
    }

    private Instrument instrument(String category, String name) {
        return instrumentRepository.findByCategoryAndName(category, name);
    }

    private Genre genreOfCode(String code) {
        return genreRepository.findByCode(code);
    }

    private Instrument instrument(String category, String kind, String name, float weight, float volume, float price, float loudness, int requiredHoursOfPracticePerWeek) {
        Instrument instrument = new Instrument();
        instrument.setName(name);
        instrument.setCategory(category);
        instrument.setKind(kind);
        instrument.setAverageWeight(weight);
        instrument.setAverageVolume(volume);
        instrument.setAverageLowEndPrice(price);
        instrument.setLoudness(loudness);
        instrument.setRequiredHoursOfPracticePerWeek(requiredHoursOfPracticePerWeek);
        return instrument;
    }

    private Genre genre(String code, int popularitySkpikeStartingYear, int popularitySkpikeEndingYear) {
        Genre genre = new Genre();
        genre.setCode(code);
        genre.setGoldenAgeStartingYear(popularitySkpikeStartingYear);
        genre.setGoldenAgeEndingYear(popularitySkpikeEndingYear);
        return genre;
    }
}
