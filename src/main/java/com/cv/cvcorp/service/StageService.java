package com.cv.cvcorp.service;

import com.cv.cvcorp.service.dto.StageDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.cv.cvcorp.domain.Stage}.
 */
public interface StageService {

    /**
     * Save a stage.
     *
     * @param stageDTO the entity to save.
     * @return the persisted entity.
     */
    StageDTO save(StageDTO stageDTO);

    /**
     * Get all the stages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StageDTO> findAll(Pageable pageable);


    /**
     * Get the "id" stage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StageDTO> findOne(Long id);

    /**
     * Delete the "id" stage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the stage corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StageDTO> search(String query, Pageable pageable);
}
