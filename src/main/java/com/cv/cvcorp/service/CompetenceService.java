package com.cv.cvcorp.service;

import com.cv.cvcorp.service.dto.CompetenceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.cv.cvcorp.domain.Competence}.
 */
public interface CompetenceService {

    /**
     * Save a competence.
     *
     * @param competenceDTO the entity to save.
     * @return the persisted entity.
     */
    CompetenceDTO save(CompetenceDTO competenceDTO);

    /**
     * Get all the competences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompetenceDTO> findAll(Pageable pageable);


    /**
     * Get the "id" competence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompetenceDTO> findOne(Long id);

    /**
     * Delete the "id" competence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the competence corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompetenceDTO> search(String query, Pageable pageable);
}
