package com.mbenabda.techwatch.testES.web.rest;

import com.mbenabda.techwatch.testES.TestEsApp;

import com.mbenabda.techwatch.testES.domain.Artist;
import com.mbenabda.techwatch.testES.repository.ArtistRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ArtistResource REST controller.
 *
 * @see ArtistResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = TestEsApp.class)

public class ArtistResourceIntTest {
    private static final String DEFAULT_STAGE_NAME = "AAAAA";
    private static final String UPDATED_STAGE_NAME = "BBBBB";

    @Inject
    private ArtistRepository artistRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restArtistMockMvc;

    private Artist artist;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArtistResource artistResource = new ArtistResource();
        ReflectionTestUtils.setField(artistResource, "artistRepository", artistRepository);
        this.restArtistMockMvc = MockMvcBuilders.standaloneSetup(artistResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artist createEntity(EntityManager em) {
        Artist artist = new Artist()
                .stageName(DEFAULT_STAGE_NAME);
        return artist;
    }

    @Before
    public void initTest() {
        artist = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtist() throws Exception {
        int databaseSizeBeforeCreate = artistRepository.findAll().size();

        // Create the Artist

        restArtistMockMvc.perform(post("/api/artists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isCreated());

        // Validate the Artist in the database
        List<Artist> artists = artistRepository.findAll();
        assertThat(artists).hasSize(databaseSizeBeforeCreate + 1);
        Artist testArtist = artists.get(artists.size() - 1);
        assertThat(testArtist.getStageName()).isEqualTo(DEFAULT_STAGE_NAME);
    }

    @Test
    @Transactional
    public void checkStageNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = artistRepository.findAll().size();
        // set the field null
        artist.setStageName(null);

        // Create the Artist, which fails.

        restArtistMockMvc.perform(post("/api/artists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artist)))
                .andExpect(status().isBadRequest());

        List<Artist> artists = artistRepository.findAll();
        assertThat(artists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArtists() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get all the artists
        restArtistMockMvc.perform(get("/api/artists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(artist.getId().intValue())))
                .andExpect(jsonPath("$.[*].stageName").value(hasItem(DEFAULT_STAGE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);

        // Get the artist
        restArtistMockMvc.perform(get("/api/artists/{id}", artist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artist.getId().intValue()))
            .andExpect(jsonPath("$.stageName").value(DEFAULT_STAGE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtist() throws Exception {
        // Get the artist
        restArtistMockMvc.perform(get("/api/artists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);
        int databaseSizeBeforeUpdate = artistRepository.findAll().size();

        // Update the artist
        Artist updatedArtist = artistRepository.findOne(artist.getId());
        updatedArtist
                .stageName(UPDATED_STAGE_NAME);

        restArtistMockMvc.perform(put("/api/artists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedArtist)))
                .andExpect(status().isOk());

        // Validate the Artist in the database
        List<Artist> artists = artistRepository.findAll();
        assertThat(artists).hasSize(databaseSizeBeforeUpdate);
        Artist testArtist = artists.get(artists.size() - 1);
        assertThat(testArtist.getStageName()).isEqualTo(UPDATED_STAGE_NAME);
    }

    @Test
    @Transactional
    public void deleteArtist() throws Exception {
        // Initialize the database
        artistRepository.saveAndFlush(artist);
        int databaseSizeBeforeDelete = artistRepository.findAll().size();

        // Get the artist
        restArtistMockMvc.perform(delete("/api/artists/{id}", artist.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Artist> artists = artistRepository.findAll();
        assertThat(artists).hasSize(databaseSizeBeforeDelete - 1);
    }
}
