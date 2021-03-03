package com.cv.cvcorp.service.impl;

import com.cv.cvcorp.service.FormationService;
import com.cv.cvcorp.domain.Formation;
import com.cv.cvcorp.repository.FormationRepository;
import com.cv.cvcorp.repository.search.FormationSearchRepository;
import com.cv.cvcorp.service.dto.FormationDTO;
import com.cv.cvcorp.service.mapper.FormationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Formation}.
 */
@Service
@Transactional
public class FormationServiceImpl implements FormationService {

    private final Logger log = LoggerFactory.getLogger(FormationServiceImpl.class);

    private final FormationRepository formationRepository;

    private final FormationMapper formationMapper;

    private final FormationSearchRepository formationSearchRepository;

    public FormationServiceImpl(FormationRepository formationRepository, FormationMapper formationMapper, FormationSearchRepository formationSearchRepository) {
        this.formationRepository = formationRepository;
        this.formationMapper = formationMapper;
        this.formationSearchRepository = formationSearchRepository;
    }

    @Override
    public FormationDTO save(FormationDTO formationDTO) {
        log.debug("Request to save Formation : {}", formationDTO);
        Formation formation = formationMapper.toEntity(formationDTO);
        formation = formationRepository.save(formation);
        FormationDTO result = formationMapper.toDto(formation);
        formationSearchRepository.save(formation);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Formations");
        return formationRepository.findAll(pageable)
            .map(formationMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<FormationDTO> findOne(Long id) {
        log.debug("Request to get Formation : {}", id);
        return formationRepository.findById(id)
            .map(formationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Formation : {}", id);
        formationRepository.deleteById(id);
        formationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Formations for query {}", query);
        return formationSearchRepository.search(queryStringQuery(query), pageable)
            .map(formationMapper::toDto);
    }
}
