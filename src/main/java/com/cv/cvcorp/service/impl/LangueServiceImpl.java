package com.cv.cvcorp.service.impl;

import com.cv.cvcorp.service.LangueService;
import com.cv.cvcorp.domain.Langue;
import com.cv.cvcorp.repository.LangueRepository;
import com.cv.cvcorp.repository.search.LangueSearchRepository;
import com.cv.cvcorp.service.dto.LangueDTO;
import com.cv.cvcorp.service.mapper.LangueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Langue}.
 */
@Service
@Transactional
public class LangueServiceImpl implements LangueService {

    private final Logger log = LoggerFactory.getLogger(LangueServiceImpl.class);

    private final LangueRepository langueRepository;

    private final LangueMapper langueMapper;

    private final LangueSearchRepository langueSearchRepository;

    public LangueServiceImpl(LangueRepository langueRepository, LangueMapper langueMapper, LangueSearchRepository langueSearchRepository) {
        this.langueRepository = langueRepository;
        this.langueMapper = langueMapper;
        this.langueSearchRepository = langueSearchRepository;
    }

    @Override
    public LangueDTO save(LangueDTO langueDTO) {
        log.debug("Request to save Langue : {}", langueDTO);
        Langue langue = langueMapper.toEntity(langueDTO);
        langue = langueRepository.save(langue);
        LangueDTO result = langueMapper.toDto(langue);
        langueSearchRepository.save(langue);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LangueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Langues");
        return langueRepository.findAll(pageable)
            .map(langueMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<LangueDTO> findOne(Long id) {
        log.debug("Request to get Langue : {}", id);
        return langueRepository.findById(id)
            .map(langueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Langue : {}", id);
        langueRepository.deleteById(id);
        langueSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LangueDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Langues for query {}", query);
        return langueSearchRepository.search(queryStringQuery(query), pageable)
            .map(langueMapper::toDto);
    }
}
