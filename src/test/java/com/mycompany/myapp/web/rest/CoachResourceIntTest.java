package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Coach;
import com.mycompany.myapp.repository.CoachRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CoachResource REST controller.
 *
 * @see CoachResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CoachResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private CoachRepository coachRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoachMockMvc;

    private Coach coach;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoachResource coachResource = new CoachResource();
        ReflectionTestUtils.setField(coachResource, "coachRepository", coachRepository);
        this.restCoachMockMvc = MockMvcBuilders.standaloneSetup(coachResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coach = new Coach();
        coach.setName(DEFAULT_NAME);
        coach.setBirthDate(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void createCoach() throws Exception {
        int databaseSizeBeforeCreate = coachRepository.findAll().size();

        // Create the Coach

        restCoachMockMvc.perform(post("/api/coachs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coach)))
                .andExpect(status().isCreated());

        // Validate the Coach in the database
        List<Coach> coachs = coachRepository.findAll();
        assertThat(coachs).hasSize(databaseSizeBeforeCreate + 1);
        Coach testCoach = coachs.get(coachs.size() - 1);
        assertThat(testCoach.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCoach.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllCoachs() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get all the coachs
        restCoachMockMvc.perform(get("/api/coachs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coach.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @Test
    @Transactional
    public void getCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get the coach
        restCoachMockMvc.perform(get("/api/coachs/{id}", coach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coach.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoach() throws Exception {
        // Get the coach
        restCoachMockMvc.perform(get("/api/coachs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

		int databaseSizeBeforeUpdate = coachRepository.findAll().size();

        // Update the coach
        coach.setName(UPDATED_NAME);
        coach.setBirthDate(UPDATED_BIRTH_DATE);

        restCoachMockMvc.perform(put("/api/coachs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coach)))
                .andExpect(status().isOk());

        // Validate the Coach in the database
        List<Coach> coachs = coachRepository.findAll();
        assertThat(coachs).hasSize(databaseSizeBeforeUpdate);
        Coach testCoach = coachs.get(coachs.size() - 1);
        assertThat(testCoach.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoach.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void deleteCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

		int databaseSizeBeforeDelete = coachRepository.findAll().size();

        // Get the coach
        restCoachMockMvc.perform(delete("/api/coachs/{id}", coach.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Coach> coachs = coachRepository.findAll();
        assertThat(coachs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
