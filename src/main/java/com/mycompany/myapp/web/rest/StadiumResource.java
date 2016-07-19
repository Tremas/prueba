package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Stadium;
import com.mycompany.myapp.repository.StadiumRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Stadium.
 */
@RestController
@RequestMapping("/api")
public class StadiumResource {

    private final Logger log = LoggerFactory.getLogger(StadiumResource.class);
        
    @Inject
    private StadiumRepository stadiumRepository;
    
    /**
     * POST  /stadiums -> Create a new stadium.
     */
    @RequestMapping(value = "/stadiums",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stadium> createStadium(@RequestBody Stadium stadium) throws URISyntaxException {
        log.debug("REST request to save Stadium : {}", stadium);
        if (stadium.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stadium", "idexists", "A new stadium cannot already have an ID")).body(null);
        }
        Stadium result = stadiumRepository.save(stadium);
        return ResponseEntity.created(new URI("/api/stadiums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stadium", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stadiums -> Updates an existing stadium.
     */
    @RequestMapping(value = "/stadiums",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stadium> updateStadium(@RequestBody Stadium stadium) throws URISyntaxException {
        log.debug("REST request to update Stadium : {}", stadium);
        if (stadium.getId() == null) {
            return createStadium(stadium);
        }
        Stadium result = stadiumRepository.save(stadium);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stadium", stadium.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stadiums -> get all the stadiums.
     */
    @RequestMapping(value = "/stadiums",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Stadium>> getAllStadiums(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("team-is-null".equals(filter)) {
            log.debug("REST request to get all Stadiums where team is null");
            return new ResponseEntity<>(StreamSupport
                .stream(stadiumRepository.findAll().spliterator(), false)
                .filter(stadium -> stadium.getTeam() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Stadiums");
        Page<Stadium> page = stadiumRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stadiums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stadiums/:id -> get the "id" stadium.
     */
    @RequestMapping(value = "/stadiums/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stadium> getStadium(@PathVariable Long id) {
        log.debug("REST request to get Stadium : {}", id);
        Stadium stadium = stadiumRepository.findOne(id);
        return Optional.ofNullable(stadium)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stadiums/:id -> delete the "id" stadium.
     */
    @RequestMapping(value = "/stadiums/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStadium(@PathVariable Long id) {
        log.debug("REST request to delete Stadium : {}", id);
        stadiumRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stadium", id.toString())).build();
    }
}
