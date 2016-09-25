package com.mbenabda.techwatch.testES.web.rest;

import com.mbenabda.techwatch.testES.TestEsApp;

import com.mbenabda.techwatch.testES.domain.Genre;
import com.mbenabda.techwatch.testES.repository.GenreRepository;

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
 * Test class for the GenreResource REST controller.
 *
 * @see GenreResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = TestEsApp.class)

public class GenreResourceIntTest {
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final Integer DEFAULT_GOLDEN_AGE_STARTING_YEAR = 1;
    private static final Integer UPDATED_GOLDEN_AGE_STARTING_YEAR = 2;

    private static final Integer DEFAULT_GOLDEN_AGE_ENDING_YEAR = 1;
    private static final Integer UPDATED_GOLDEN_AGE_ENDING_YEAR = 2;

    @Inject
    private GenreRepository genreRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGenreMockMvc;

    private Genre genre;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenreResource genreResource = new GenreResource();
        ReflectionTestUtils.setField(genreResource, "genreRepository", genreRepository);
        this.restGenreMockMvc = MockMvcBuilders.standaloneSetup(genreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genre createEntity(EntityManager em) {
        Genre genre = new Genre()
                .code(DEFAULT_CODE)
                .goldenAgeStartingYear(DEFAULT_GOLDEN_AGE_STARTING_YEAR)
                .goldenAgeEndingYear(DEFAULT_GOLDEN_AGE_ENDING_YEAR);
        return genre;
    }

    @Before
    public void initTest() {
        genre = createEntity(em);
    }

    @Test
    @Transactional
    public void createGenre() throws Exception {
        int databaseSizeBeforeCreate = genreRepository.findAll().size();

        // Create the Genre

        restGenreMockMvc.perform(post("/api/genres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genre)))
                .andExpect(status().isCreated());

        // Validate the Genre in the database
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(databaseSizeBeforeCreate + 1);
        Genre testGenre = genres.get(genres.size() - 1);
        assertThat(testGenre.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testGenre.getGoldenAgeStartingYear()).isEqualTo(DEFAULT_GOLDEN_AGE_STARTING_YEAR);
        assertThat(testGenre.getGoldenAgeEndingYear()).isEqualTo(DEFAULT_GOLDEN_AGE_ENDING_YEAR);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = genreRepository.findAll().size();
        // set the field null
        genre.setCode(null);

        // Create the Genre, which fails.

        restGenreMockMvc.perform(post("/api/genres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(genre)))
                .andExpect(status().isBadRequest());

        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGenres() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genres
        restGenreMockMvc.perform(get("/api/genres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(genre.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].goldenAgeStartingYear").value(hasItem(DEFAULT_GOLDEN_AGE_STARTING_YEAR)))
                .andExpect(jsonPath("$.[*].goldenAgeEndingYear").value(hasItem(DEFAULT_GOLDEN_AGE_ENDING_YEAR)));
    }

    @Test
    @Transactional
    public void getGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get the genre
        restGenreMockMvc.perform(get("/api/genres/{id}", genre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(genre.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.goldenAgeStartingYear").value(DEFAULT_GOLDEN_AGE_STARTING_YEAR))
            .andExpect(jsonPath("$.goldenAgeEndingYear").value(DEFAULT_GOLDEN_AGE_ENDING_YEAR));
    }

    @Test
    @Transactional
    public void getNonExistingGenre() throws Exception {
        // Get the genre
        restGenreMockMvc.perform(get("/api/genres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre
        Genre updatedGenre = genreRepository.findOne(genre.getId());
        updatedGenre
                .code(UPDATED_CODE)
                .goldenAgeStartingYear(UPDATED_GOLDEN_AGE_STARTING_YEAR)
                .goldenAgeEndingYear(UPDATED_GOLDEN_AGE_ENDING_YEAR);

        restGenreMockMvc.perform(put("/api/genres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGenre)))
                .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genres.get(genres.size() - 1);
        assertThat(testGenre.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testGenre.getGoldenAgeStartingYear()).isEqualTo(UPDATED_GOLDEN_AGE_STARTING_YEAR);
        assertThat(testGenre.getGoldenAgeEndingYear()).isEqualTo(UPDATED_GOLDEN_AGE_ENDING_YEAR);
    }

    @Test
    @Transactional
    public void deleteGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);
        int databaseSizeBeforeDelete = genreRepository.findAll().size();

        // Get the genre
        restGenreMockMvc.perform(delete("/api/genres/{id}", genre.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(databaseSizeBeforeDelete - 1);
    }
}
