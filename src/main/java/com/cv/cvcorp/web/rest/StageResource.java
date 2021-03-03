package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.service.StageService;
import com.cv.cvcorp.web.rest.errors.BadRequestAlertException;
import com.cv.cvcorp.service.dto.StageDTO;
import com.cv.cvcorp.service.dto.StageCriteria;
import com.cv.cvcorp.service.StageQueryService;

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
 * REST controller for managing {@link com.cv.cvcorp.domain.Stage}.
 */
@RestController
@RequestMapping("/api")
public class StageResource {

    private final Logger log = LoggerFactory.getLogger(StageResource.class);

    private static final String ENTITY_NAME = "cvcorpStage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StageService stageService;

    private final StageQueryService stageQueryService;

    public StageResource(StageService stageService, StageQueryService stageQueryService) {
        this.stageService = stageService;
        this.stageQueryService = stageQueryService;
    }

    /**
     * {@code POST  /stages} : Create a new stage.
     *
     * @param stageDTO the stageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stageDTO, or with status {@code 400 (Bad Request)} if the stage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stages")
    public ResponseEntity<StageDTO> createStage(@RequestBody StageDTO stageDTO) throws URISyntaxException {
        log.debug("REST request to save Stage : {}", stageDTO);
        if (stageDTO.getId() != null) {
            throw new BadRequestAlertException("A new stage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StageDTO result = stageService.save(stageDTO);
        return ResponseEntity.created(new URI("/api/stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stages} : Updates an existing stage.
     *
     * @param stageDTO the stageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stageDTO,
     * or with status {@code 400 (Bad Request)} if the stageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stages")
    public ResponseEntity<StageDTO> updateStage(@RequestBody StageDTO stageDTO) throws URISyntaxException {
        log.debug("REST request to update Stage : {}", stageDTO);
        if (stageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StageDTO result = stageService.save(stageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /stages} : get all the stages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stages in body.
     */
    @GetMapping("/stages")
    public ResponseEntity<List<StageDTO>> getAllStages(StageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Stages by criteria: {}", criteria);
        Page<StageDTO> page = stageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stages/count} : count all the stages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/stages/count")
    public ResponseEntity<Long> countStages(StageCriteria criteria) {
        log.debug("REST request to count Stages by criteria: {}", criteria);
        return ResponseEntity.ok().body(stageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /stages/:id} : get the "id" stage.
     *
     * @param id the id of the stageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stages/{id}")
    public ResponseEntity<StageDTO> getStage(@PathVariable Long id) {
        log.debug("REST request to get Stage : {}", id);
        Optional<StageDTO> stageDTO = stageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stageDTO);
    }

    /**
     * {@code DELETE  /stages/:id} : delete the "id" stage.
     *
     * @param id the id of the stageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stages/{id}")
    public ResponseEntity<Void> deleteStage(@PathVariable Long id) {
        log.debug("REST request to delete Stage : {}", id);
        stageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/stages?query=:query} : search for the stage corresponding
     * to the query.
     *
     * @param query the query of the stage search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/stages")
    public ResponseEntity<List<StageDTO>> searchStages(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Stages for query {}", query);
        Page<StageDTO> page = stageService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
