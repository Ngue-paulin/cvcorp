package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.service.CvcorpService;
import com.cv.cvcorp.web.rest.errors.BadRequestAlertException;
import com.cv.cvcorp.service.dto.CvcorpDTO;
import com.cv.cvcorp.service.dto.CvcorpCriteria;
import com.cv.cvcorp.service.CvcorpQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cv.cvcorp.domain.Cvcorp}.
 */
@RestController
@RequestMapping("/api")
public class CvcorpResource {

    private final Logger log = LoggerFactory.getLogger(CvcorpResource.class);

    private static final String ENTITY_NAME = "cvcorpCvcorp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CvcorpService cvcorpService;

    private final CvcorpQueryService cvcorpQueryService;

    public CvcorpResource(CvcorpService cvcorpService, CvcorpQueryService cvcorpQueryService) {
        this.cvcorpService = cvcorpService;
        this.cvcorpQueryService = cvcorpQueryService;
    }

    /**
     * {@code POST  /cvcorps} : Create a new cvcorp.
     *
     * @param cvcorpDTO the cvcorpDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cvcorpDTO, or with status {@code 400 (Bad Request)} if the cvcorp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cvcorps")
    public ResponseEntity<CvcorpDTO> createCvcorp(@RequestBody CvcorpDTO cvcorpDTO) throws URISyntaxException {
        log.debug("REST request to save Cvcorp : {}", cvcorpDTO);
        if (cvcorpDTO.getId() != null) {
            throw new BadRequestAlertException("A new cvcorp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CvcorpDTO result = cvcorpService.save(cvcorpDTO);
        return ResponseEntity.created(new URI("/api/cvcorps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cvcorps} : Updates an existing cvcorp.
     *
     * @param cvcorpDTO the cvcorpDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cvcorpDTO,
     * or with status {@code 400 (Bad Request)} if the cvcorpDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cvcorpDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cvcorps")
    public ResponseEntity<CvcorpDTO> updateCvcorp(@RequestBody CvcorpDTO cvcorpDTO) throws URISyntaxException {
        log.debug("REST request to update Cvcorp : {}", cvcorpDTO);
        if (cvcorpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CvcorpDTO result = cvcorpService.save(cvcorpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cvcorpDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cvcorps} : get all the cvcorps.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cvcorps in body.
     */
    @GetMapping("/cvcorps")
    public ResponseEntity<List<CvcorpDTO>> getAllCvcorps(CvcorpCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cvcorps by criteria: {}", criteria);
        Page<CvcorpDTO> page = cvcorpQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cvcorps/count} : count all the cvcorps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cvcorps/count")
    public ResponseEntity<Long> countCvcorps(CvcorpCriteria criteria) {
        log.debug("REST request to count Cvcorps by criteria: {}", criteria);
        return ResponseEntity.ok().body(cvcorpQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cvcorps/:id} : get the "id" cvcorp.
     *
     * @param id the id of the cvcorpDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cvcorpDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cvcorps/{id}")
    public ResponseEntity<CvcorpDTO> getCvcorp(@PathVariable Long id) {
        log.debug("REST request to get Cvcorp : {}", id);
        Optional<CvcorpDTO> cvcorpDTO = cvcorpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cvcorpDTO);
    }

    /**
     * {@code DELETE  /cvcorps/:id} : delete the "id" cvcorp.
     *
     * @param id the id of the cvcorpDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cvcorps/{id}")
    public ResponseEntity<Void> deleteCvcorp(@PathVariable Long id) {
        log.debug("REST request to delete Cvcorp : {}", id);
        cvcorpService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/cvcorps?query=:query} : search for the cvcorp corresponding
     * to the query.
     *
     * @param query the query of the cvcorp search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/cvcorps")
    public ResponseEntity<List<CvcorpDTO>> searchCvcorps(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cvcorps for query {}", query);
        Page<CvcorpDTO> page = cvcorpService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
