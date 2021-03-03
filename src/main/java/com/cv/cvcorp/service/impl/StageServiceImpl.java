package com.cv.cvcorp.service.impl;

import com.cv.cvcorp.service.StageService;
import com.cv.cvcorp.domain.Stage;
import com.cv.cvcorp.repository.StageRepository;
import com.cv.cvcorp.repository.search.StageSearchRepository;
import com.cv.cvcorp.service.dto.StageDTO;
import com.cv.cvcorp.service.mapper.StageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Stage}.
 */
@Service
@Transactional
public class StageServiceImpl implements StageService {

    private final Logger log = LoggerFactory.getLogger(StageServiceImpl.class);

    private final StageRepository stageRepository;

    private final StageMapper stageMapper;

    private final StageSearchRepository stageSearchRepository;

    public StageServiceImpl(StageRepository stageRepository, StageMapper stageMapper, StageSearchRepository stageSearchRepository) {
        this.stageRepository = stageRepository;
        this.stageMapper = stageMapper;
        this.stageSearchRepository = stageSearchRepository;
    }

    @Override
    public StageDTO save(StageDTO stageDTO) {
        log.debug("Request to save Stage : {}", stageDTO);
        Stage stage = stageMapper.toEntity(stageDTO);
        stage = stageRepository.save(stage);
        StageDTO result = stageMapper.toDto(stage);
        stageSearchRepository.save(stage);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stages");
        return stageRepository.findAll(pageable)
            .map(stageMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<StageDTO> findOne(Long id) {
        log.debug("Request to get Stage : {}", id);
        return stageRepository.findById(id)
            .map(stageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stage : {}", id);
        stageRepository.deleteById(id);
        stageSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StageDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stages for query {}", query);
        return stageSearchRepository.search(queryStringQuery(query), pageable)
            .map(stageMapper::toDto);
    }
}
