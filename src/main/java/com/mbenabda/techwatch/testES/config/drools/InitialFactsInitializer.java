package com.mbenabda.techwatch.testES.config.drools;

import com.mbenabda.techwatch.testES.facts.age.YouthLimitAge;
import com.mbenabda.techwatch.testES.facts.instruments.CharacteristicInstrument;
import com.mbenabda.techwatch.testES.facts.noise.LoudnessThreshold;
import com.mbenabda.techwatch.testES.facts.storage.VolumeLimit;
import com.mbenabda.techwatch.testES.repository.GenreRepository;
import com.mbenabda.techwatch.testES.repository.InstrumentRepository;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InitialFactsInitializer {
    private static final int LIMIT_AGE = 18;

    private final GenreRepository genreRepository;
    private final KieSession session;
    private final InstrumentRepository instrumentRepository;

    @Autowired
    public InitialFactsInitializer(KieSession session, GenreRepository genreRepository, InstrumentRepository instrumentRepository) {
        this.session = session;
        this.genreRepository = genreRepository;
        this.instrumentRepository = instrumentRepository;
    }

    public void init() {
        session.insert(new YouthLimitAge(LIMIT_AGE));
        session.insert(new LoudnessThreshold(.5f));
        session.insert(new VolumeLimit(4f));

        instrumentRepository.findAll().stream().forEach(session::insert);

        genreRepository.findAll().stream()
            .forEach(genre -> {
                    session.insert(genre);

                    genre.getCharacteristicInstruments().stream()
                        .map(instrument -> new CharacteristicInstrument(genre.getId(), instrument.getId()))
                        .forEach(session::insert);

                }
            );
    }
}
