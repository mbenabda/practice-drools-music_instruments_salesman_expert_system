package com.mbenabda.techwatch.testES.facts.suggestion;

import com.mbenabda.techwatch.testES.domain.Instrument;

import java.util.Objects;

public class InstrumentSuggestion {
    private final Instrument instrument;

    public InstrumentSuggestion(Instrument instrument) {
        this.instrument = instrument;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentSuggestion that = (InstrumentSuggestion) o;
        return Objects.equals(instrument, that.instrument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument);
    }
}
