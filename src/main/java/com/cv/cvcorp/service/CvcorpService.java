package com.cv.cvcorp.service;

import com.cv.cvcorp.service.dto.CvcorpDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.cv.cvcorp.domain.Cvcorp}.
 */
public interface CvcorpService {

    /**
     * Save a cvcorp.
     *
     * @param cvcorpDTO the entity to save.
     * @return the persisted entity.
     */
    CvcorpDTO save(CvcorpDTO cvcorpDTO);

    /**
     * Get all the cvcorps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CvcorpDTO> findAll(Pageable pageable);


    /**
     * Get the "id" cvcorp.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CvcorpDTO> findOne(Long id);

    /**
     * Delete the "id" cvcorp.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cvcorp corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CvcorpDTO> search(String query, Pageable pageable);
}
