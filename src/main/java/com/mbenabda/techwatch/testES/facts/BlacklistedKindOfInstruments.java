package com.mbenabda.techwatch.testES.facts;

import java.util.Objects;

public class BlacklistedKindOfInstruments {
    private final String kindCode;

    public BlacklistedKindOfInstruments(String code) {
        this.kindCode = code;
    }

    public String getKindCode() {
        return kindCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlacklistedKindOfInstruments that = (BlacklistedKindOfInstruments) o;
        return Objects.equals(kindCode, that.kindCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kindCode);
    }
}
