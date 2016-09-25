package com.mbenabda.techwatch.testES.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Instrument.
 */
@Entity
@Table(name = "instrument")
public class Instrument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "average_volume", nullable = false)
    private Float averageVolume;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "average_weight", nullable = false)
    private Float averageWeight;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "average_low_end_price", nullable = false)
    private Float averageLowEndPrice;

    @NotNull
    @Min(value = 0)
    @Column(name = "required_hours_of_practice_per_week", nullable = false)
    private Integer requiredHoursOfPracticePerWeek;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "loudness", nullable = false)
    private Float loudness;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @NotNull
    @Column(name = "kind", nullable = false)
    private String kind;

    @ManyToMany(mappedBy = "characteristicInstruments")
    @JsonIgnore
    private Set<Genre> genresItIsCharacteristicOfs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Instrument name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAverageVolume() {
        return averageVolume;
    }

    public Instrument averageVolume(Float averageVolume) {
        this.averageVolume = averageVolume;
        return this;
    }

    public void setAverageVolume(Float averageVolume) {
        this.averageVolume = averageVolume;
    }

    public Float getAverageWeight() {
        return averageWeight;
    }

    public Instrument averageWeight(Float averageWeight) {
        this.averageWeight = averageWeight;
        return this;
    }

    public void setAverageWeight(Float averageWeight) {
        this.averageWeight = averageWeight;
    }

    public Float getAverageLowEndPrice() {
        return averageLowEndPrice;
    }

    public Instrument averageLowEndPrice(Float averageLowEndPrice) {
        this.averageLowEndPrice = averageLowEndPrice;
        return this;
    }

    public void setAverageLowEndPrice(Float averageLowEndPrice) {
        this.averageLowEndPrice = averageLowEndPrice;
    }

    public Integer getRequiredHoursOfPracticePerWeek() {
        return requiredHoursOfPracticePerWeek;
    }

    public Instrument requiredHoursOfPracticePerWeek(Integer requiredHoursOfPracticePerWeek) {
        this.requiredHoursOfPracticePerWeek = requiredHoursOfPracticePerWeek;
        return this;
    }

    public void setRequiredHoursOfPracticePerWeek(Integer requiredHoursOfPracticePerWeek) {
        this.requiredHoursOfPracticePerWeek = requiredHoursOfPracticePerWeek;
    }

    public Float getLoudness() {
        return loudness;
    }

    public Instrument loudness(Float loudness) {
        this.loudness = loudness;
        return this;
    }

    public void setLoudness(Float loudness) {
        this.loudness = loudness;
    }

    public String getCategory() {
        return category;
    }

    public Instrument category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKind() {
        return kind;
    }

    public Instrument kind(String kind) {
        this.kind = kind;
        return this;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Set<Genre> getGenresItIsCharacteristicOfs() {
        return genresItIsCharacteristicOfs;
    }

    public Instrument genresItIsCharacteristicOfs(Set<Genre> genres) {
        this.genresItIsCharacteristicOfs = genres;
        return this;
    }

    public Instrument addGenresItIsCharacteristicOf(Genre genre) {
        genresItIsCharacteristicOfs.add(genre);
        genre.getCharacteristicInstruments().add(this);
        return this;
    }

    public Instrument removeGenresItIsCharacteristicOf(Genre genre) {
        genresItIsCharacteristicOfs.remove(genre);
        genre.getCharacteristicInstruments().remove(this);
        return this;
    }

    public void setGenresItIsCharacteristicOfs(Set<Genre> genres) {
        this.genresItIsCharacteristicOfs = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Instrument instrument = (Instrument) o;
        if(instrument.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instrument.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Instrument{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", averageVolume='" + averageVolume + "'" +
            ", averageWeight='" + averageWeight + "'" +
            ", averageLowEndPrice='" + averageLowEndPrice + "'" +
            ", requiredHoursOfPracticePerWeek='" + requiredHoursOfPracticePerWeek + "'" +
            ", loudness='" + loudness + "'" +
            ", category='" + category + "'" +
            ", kind='" + kind + "'" +
            '}';
    }
}
