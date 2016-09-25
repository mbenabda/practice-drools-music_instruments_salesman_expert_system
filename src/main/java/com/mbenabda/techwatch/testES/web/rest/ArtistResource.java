package com.mbenabda.techwatch.testES.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mbenabda.techwatch.testES.domain.Artist;

import com.mbenabda.techwatch.testES.repository.ArtistRepository;
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
 * REST controller for managing Artist.
 */
@RestController
@RequestMapping("/api")
public class ArtistResource {

    private final Logger log = LoggerFactory.getLogger(ArtistResource.class);
        
    @Inject
    private ArtistRepository artistRepository;

    /**
     * POST  /artists : Create a new artist.
     *
     * @param artist the artist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new artist, or with status 400 (Bad Request) if the artist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/artists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Artist> createArtist(@Valid @RequestBody Artist artist) throws URISyntaxException {
        log.debug("REST request to save Artist : {}", artist);
        if (artist.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("artist", "idexists", "A new artist cannot already have an ID")).body(null);
        }
        Artist result = artistRepository.save(artist);
        return ResponseEntity.created(new URI("/api/artists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("artist", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artists : Updates an existing artist.
     *
     * @param artist the artist to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated artist,
     * or with status 400 (Bad Request) if the artist is not valid,
     * or with status 500 (Internal Server Error) if the artist couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/artists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Artist> updateArtist(@Valid @RequestBody Artist artist) throws URISyntaxException {
        log.debug("REST request to update Artist : {}", artist);
        if (artist.getId() == null) {
            return createArtist(artist);
        }
        Artist result = artistRepository.save(artist);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("artist", artist.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artists : get all the artists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of artists in body
     */
    @RequestMapping(value = "/artists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Artist> getAllArtists() {
        log.debug("REST request to get all Artists");
        List<Artist> artists = artistRepository.findAll();
        return artists;
    }

    /**
     * GET  /artists/:id : get the "id" artist.
     *
     * @param id the id of the artist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the artist, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/artists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Artist> getArtist(@PathVariable Long id) {
        log.debug("REST request to get Artist : {}", id);
        Artist artist = artistRepository.findOne(id);
        return Optional.ofNullable(artist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /artists/:id : delete the "id" artist.
     *
     * @param id the id of the artist to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/artists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        log.debug("REST request to delete Artist : {}", id);
        artistRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("artist", id.toString())).build();
    }

}
