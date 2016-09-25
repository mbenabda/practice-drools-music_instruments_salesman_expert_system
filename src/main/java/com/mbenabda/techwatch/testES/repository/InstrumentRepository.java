package com.mbenabda.techwatch.testES.repository;

import com.mbenabda.techwatch.testES.domain.Instrument;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Instrument entity.
 */
@SuppressWarnings("unused")
public interface InstrumentRepository extends JpaRepository<Instrument,Long> {

    Instrument findByCategoryAndName(String category, String name);
}
