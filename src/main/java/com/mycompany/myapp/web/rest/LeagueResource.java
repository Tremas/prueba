package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.League;
import com.mycompany.myapp.repository.LeagueRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing League.
 */
@RestController
@RequestMapping("/api")
public class LeagueResource {

    private final Logger log = LoggerFactory.getLogger(LeagueResource.class);
        
    @Inject
    private LeagueRepository leagueRepository;
    
    /**
     * POST  /leagues -> Create a new league.
     */
    @RequestMapping(value = "/leagues",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<League> createLeague(@RequestBody League league) throws URISyntaxException {
        log.debug("REST request to save League : {}", league);
        if (league.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("league", "idexists", "A new league cannot already have an ID")).body(null);
        }
        League result = leagueRepository.save(league);
        return ResponseEntity.created(new URI("/api/leagues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("league", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leagues -> Updates an existing league.
     */
    @RequestMapping(value = "/leagues",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<League> updateLeague(@RequestBody League league) throws URISyntaxException {
        log.debug("REST request to update League : {}", league);
        if (league.getId() == null) {
            return createLeague(league);
        }
        League result = leagueRepository.save(league);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("league", league.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leagues -> get all the leagues.
     */
    @RequestMapping(value = "/leagues",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<League>> getAllLeagues(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Leagues");
        Page<League> page = leagueRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leagues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leagues/:id -> get the "id" league.
     */
    @RequestMapping(value = "/leagues/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<League> getLeague(@PathVariable Long id) {
        log.debug("REST request to get League : {}", id);
        League league = leagueRepository.findOne(id);
        return Optional.ofNullable(league)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /leagues/:id -> delete the "id" league.
     */
    @RequestMapping(value = "/leagues/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLeague(@PathVariable Long id) {
        log.debug("REST request to delete League : {}", id);
        leagueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("league", id.toString())).build();
    }
}
