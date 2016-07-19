package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Stadistics;
import com.mycompany.myapp.repository.StadisticsRepository;
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
 * REST controller for managing Stadistics.
 */
@RestController
@RequestMapping("/api")
public class StadisticsResource {

    private final Logger log = LoggerFactory.getLogger(StadisticsResource.class);
        
    @Inject
    private StadisticsRepository stadisticsRepository;
    
    /**
     * POST  /stadisticss -> Create a new stadistics.
     */
    @RequestMapping(value = "/stadisticss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stadistics> createStadistics(@RequestBody Stadistics stadistics) throws URISyntaxException {
        log.debug("REST request to save Stadistics : {}", stadistics);
        if (stadistics.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stadistics", "idexists", "A new stadistics cannot already have an ID")).body(null);
        }
        Stadistics result = stadisticsRepository.save(stadistics);
        return ResponseEntity.created(new URI("/api/stadisticss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stadistics", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stadisticss -> Updates an existing stadistics.
     */
    @RequestMapping(value = "/stadisticss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stadistics> updateStadistics(@RequestBody Stadistics stadistics) throws URISyntaxException {
        log.debug("REST request to update Stadistics : {}", stadistics);
        if (stadistics.getId() == null) {
            return createStadistics(stadistics);
        }
        Stadistics result = stadisticsRepository.save(stadistics);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stadistics", stadistics.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stadisticss -> get all the stadisticss.
     */
    @RequestMapping(value = "/stadisticss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Stadistics>> getAllStadisticss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Stadisticss");
        Page<Stadistics> page = stadisticsRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stadisticss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stadisticss/:id -> get the "id" stadistics.
     */
    @RequestMapping(value = "/stadisticss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Stadistics> getStadistics(@PathVariable Long id) {
        log.debug("REST request to get Stadistics : {}", id);
        Stadistics stadistics = stadisticsRepository.findOne(id);
        return Optional.ofNullable(stadistics)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stadisticss/:id -> delete the "id" stadistics.
     */
    @RequestMapping(value = "/stadisticss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStadistics(@PathVariable Long id) {
        log.debug("REST request to delete Stadistics : {}", id);
        stadisticsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stadistics", id.toString())).build();
    }
}
