package com.cv.cvcorp.service;

import com.cv.cvcorp.service.dto.LangueDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.cv.cvcorp.domain.Langue}.
 */
public interface LangueService {

    /**
     * Save a langue.
     *
     * @param langueDTO the entity to save.
     * @return the persisted entity.
     */
    LangueDTO save(LangueDTO langueDTO);

    /**
     * Get all the langues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LangueDTO> findAll(Pageable pageable);


    /**
     * Get the "id" langue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LangueDTO> findOne(Long id);

    /**
     * Delete the "id" langue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the langue corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LangueDTO> search(String query, Pageable pageable);
}
