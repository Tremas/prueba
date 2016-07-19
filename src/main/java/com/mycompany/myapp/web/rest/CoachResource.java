package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Coach;
import com.mycompany.myapp.repository.CoachRepository;
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
 * REST controller for managing Coach.
 */
@RestController
@RequestMapping("/api")
public class CoachResource {

    private final Logger log = LoggerFactory.getLogger(CoachResource.class);
        
    @Inject
    private CoachRepository coachRepository;
    
    /**
     * POST  /coachs -> Create a new coach.
     */
    @RequestMapping(value = "/coachs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Coach> createCoach(@RequestBody Coach coach) throws URISyntaxException {
        log.debug("REST request to save Coach : {}", coach);
        if (coach.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("coach", "idexists", "A new coach cannot already have an ID")).body(null);
        }
        Coach result = coachRepository.save(coach);
        return ResponseEntity.created(new URI("/api/coachs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("coach", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coachs -> Updates an existing coach.
     */
    @RequestMapping(value = "/coachs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Coach> updateCoach(@RequestBody Coach coach) throws URISyntaxException {
        log.debug("REST request to update Coach : {}", coach);
        if (coach.getId() == null) {
            return createCoach(coach);
        }
        Coach result = coachRepository.save(coach);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("coach", coach.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coachs -> get all the coachs.
     */
    @RequestMapping(value = "/coachs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Coach>> getAllCoachs(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("team-is-null".equals(filter)) {
            log.debug("REST request to get all Coachs where team is null");
            return new ResponseEntity<>(StreamSupport
                .stream(coachRepository.findAll().spliterator(), false)
                .filter(coach -> coach.getTeam() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Coachs");
        Page<Coach> page = coachRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coachs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coachs/:id -> get the "id" coach.
     */
    @RequestMapping(value = "/coachs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Coach> getCoach(@PathVariable Long id) {
        log.debug("REST request to get Coach : {}", id);
        Coach coach = coachRepository.findOne(id);
        return Optional.ofNullable(coach)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coachs/:id -> delete the "id" coach.
     */
    @RequestMapping(value = "/coachs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCoach(@PathVariable Long id) {
        log.debug("REST request to delete Coach : {}", id);
        coachRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("coach", id.toString())).build();
    }
}
