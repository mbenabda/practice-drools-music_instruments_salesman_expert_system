package com.mbenabda.techwatch.testES.repository;

import com.mbenabda.techwatch.testES.domain.Genre;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Genre entity.
 */
@SuppressWarnings("unused")
public interface GenreRepository extends JpaRepository<Genre,Long> {

    Genre findByCode(String code);

    @Query("select distinct genre from Genre genre left join fetch genre.characteristicInstruments")
    List<Genre> findAllWithEagerRelationships();

    @Query("select genre from Genre genre left join fetch genre.characteristicInstruments where genre.id =:id")
    Genre findOneWithEagerRelationships(@Param("id") Long id);

}
