package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Stadistics;
import com.mycompany.myapp.repository.StadisticsRepository;

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
 * Test class for the StadisticsResource REST controller.
 *
 * @see StadisticsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StadisticsResourceIntTest {


    private static final Integer DEFAULT_BASKETS = 1;
    private static final Integer UPDATED_BASKETS = 2;

    private static final Integer DEFAULT_FAULTS = 1;
    private static final Integer UPDATED_FAULTS = 2;

    @Inject
    private StadisticsRepository stadisticsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStadisticsMockMvc;

    private Stadistics stadistics;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StadisticsResource stadisticsResource = new StadisticsResource();
        ReflectionTestUtils.setField(stadisticsResource, "stadisticsRepository", stadisticsRepository);
        this.restStadisticsMockMvc = MockMvcBuilders.standaloneSetup(stadisticsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stadistics = new Stadistics();
        stadistics.setBaskets(DEFAULT_BASKETS);
        stadistics.setFaults(DEFAULT_FAULTS);
    }

    @Test
    @Transactional
    public void createStadistics() throws Exception {
        int databaseSizeBeforeCreate = stadisticsRepository.findAll().size();

        // Create the Stadistics

        restStadisticsMockMvc.perform(post("/api/stadisticss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stadistics)))
                .andExpect(status().isCreated());

        // Validate the Stadistics in the database
        List<Stadistics> stadisticss = stadisticsRepository.findAll();
        assertThat(stadisticss).hasSize(databaseSizeBeforeCreate + 1);
        Stadistics testStadistics = stadisticss.get(stadisticss.size() - 1);
        assertThat(testStadistics.getBaskets()).isEqualTo(DEFAULT_BASKETS);
        assertThat(testStadistics.getFaults()).isEqualTo(DEFAULT_FAULTS);
    }

    @Test
    @Transactional
    public void getAllStadisticss() throws Exception {
        // Initialize the database
        stadisticsRepository.saveAndFlush(stadistics);

        // Get all the stadisticss
        restStadisticsMockMvc.perform(get("/api/stadisticss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stadistics.getId().intValue())))
                .andExpect(jsonPath("$.[*].baskets").value(hasItem(DEFAULT_BASKETS)))
                .andExpect(jsonPath("$.[*].faults").value(hasItem(DEFAULT_FAULTS)));
    }

    @Test
    @Transactional
    public void getStadistics() throws Exception {
        // Initialize the database
        stadisticsRepository.saveAndFlush(stadistics);

        // Get the stadistics
        restStadisticsMockMvc.perform(get("/api/stadisticss/{id}", stadistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stadistics.getId().intValue()))
            .andExpect(jsonPath("$.baskets").value(DEFAULT_BASKETS))
            .andExpect(jsonPath("$.faults").value(DEFAULT_FAULTS));
    }

    @Test
    @Transactional
    public void getNonExistingStadistics() throws Exception {
        // Get the stadistics
        restStadisticsMockMvc.perform(get("/api/stadisticss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStadistics() throws Exception {
        // Initialize the database
        stadisticsRepository.saveAndFlush(stadistics);

		int databaseSizeBeforeUpdate = stadisticsRepository.findAll().size();

        // Update the stadistics
        stadistics.setBaskets(UPDATED_BASKETS);
        stadistics.setFaults(UPDATED_FAULTS);

        restStadisticsMockMvc.perform(put("/api/stadisticss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stadistics)))
                .andExpect(status().isOk());

        // Validate the Stadistics in the database
        List<Stadistics> stadisticss = stadisticsRepository.findAll();
        assertThat(stadisticss).hasSize(databaseSizeBeforeUpdate);
        Stadistics testStadistics = stadisticss.get(stadisticss.size() - 1);
        assertThat(testStadistics.getBaskets()).isEqualTo(UPDATED_BASKETS);
        assertThat(testStadistics.getFaults()).isEqualTo(UPDATED_FAULTS);
    }

    @Test
    @Transactional
    public void deleteStadistics() throws Exception {
        // Initialize the database
        stadisticsRepository.saveAndFlush(stadistics);

		int databaseSizeBeforeDelete = stadisticsRepository.findAll().size();

        // Get the stadistics
        restStadisticsMockMvc.perform(delete("/api/stadisticss/{id}", stadistics.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Stadistics> stadisticss = stadisticsRepository.findAll();
        assertThat(stadisticss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
