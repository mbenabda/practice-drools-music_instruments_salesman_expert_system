package com.mbenabda.techwatch.testES.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Genre.
 */
@Entity
@Table(name = "genre")
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "golden_age_starting_year")
    private Integer goldenAgeStartingYear;

    @Column(name = "golden_age_ending_year")
    private Integer goldenAgeEndingYear;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Genre code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGoldenAgeStartingYear() {
        return goldenAgeStartingYear;
    }

    public Genre goldenAgeStartingYear(Integer goldenAgeStartingYear) {
        this.goldenAgeStartingYear = goldenAgeStartingYear;
        return this;
    }

    public void setGoldenAgeStartingYear(Integer goldenAgeStartingYear) {
        this.goldenAgeStartingYear = goldenAgeStartingYear;
    }

    public Integer getGoldenAgeEndingYear() {
        return goldenAgeEndingYear;
    }

    public Genre goldenAgeEndingYear(Integer goldenAgeEndingYear) {
        this.goldenAgeEndingYear = goldenAgeEndingYear;
        return this;
    }

    public void setGoldenAgeEndingYear(Integer goldenAgeEndingYear) {
        this.goldenAgeEndingYear = goldenAgeEndingYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Genre genre = (Genre) o;
        if(genre.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, genre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Genre{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", goldenAgeStartingYear='" + goldenAgeStartingYear + "'" +
            ", goldenAgeEndingYear='" + goldenAgeEndingYear + "'" +
            '}';
    }
}
