package com.cv.cvcorp.service.impl;

import com.cv.cvcorp.service.ExperienceService;
import com.cv.cvcorp.domain.Experience;
import com.cv.cvcorp.repository.ExperienceRepository;
import com.cv.cvcorp.repository.search.ExperienceSearchRepository;
import com.cv.cvcorp.service.dto.ExperienceDTO;
import com.cv.cvcorp.service.mapper.ExperienceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Experience}.
 */
@Service
@Transactional
public class ExperienceServiceImpl implements ExperienceService {

    private final Logger log = LoggerFactory.getLogger(ExperienceServiceImpl.class);

    private final ExperienceRepository experienceRepository;

    private final ExperienceMapper experienceMapper;

    private final ExperienceSearchRepository experienceSearchRepository;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper, ExperienceSearchRepository experienceSearchRepository) {
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
        this.experienceSearchRepository = experienceSearchRepository;
    }

    @Override
    public ExperienceDTO save(ExperienceDTO experienceDTO) {
        log.debug("Request to save Experience : {}", experienceDTO);
        Experience experience = experienceMapper.toEntity(experienceDTO);
        experience = experienceRepository.save(experience);
        ExperienceDTO result = experienceMapper.toDto(experience);
        experienceSearchRepository.save(experience);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExperienceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Experiences");
        return experienceRepository.findAll(pageable)
            .map(experienceMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ExperienceDTO> findOne(Long id) {
        log.debug("Request to get Experience : {}", id);
        return experienceRepository.findById(id)
            .map(experienceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Experience : {}", id);
        experienceRepository.deleteById(id);
        experienceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExperienceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Experiences for query {}", query);
        return experienceSearchRepository.search(queryStringQuery(query), pageable)
            .map(experienceMapper::toDto);
    }
}
