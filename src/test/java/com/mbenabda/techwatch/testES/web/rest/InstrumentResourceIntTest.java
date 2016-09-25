package com.mbenabda.techwatch.testES.web.rest;

import com.mbenabda.techwatch.testES.TestEsApp;

import com.mbenabda.techwatch.testES.domain.Instrument;
import com.mbenabda.techwatch.testES.repository.InstrumentRepository;

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
 * Test class for the InstrumentResource REST controller.
 *
 * @see InstrumentResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = TestEsApp.class)

public class InstrumentResourceIntTest {
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final Float DEFAULT_AVERAGE_VOLUME = 0F;
    private static final Float UPDATED_AVERAGE_VOLUME = 1F;

    private static final Float DEFAULT_AVERAGE_WEIGHT = 0F;
    private static final Float UPDATED_AVERAGE_WEIGHT = 1F;

    private static final Float DEFAULT_AVERAGE_LOW_END_PRICE = 0F;
    private static final Float UPDATED_AVERAGE_LOW_END_PRICE = 1F;

    private static final Integer DEFAULT_REQUIRED_HOURS_OF_PRACTICE_PER_WEEK = 0;
    private static final Integer UPDATED_REQUIRED_HOURS_OF_PRACTICE_PER_WEEK = 1;

    private static final Float DEFAULT_LOUDNESS = 0F;
    private static final Float UPDATED_LOUDNESS = 1F;
    private static final String DEFAULT_CATEGORY = "AAAAA";
    private static final String UPDATED_CATEGORY = "BBBBB";
    private static final String DEFAULT_KIND = "AAAAA";
    private static final String UPDATED_KIND = "BBBBB";

    @Inject
    private InstrumentRepository instrumentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInstrumentMockMvc;

