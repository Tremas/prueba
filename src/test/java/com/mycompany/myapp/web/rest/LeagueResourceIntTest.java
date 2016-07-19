package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.League;
import com.mycompany.myapp.repository.LeagueRepository;

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
 * Test class for the LeagueResource REST controller.
 *
 * @see LeagueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LeagueResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private LeagueRepository leagueRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLeagueMockMvc;

    private League league;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LeagueResource leagueResource = new LeagueResource();
        ReflectionTestUtils.setField(leagueResource, "leagueRepository", leagueRepository);
        this.restLeagueMockMvc = MockMvcBuilders.standaloneSetup(leagueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        league = new League();
        league.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createLeague() throws Exception {
        int databaseSizeBeforeCreate = leagueRepository.findAll().size();

        // Create the League

        restLeagueMockMvc.perform(post("/api/leagues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(league)))
                .andExpect(status().isCreated());

        // Validate the League in the database
        List<League> leagues = leagueRepository.findAll();
        assertThat(leagues).hasSize(databaseSizeBeforeCreate + 1);
        League testLeague = leagues.get(leagues.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllLeagues() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get all the leagues
        restLeagueMockMvc.perform(get("/api/leagues?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(league.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getLeague() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get the league
        restLeagueMockMvc.perform(get("/api/leagues/{id}", league.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(league.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLeague() throws Exception {
        // Get the league
        restLeagueMockMvc.perform(get("/api/leagues/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeague() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

		int databaseSizeBeforeUpdate = leagueRepository.findAll().size();

        // Update the league
        league.setName(UPDATED_NAME);

        restLeagueMockMvc.perform(put("/api/leagues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(league)))
                .andExpect(status().isOk());

        // Validate the League in the database
        List<League> leagues = leagueRepository.findAll();
        assertThat(leagues).hasSize(databaseSizeBeforeUpdate);
        League testLeague = leagues.get(leagues.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteLeague() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

		int databaseSizeBeforeDelete = leagueRepository.findAll().size();

        // Get the league
        restLeagueMockMvc.perform(delete("/api/leagues/{id}", league.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<League> leagues = leagueRepository.findAll();
        assertThat(leagues).hasSize(databaseSizeBeforeDelete - 1);
    }
}
