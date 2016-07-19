package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Player;
import com.mycompany.myapp.repository.PlayerRepository;

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
 * Test class for the PlayerResource REST controller.
 *
 * @see PlayerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PlayerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TOTAL_BASKETS = 1;
    private static final Integer UPDATED_TOTAL_BASKETS = 2;

    private static final Integer DEFAULT_TOTAL_ASSISTS = 1;
    private static final Integer UPDATED_TOTAL_ASSISTS = 2;

    private static final Integer DEFAULT_TOTAL_REBOUNDS = 1;
    private static final Integer UPDATED_TOTAL_REBOUNDS = 2;
    private static final String DEFAULT_FIELD_POSITION = "AAAAA";
    private static final String UPDATED_FIELD_POSITION = "BBBBB";

    @Inject
    private PlayerRepository playerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPlayerMockMvc;

    private Player player;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlayerResource playerResource = new PlayerResource();
        ReflectionTestUtils.setField(playerResource, "playerRepository", playerRepository);
        this.restPlayerMockMvc = MockMvcBuilders.standaloneSetup(playerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        player = new Player();
        player.setName(DEFAULT_NAME);
        player.setBirthDate(DEFAULT_BIRTH_DATE);
        player.setTotalBaskets(DEFAULT_TOTAL_BASKETS);
        player.setTotalAssists(DEFAULT_TOTAL_ASSISTS);
        player.setTotalRebounds(DEFAULT_TOTAL_REBOUNDS);
        player.setFieldPosition(DEFAULT_FIELD_POSITION);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player

        restPlayerMockMvc.perform(post("/api/players")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(player)))
                .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> players = playerRepository.findAll();
        assertThat(players).hasSize(databaseSizeBeforeCreate + 1);
        Player testPlayer = players.get(players.size() - 1);
        assertThat(testPlayer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlayer.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPlayer.getTotalBaskets()).isEqualTo(DEFAULT_TOTAL_BASKETS);
        assertThat(testPlayer.getTotalAssists()).isEqualTo(DEFAULT_TOTAL_ASSISTS);
        assertThat(testPlayer.getTotalRebounds()).isEqualTo(DEFAULT_TOTAL_REBOUNDS);
        assertThat(testPlayer.getFieldPosition()).isEqualTo(DEFAULT_FIELD_POSITION);
    }

    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the players
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
                .andExpect(jsonPath("$.[*].totalBaskets").value(hasItem(DEFAULT_TOTAL_BASKETS)))
                .andExpect(jsonPath("$.[*].totalAssists").value(hasItem(DEFAULT_TOTAL_ASSISTS)))
                .andExpect(jsonPath("$.[*].totalRebounds").value(hasItem(DEFAULT_TOTAL_REBOUNDS)))
                .andExpect(jsonPath("$.[*].fieldPosition").value(hasItem(DEFAULT_FIELD_POSITION.toString())));
    }

    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(player.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.totalBaskets").value(DEFAULT_TOTAL_BASKETS))
            .andExpect(jsonPath("$.totalAssists").value(DEFAULT_TOTAL_ASSISTS))
            .andExpect(jsonPath("$.totalRebounds").value(DEFAULT_TOTAL_REBOUNDS))
            .andExpect(jsonPath("$.fieldPosition").value(DEFAULT_FIELD_POSITION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

		int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Update the player
        player.setName(UPDATED_NAME);
        player.setBirthDate(UPDATED_BIRTH_DATE);
        player.setTotalBaskets(UPDATED_TOTAL_BASKETS);
        player.setTotalAssists(UPDATED_TOTAL_ASSISTS);
        player.setTotalRebounds(UPDATED_TOTAL_REBOUNDS);
        player.setFieldPosition(UPDATED_FIELD_POSITION);

        restPlayerMockMvc.perform(put("/api/players")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(player)))
                .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> players = playerRepository.findAll();
        assertThat(players).hasSize(databaseSizeBeforeUpdate);
        Player testPlayer = players.get(players.size() - 1);
        assertThat(testPlayer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlayer.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPlayer.getTotalBaskets()).isEqualTo(UPDATED_TOTAL_BASKETS);
        assertThat(testPlayer.getTotalAssists()).isEqualTo(UPDATED_TOTAL_ASSISTS);
        assertThat(testPlayer.getTotalRebounds()).isEqualTo(UPDATED_TOTAL_REBOUNDS);
        assertThat(testPlayer.getFieldPosition()).isEqualTo(UPDATED_FIELD_POSITION);
    }

    @Test
    @Transactional
    public void deletePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

		int databaseSizeBeforeDelete = playerRepository.findAll().size();

        // Get the player
        restPlayerMockMvc.perform(delete("/api/players/{id}", player.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Player> players = playerRepository.findAll();
        assertThat(players).hasSize(databaseSizeBeforeDelete - 1);
    }
}
