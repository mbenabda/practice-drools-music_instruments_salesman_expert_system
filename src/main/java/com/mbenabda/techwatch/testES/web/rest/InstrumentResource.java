package com.mbenabda.techwatch.testES.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbenabda.techwatch.testES.domain.Instrument;

import com.mbenabda.techwatch.testES.repository.InstrumentRepository;
import com.mbenabda.techwatch.testES.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Instrument.
 */
@RestController
@RequestMapping("/api")
public class InstrumentResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentResource.class);
        
    @Inject
    private InstrumentRepository instrumentRepository;

    /**
     * POST  /instruments : Create a new instrument.
     *
     * @param instrument the instrument to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instrument, or with status 400 (Bad Request) if the instrument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instruments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Instrument> createInstrument(@Valid @RequestBody Instrument instrument) throws URISyntaxException {
        log.debug("REST request to save Instrument : {}", instrument);
        if (instrument.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instrument", "idexists", "A new instrument cannot already have an ID")).body(null);
        }
        Instrument result = instrumentRepository.save(instrument);
        return ResponseEntity.created(new URI("/api/instruments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instrument", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instruments : Updates an existing instrument.
     *
     * @param instrument the instrument to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instrument,
     * or with status 400 (Bad Request) if the instrument is not valid,
     * or with status 500 (Internal Server Error) if the instrument couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instruments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Instrument> updateInstrument(@Valid @RequestBody Instrument instrument) throws URISyntaxException {
        log.debug("REST request to update Instrument : {}", instrument);
        if (instrument.getId() == null) {
            return createInstrument(instrument);
        }
        Instrument result = instrumentRepository.save(instrument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instrument", instrument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instruments : get all the instruments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instruments in body
     */
    @RequestMapping(value = "/instruments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Instrument> getAllInstruments() {
        log.debug("REST request to get all Instruments");
        List<Instrument> instruments = instrumentRepository.findAll();
        return instruments;
    }

    /**
     * GET  /instruments/:id : get the "id" instrument.
     *
     * @param id the id of the instrument to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instrument, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instruments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Instrument> getInstrument(@PathVariable Long id) {
        log.debug("REST request to get Instrument : {}", id);
        Instrument instrument = instrumentRepository.findOne(id);
        return Optional.ofNullable(instrument)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instruments/:id : delete the "id" instrument.
     *
     * @param id the id of the instrument to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instruments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
        log.debug("REST request to delete Instrument : {}", id);
        instrumentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instrument", id.toString())).build();
    }

}
