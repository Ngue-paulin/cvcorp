package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.service.CompetenceService;
import com.cv.cvcorp.web.rest.errors.BadRequestAlertException;
import com.cv.cvcorp.service.dto.CompetenceDTO;
import com.cv.cvcorp.service.dto.CompetenceCriteria;
import com.cv.cvcorp.service.CompetenceQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cv.cvcorp.domain.Competence}.
 */
@RestController
@RequestMapping("/api")
public class CompetenceResource {

    private final Logger log = LoggerFactory.getLogger(CompetenceResource.class);

    private static final String ENTITY_NAME = "cvcorpCompetence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompetenceService competenceService;

    private final CompetenceQueryService competenceQueryService;

    public CompetenceResource(CompetenceService competenceService, CompetenceQueryService competenceQueryService) {
        this.competenceService = competenceService;
        this.competenceQueryService = competenceQueryService;
    }

    /**
     * {@code POST  /competences} : Create a new competence.
     *
     * @param competenceDTO the competenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new competenceDTO, or with status {@code 400 (Bad Request)} if the competence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/competences")
    public ResponseEntity<CompetenceDTO> createCompetence(@Valid @RequestBody CompetenceDTO competenceDTO) throws URISyntaxException {
        log.debug("REST request to save Competence : {}", competenceDTO);
        if (competenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new competence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompetenceDTO result = competenceService.save(competenceDTO);
        return ResponseEntity.created(new URI("/api/competences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /competences} : Updates an existing competence.
     *
     * @param competenceDTO the competenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competenceDTO,
     * or with status {@code 400 (Bad Request)} if the competenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the competenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/competences")
    public ResponseEntity<CompetenceDTO> updateCompetence(@Valid @RequestBody CompetenceDTO competenceDTO) throws URISyntaxException {
        log.debug("REST request to update Competence : {}", competenceDTO);
        if (competenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompetenceDTO result = competenceService.save(competenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, competenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /competences} : get all the competences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of competences in body.
     */
    @GetMapping("/competences")
    public ResponseEntity<List<CompetenceDTO>> getAllCompetences(CompetenceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Competences by criteria: {}", criteria);
        Page<CompetenceDTO> page = competenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /competences/count} : count all the competences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/competences/count")
    public ResponseEntity<Long> countCompetences(CompetenceCriteria criteria) {
        log.debug("REST request to count Competences by criteria: {}", criteria);
        return ResponseEntity.ok().body(competenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /competences/:id} : get the "id" competence.
     *
     * @param id the id of the competenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the competenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/competences/{id}")
    public ResponseEntity<CompetenceDTO> getCompetence(@PathVariable Long id) {
        log.debug("REST request to get Competence : {}", id);
        Optional<CompetenceDTO> competenceDTO = competenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(competenceDTO);
    }

    /**
     * {@code DELETE  /competences/:id} : delete the "id" competence.
     *
     * @param id the id of the competenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/competences/{id}")
    public ResponseEntity<Void> deleteCompetence(@PathVariable Long id) {
        log.debug("REST request to delete Competence : {}", id);
        competenceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/competences?query=:query} : search for the competence corresponding
     * to the query.
     *
     * @param query the query of the competence search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/competences")
    public ResponseEntity<List<CompetenceDTO>> searchCompetences(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Competences for query {}", query);
        Page<CompetenceDTO> page = competenceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
