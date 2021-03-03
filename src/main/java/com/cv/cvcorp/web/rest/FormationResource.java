package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.service.FormationService;
import com.cv.cvcorp.web.rest.errors.BadRequestAlertException;
import com.cv.cvcorp.service.dto.FormationDTO;
import com.cv.cvcorp.service.dto.FormationCriteria;
import com.cv.cvcorp.service.FormationQueryService;

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
 * REST controller for managing {@link com.cv.cvcorp.domain.Formation}.
 */
@RestController
@RequestMapping("/api")
public class FormationResource {

    private final Logger log = LoggerFactory.getLogger(FormationResource.class);

    private static final String ENTITY_NAME = "cvcorpFormation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormationService formationService;

    private final FormationQueryService formationQueryService;

    public FormationResource(FormationService formationService, FormationQueryService formationQueryService) {
        this.formationService = formationService;
        this.formationQueryService = formationQueryService;
    }

    /**
     * {@code POST  /formations} : Create a new formation.
     *
     * @param formationDTO the formationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formationDTO, or with status {@code 400 (Bad Request)} if the formation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formations")
    public ResponseEntity<FormationDTO> createFormation(@RequestBody FormationDTO formationDTO) throws URISyntaxException {
        log.debug("REST request to save Formation : {}", formationDTO);
        if (formationDTO.getId() != null) {
            throw new BadRequestAlertException("A new formation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormationDTO result = formationService.save(formationDTO);
        return ResponseEntity.created(new URI("/api/formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formations} : Updates an existing formation.
     *
     * @param formationDTO the formationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationDTO,
     * or with status {@code 400 (Bad Request)} if the formationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formations")
    public ResponseEntity<FormationDTO> updateFormation(@RequestBody FormationDTO formationDTO) throws URISyntaxException {
        log.debug("REST request to update Formation : {}", formationDTO);
        if (formationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormationDTO result = formationService.save(formationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, formationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /formations} : get all the formations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formations in body.
     */
    @GetMapping("/formations")
    public ResponseEntity<List<FormationDTO>> getAllFormations(FormationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Formations by criteria: {}", criteria);
        Page<FormationDTO> page = formationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formations/count} : count all the formations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/formations/count")
    public ResponseEntity<Long> countFormations(FormationCriteria criteria) {
        log.debug("REST request to count Formations by criteria: {}", criteria);
        return ResponseEntity.ok().body(formationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /formations/:id} : get the "id" formation.
     *
     * @param id the id of the formationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formations/{id}")
    public ResponseEntity<FormationDTO> getFormation(@PathVariable Long id) {
        log.debug("REST request to get Formation : {}", id);
        Optional<FormationDTO> formationDTO = formationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formationDTO);
    }

    /**
     * {@code DELETE  /formations/:id} : delete the "id" formation.
     *
     * @param id the id of the formationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formations/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        log.debug("REST request to delete Formation : {}", id);
        formationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/formations?query=:query} : search for the formation corresponding
     * to the query.
     *
     * @param query the query of the formation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/formations")
    public ResponseEntity<List<FormationDTO>> searchFormations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Formations for query {}", query);
        Page<FormationDTO> page = formationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
