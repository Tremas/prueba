package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Season;
import com.mycompany.myapp.repository.SeasonRepository;

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
 * Test class for the SeasonResource REST controller.
 *
 * @see SeasonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SeasonResourceIntTest {


    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    @Inject
    private SeasonRepository seasonRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSeasonMockMvc;

    private Season season;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SeasonResource seasonResource = new SeasonResource();
        ReflectionTestUtils.setField(seasonResource, "seasonRepository", seasonRepository);
        this.restSeasonMockMvc = MockMvcBuilders.standaloneSetup(seasonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        season = new Season();
        season.setYear(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void createSeason() throws Exception {
        int databaseSizeBeforeCreate = seasonRepository.findAll().size();

        // Create the Season

        restSeasonMockMvc.perform(post("/api/seasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(season)))
                .andExpect(status().isCreated());

        // Validate the Season in the database
        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeCreate + 1);
        Season testSeason = seasons.get(seasons.size() - 1);
        assertThat(testSeason.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void getAllSeasons() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasons
        restSeasonMockMvc.perform(get("/api/seasons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(season.getId().intValue())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    public void getSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get the season
        restSeasonMockMvc.perform(get("/api/seasons/{id}", season.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(season.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    public void getNonExistingSeason() throws Exception {
        // Get the season
        restSeasonMockMvc.perform(get("/api/seasons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

		int databaseSizeBeforeUpdate = seasonRepository.findAll().size();

        // Update the season
        season.setYear(UPDATED_YEAR);

        restSeasonMockMvc.perform(put("/api/seasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(season)))
                .andExpect(status().isOk());

        // Validate the Season in the database
        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeUpdate);
        Season testSeason = seasons.get(seasons.size() - 1);
        assertThat(testSeason.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void deleteSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

		int databaseSizeBeforeDelete = seasonRepository.findAll().size();

        // Get the season
        restSeasonMockMvc.perform(delete("/api/seasons/{id}", season.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
