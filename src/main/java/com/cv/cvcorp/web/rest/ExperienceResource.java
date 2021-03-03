package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.service.ExperienceService;
import com.cv.cvcorp.web.rest.errors.BadRequestAlertException;
import com.cv.cvcorp.service.dto.ExperienceDTO;
import com.cv.cvcorp.service.dto.ExperienceCriteria;
import com.cv.cvcorp.service.ExperienceQueryService;

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
 * REST controller for managing {@link com.cv.cvcorp.domain.Experience}.
 */
@RestController
@RequestMapping("/api")
public class ExperienceResource {

    private final Logger log = LoggerFactory.getLogger(ExperienceResource.class);

    private static final String ENTITY_NAME = "cvcorpExperience";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExperienceService experienceService;

    private final ExperienceQueryService experienceQueryService;

    public ExperienceResource(ExperienceService experienceService, ExperienceQueryService experienceQueryService) {
        this.experienceService = experienceService;
        this.experienceQueryService = experienceQueryService;
    }

    /**
     * {@code POST  /experiences} : Create a new experience.
     *
     * @param experienceDTO the experienceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new experienceDTO, or with status {@code 400 (Bad Request)} if the experience has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/experiences")
    public ResponseEntity<ExperienceDTO> createExperience(@RequestBody ExperienceDTO experienceDTO) throws URISyntaxException {
        log.debug("REST request to save Experience : {}", experienceDTO);
        if (experienceDTO.getId() != null) {
            throw new BadRequestAlertException("A new experience cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExperienceDTO result = experienceService.save(experienceDTO);
        return ResponseEntity.created(new URI("/api/experiences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /experiences} : Updates an existing experience.
     *
     * @param experienceDTO the experienceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated experienceDTO,
     * or with status {@code 400 (Bad Request)} if the experienceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the experienceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/experiences")
    public ResponseEntity<ExperienceDTO> updateExperience(@RequestBody ExperienceDTO experienceDTO) throws URISyntaxException {
        log.debug("REST request to update Experience : {}", experienceDTO);
        if (experienceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExperienceDTO result = experienceService.save(experienceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, experienceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /experiences} : get all the experiences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of experiences in body.
     */
    @GetMapping("/experiences")
    public ResponseEntity<List<ExperienceDTO>> getAllExperiences(ExperienceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Experiences by criteria: {}", criteria);
        Page<ExperienceDTO> page = experienceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /experiences/count} : count all the experiences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/experiences/count")
    public ResponseEntity<Long> countExperiences(ExperienceCriteria criteria) {
        log.debug("REST request to count Experiences by criteria: {}", criteria);
        return ResponseEntity.ok().body(experienceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /experiences/:id} : get the "id" experience.
     *
     * @param id the id of the experienceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the experienceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/experiences/{id}")
    public ResponseEntity<ExperienceDTO> getExperience(@PathVariable Long id) {
        log.debug("REST request to get Experience : {}", id);
        Optional<ExperienceDTO> experienceDTO = experienceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(experienceDTO);
    }

    /**
     * {@code DELETE  /experiences/:id} : delete the "id" experience.
     *
     * @param id the id of the experienceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/experiences/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        log.debug("REST request to delete Experience : {}", id);
        experienceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/experiences?query=:query} : search for the experience corresponding
     * to the query.
     *
     * @param query the query of the experience search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/experiences")
    public ResponseEntity<List<ExperienceDTO>> searchExperiences(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Experiences for query {}", query);
        Page<ExperienceDTO> page = experienceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
