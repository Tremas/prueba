package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Stadium;
import com.mycompany.myapp.repository.StadiumRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StadiumResource REST controller.
 *
 * @see StadiumResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StadiumResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_MEASURE = 1;
    private static final Integer UPDATED_MEASURE = 2;

    private static final Integer DEFAULT_MAXIMUM_CAPACITY = 1;
    private static final Integer UPDATED_MAXIMUM_CAPACITY = 2;

    @Inject
    private StadiumRepository stadiumRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStadiumMockMvc;

    private Stadium stadium;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StadiumResource stadiumResource = new StadiumResource();
        ReflectionTestUtils.setField(stadiumResource, "stadiumRepository", stadiumRepository);
        this.restStadiumMockMvc = MockMvcBuilders.standaloneSetup(stadiumResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stadium = new Stadium();
        stadium.setName(DEFAULT_NAME);
        stadium.setMeasure(DEFAULT_MEASURE);
        stadium.setMaximumCapacity(DEFAULT_MAXIMUM_CAPACITY);
    }

    @Test
    @Transactional
    public void createStadium() throws Exception {
        int databaseSizeBeforeCreate = stadiumRepository.findAll().size();

        // Create the Stadium

        restStadiumMockMvc.perform(post("/api/stadiums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stadium)))
                .andExpect(status().isCreated());

        // Validate the Stadium in the database
        List<Stadium> stadiums = stadiumRepository.findAll();
        assertThat(stadiums).hasSize(databaseSizeBeforeCreate + 1);
        Stadium testStadium = stadiums.get(stadiums.size() - 1);
        assertThat(testStadium.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStadium.getMeasure()).isEqualTo(DEFAULT_MEASURE);
        assertThat(testStadium.getMaximumCapacity()).isEqualTo(DEFAULT_MAXIMUM_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllStadiums() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

        // Get all the stadiums
        restStadiumMockMvc.perform(get("/api/stadiums?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stadium.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].measure").value(hasItem(DEFAULT_MEASURE)))
                .andExpect(jsonPath("$.[*].maximumCapacity").value(hasItem(DEFAULT_MAXIMUM_CAPACITY)));
    }

    @Test
    @Transactional
    public void getStadium() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

        // Get the stadium
        restStadiumMockMvc.perform(get("/api/stadiums/{id}", stadium.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stadium.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.measure").value(DEFAULT_MEASURE))
            .andExpect(jsonPath("$.maximumCapacity").value(DEFAULT_MAXIMUM_CAPACITY));
    }

    @Test
    @Transactional
    public void getNonExistingStadium() throws Exception {
        // Get the stadium
        restStadiumMockMvc.perform(get("/api/stadiums/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStadium() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

		int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();

        // Update the stadium
        stadium.setName(UPDATED_NAME);
        stadium.setMeasure(UPDATED_MEASURE);
        stadium.setMaximumCapacity(UPDATED_MAXIMUM_CAPACITY);

        restStadiumMockMvc.perform(put("/api/stadiums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stadium)))
                .andExpect(status().isOk());

        // Validate the Stadium in the database
        List<Stadium> stadiums = stadiumRepository.findAll();
        assertThat(stadiums).hasSize(databaseSizeBeforeUpdate);
        Stadium testStadium = stadiums.get(stadiums.size() - 1);
        assertThat(testStadium.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStadium.getMeasure()).isEqualTo(UPDATED_MEASURE);
        assertThat(testStadium.getMaximumCapacity()).isEqualTo(UPDATED_MAXIMUM_CAPACITY);
    }

    @Test
    @Transactional
    public void deleteStadium() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

		int databaseSizeBeforeDelete = stadiumRepository.findAll().size();

        // Get the stadium
        restStadiumMockMvc.perform(delete("/api/stadiums/{id}", stadium.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Stadium> stadiums = stadiumRepository.findAll();
        assertThat(stadiums).hasSize(databaseSizeBeforeDelete - 1);
    }
}
