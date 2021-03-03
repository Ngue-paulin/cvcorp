package com.cv.cvcorp.service.impl;

import com.cv.cvcorp.service.CompetenceService;
import com.cv.cvcorp.domain.Competence;
import com.cv.cvcorp.repository.CompetenceRepository;
import com.cv.cvcorp.repository.search.CompetenceSearchRepository;
import com.cv.cvcorp.service.dto.CompetenceDTO;
import com.cv.cvcorp.service.mapper.CompetenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Competence}.
 */
@Service
@Transactional
public class CompetenceServiceImpl implements CompetenceService {

    private final Logger log = LoggerFactory.getLogger(CompetenceServiceImpl.class);

    private final CompetenceRepository competenceRepository;

    private final CompetenceMapper competenceMapper;

    private final CompetenceSearchRepository competenceSearchRepository;

    public CompetenceServiceImpl(CompetenceRepository competenceRepository, CompetenceMapper competenceMapper, CompetenceSearchRepository competenceSearchRepository) {
        this.competenceRepository = competenceRepository;
        this.competenceMapper = competenceMapper;
        this.competenceSearchRepository = competenceSearchRepository;
    }

    @Override
    public CompetenceDTO save(CompetenceDTO competenceDTO) {
        log.debug("Request to save Competence : {}", competenceDTO);
        Competence competence = competenceMapper.toEntity(competenceDTO);
        competence = competenceRepository.save(competence);
        CompetenceDTO result = competenceMapper.toDto(competence);
        competenceSearchRepository.save(competence);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompetenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Competences");
        return competenceRepository.findAll(pageable)
            .map(competenceMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CompetenceDTO> findOne(Long id) {
        log.debug("Request to get Competence : {}", id);
        return competenceRepository.findById(id)
            .map(competenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Competence : {}", id);
        competenceRepository.deleteById(id);
        competenceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompetenceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Competences for query {}", query);
        return competenceSearchRepository.search(queryStringQuery(query), pageable)
            .map(competenceMapper::toDto);
    }
}
