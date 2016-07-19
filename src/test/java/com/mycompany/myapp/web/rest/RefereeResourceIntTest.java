package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Referee;
import com.mycompany.myapp.repository.RefereeRepository;

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
 * Test class for the RefereeResource REST controller.
 *
 * @see RefereeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RefereeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private RefereeRepository refereeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRefereeMockMvc;

    private Referee referee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RefereeResource refereeResource = new RefereeResource();
        ReflectionTestUtils.setField(refereeResource, "refereeRepository", refereeRepository);
        this.restRefereeMockMvc = MockMvcBuilders.standaloneSetup(refereeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        referee = new Referee();
        referee.setName(DEFAULT_NAME);
        referee.setBirthDate(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void createReferee() throws Exception {
        int databaseSizeBeforeCreate = refereeRepository.findAll().size();

        // Create the Referee

        restRefereeMockMvc.perform(post("/api/referees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(referee)))
                .andExpect(status().isCreated());

        // Validate the Referee in the database
        List<Referee> referees = refereeRepository.findAll();
        assertThat(referees).hasSize(databaseSizeBeforeCreate + 1);
        Referee testReferee = referees.get(referees.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReferee.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllReferees() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        // Get all the referees
        restRefereeMockMvc.perform(get("/api/referees?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(referee.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @Test
    @Transactional
    public void getReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        // Get the referee
        restRefereeMockMvc.perform(get("/api/referees/{id}", referee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(referee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReferee() throws Exception {
        // Get the referee
        restRefereeMockMvc.perform(get("/api/referees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

		int databaseSizeBeforeUpdate = refereeRepository.findAll().size();

        // Update the referee
        referee.setName(UPDATED_NAME);
        referee.setBirthDate(UPDATED_BIRTH_DATE);

        restRefereeMockMvc.perform(put("/api/referees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(referee)))
                .andExpect(status().isOk());

        // Validate the Referee in the database
        List<Referee> referees = refereeRepository.findAll();
        assertThat(referees).hasSize(databaseSizeBeforeUpdate);
        Referee testReferee = referees.get(referees.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReferee.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void deleteReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

		int databaseSizeBeforeDelete = refereeRepository.findAll().size();

        // Get the referee
        restRefereeMockMvc.perform(delete("/api/referees/{id}", referee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Referee> referees = refereeRepository.findAll();
        assertThat(referees).hasSize(databaseSizeBeforeDelete - 1);
    }
}