    private Instrument instrument;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstrumentResource instrumentResource = new InstrumentResource();
        ReflectionTestUtils.setField(instrumentResource, "instrumentRepository", instrumentRepository);
        this.restInstrumentMockMvc = MockMvcBuilders.standaloneSetup(instrumentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instrument createEntity(EntityManager em) {
        Instrument instrument = new Instrument()
                .code(DEFAULT_CODE)
                .averageVolume(DEFAULT_AVERAGE_VOLUME)
                .averageWeight(DEFAULT_AVERAGE_WEIGHT)
                .averageLowEndPrice(DEFAULT_AVERAGE_LOW_END_PRICE)
                .requiredHoursOfPracticePerWeek(DEFAULT_REQUIRED_HOURS_OF_PRACTICE_PER_WEEK)
                .loudness(DEFAULT_LOUDNESS)
                .category(DEFAULT_CATEGORY)
                .kind(DEFAULT_KIND);
        return instrument;
    }

    @Before
    public void initTest() {
        instrument = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstrument() throws Exception {
        int databaseSizeBeforeCreate = instrumentRepository.findAll().size();

        // Create the Instrument

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isCreated());

        // Validate the Instrument in the database
        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeCreate + 1);
        Instrument testInstrument = instruments.get(instruments.size() - 1);
        assertThat(testInstrument.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInstrument.getAverageVolume()).isEqualTo(DEFAULT_AVERAGE_VOLUME);
        assertThat(testInstrument.getAverageWeight()).isEqualTo(DEFAULT_AVERAGE_WEIGHT);
        assertThat(testInstrument.getAverageLowEndPrice()).isEqualTo(DEFAULT_AVERAGE_LOW_END_PRICE);
        assertThat(testInstrument.getRequiredHoursOfPracticePerWeek()).isEqualTo(DEFAULT_REQUIRED_HOURS_OF_PRACTICE_PER_WEEK);
        assertThat(testInstrument.getLoudness()).isEqualTo(DEFAULT_LOUDNESS);
        assertThat(testInstrument.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testInstrument.getKind()).isEqualTo(DEFAULT_KIND);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setCode(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isBadRequest());

        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAverageVolumeIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setAverageVolume(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isBadRequest());

        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAverageWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setAverageWeight(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isBadRequest());

        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAverageLowEndPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setAverageLowEndPrice(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isBadRequest());

        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequiredHoursOfPracticePerWeekIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setRequiredHoursOfPracticePerWeek(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isBadRequest());

        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoudnessIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setLoudness(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isBadRequest());

        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setCategory(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isBadRequest());

        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKindIsRequired() throws Exception {
        int databaseSizeBeforeTest = instrumentRepository.findAll().size();
        // set the field null
        instrument.setKind(null);

        // Create the Instrument, which fails.

        restInstrumentMockMvc.perform(post("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instrument)))
                .andExpect(status().isBadRequest());

        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstruments() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get all the instruments
        restInstrumentMockMvc.perform(get("/api/instruments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instrument.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].averageVolume").value(hasItem(DEFAULT_AVERAGE_VOLUME.doubleValue())))
                .andExpect(jsonPath("$.[*].averageWeight").value(hasItem(DEFAULT_AVERAGE_WEIGHT.doubleValue())))
                .andExpect(jsonPath("$.[*].averageLowEndPrice").value(hasItem(DEFAULT_AVERAGE_LOW_END_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].requiredHoursOfPracticePerWeek").value(hasItem(DEFAULT_REQUIRED_HOURS_OF_PRACTICE_PER_WEEK)))
                .andExpect(jsonPath("$.[*].loudness").value(hasItem(DEFAULT_LOUDNESS.doubleValue())))
                .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].kind").value(hasItem(DEFAULT_KIND.toString())));
    }

    @Test
    @Transactional
    public void getInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get the instrument
        restInstrumentMockMvc.perform(get("/api/instruments/{id}", instrument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(instrument.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.averageVolume").value(DEFAULT_AVERAGE_VOLUME.doubleValue()))
            .andExpect(jsonPath("$.averageWeight").value(DEFAULT_AVERAGE_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.averageLowEndPrice").value(DEFAULT_AVERAGE_LOW_END_PRICE.doubleValue()))
            .andExpect(jsonPath("$.requiredHoursOfPracticePerWeek").value(DEFAULT_REQUIRED_HOURS_OF_PRACTICE_PER_WEEK))
            .andExpect(jsonPath("$.loudness").value(DEFAULT_LOUDNESS.doubleValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.kind").value(DEFAULT_KIND.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstrument() throws Exception {
        // Get the instrument
        restInstrumentMockMvc.perform(get("/api/instruments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();

        // Update the instrument
        Instrument updatedInstrument = instrumentRepository.findOne(instrument.getId());
        updatedInstrument
                .code(UPDATED_CODE)
                .averageVolume(UPDATED_AVERAGE_VOLUME)
                .averageWeight(UPDATED_AVERAGE_WEIGHT)
                .averageLowEndPrice(UPDATED_AVERAGE_LOW_END_PRICE)
                .requiredHoursOfPracticePerWeek(UPDATED_REQUIRED_HOURS_OF_PRACTICE_PER_WEEK)
                .loudness(UPDATED_LOUDNESS)
                .category(UPDATED_CATEGORY)
                .kind(UPDATED_KIND);

        restInstrumentMockMvc.perform(put("/api/instruments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstrument)))
                .andExpect(status().isOk());

        // Validate the Instrument in the database
        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instruments.get(instruments.size() - 1);
        assertThat(testInstrument.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInstrument.getAverageVolume()).isEqualTo(UPDATED_AVERAGE_VOLUME);
        assertThat(testInstrument.getAverageWeight()).isEqualTo(UPDATED_AVERAGE_WEIGHT);
        assertThat(testInstrument.getAverageLowEndPrice()).isEqualTo(UPDATED_AVERAGE_LOW_END_PRICE);
        assertThat(testInstrument.getRequiredHoursOfPracticePerWeek()).isEqualTo(UPDATED_REQUIRED_HOURS_OF_PRACTICE_PER_WEEK);
        assertThat(testInstrument.getLoudness()).isEqualTo(UPDATED_LOUDNESS);
        assertThat(testInstrument.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testInstrument.getKind()).isEqualTo(UPDATED_KIND);
    }

    @Test
    @Transactional
    public void deleteInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);
        int databaseSizeBeforeDelete = instrumentRepository.findAll().size();

        // Get the instrument
        restInstrumentMockMvc.perform(delete("/api/instruments/{id}", instrument.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Instrument> instruments = instrumentRepository.findAll();
        assertThat(instruments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
