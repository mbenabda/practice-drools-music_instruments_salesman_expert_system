package com.mbenabda.techwatch.testES.facts;

import java.util.Objects;

public class BannishCategory {
    private final String categoryCode;

    public BannishCategory(String code) {
        this.categoryCode = code;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BannishCategory that = (BannishCategory) o;
        return Objects.equals(categoryCode, that.categoryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryCode);
    }
}
