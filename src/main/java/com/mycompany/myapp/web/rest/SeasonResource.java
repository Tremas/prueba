package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Season;
import com.mycompany.myapp.repository.SeasonRepository;
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
 * REST controller for managing Season.
 */
@RestController
@RequestMapping("/api")
public class SeasonResource {

    private final Logger log = LoggerFactory.getLogger(SeasonResource.class);
        
    @Inject
    private SeasonRepository seasonRepository;
    
    /**
     * POST  /seasons -> Create a new season.
     */
    @RequestMapping(value = "/seasons",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Season> createSeason(@RequestBody Season season) throws URISyntaxException {
        log.debug("REST request to save Season : {}", season);
        if (season.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("season", "idexists", "A new season cannot already have an ID")).body(null);
        }
        Season result = seasonRepository.save(season);
        return ResponseEntity.created(new URI("/api/seasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("season", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /seasons -> Updates an existing season.
     */
    @RequestMapping(value = "/seasons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Season> updateSeason(@RequestBody Season season) throws URISyntaxException {
        log.debug("REST request to update Season : {}", season);
        if (season.getId() == null) {
            return createSeason(season);
        }
        Season result = seasonRepository.save(season);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("season", season.getId().toString()))
            .body(result);
    }

    /**
     * GET  /seasons -> get all the seasons.
     */
    @RequestMapping(value = "/seasons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Season>> getAllSeasons(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Seasons");
        Page<Season> page = seasonRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/seasons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /seasons/:id -> get the "id" season.
     */
    @RequestMapping(value = "/seasons/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Season> getSeason(@PathVariable Long id) {
        log.debug("REST request to get Season : {}", id);
        Season season = seasonRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(season)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /seasons/:id -> delete the "id" season.
     */
    @RequestMapping(value = "/seasons/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSeason(@PathVariable Long id) {
        log.debug("REST request to delete Season : {}", id);
        seasonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("season", id.toString())).build();
    }
}
