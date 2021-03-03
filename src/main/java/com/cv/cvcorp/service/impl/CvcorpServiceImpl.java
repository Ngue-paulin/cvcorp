package com.cv.cvcorp.service.impl;

import com.cv.cvcorp.service.CvcorpService;
import com.cv.cvcorp.domain.Cvcorp;
import com.cv.cvcorp.repository.CvcorpRepository;
import com.cv.cvcorp.repository.search.CvcorpSearchRepository;
import com.cv.cvcorp.service.dto.CvcorpDTO;
import com.cv.cvcorp.service.mapper.CvcorpMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Cvcorp}.
 */
@Service
@Transactional
public class CvcorpServiceImpl implements CvcorpService {

    private final Logger log = LoggerFactory.getLogger(CvcorpServiceImpl.class);

    private final CvcorpRepository cvcorpRepository;

    private final CvcorpMapper cvcorpMapper;

    private final CvcorpSearchRepository cvcorpSearchRepository;

    public CvcorpServiceImpl(CvcorpRepository cvcorpRepository, CvcorpMapper cvcorpMapper, CvcorpSearchRepository cvcorpSearchRepository) {
        this.cvcorpRepository = cvcorpRepository;
        this.cvcorpMapper = cvcorpMapper;
        this.cvcorpSearchRepository = cvcorpSearchRepository;
    }

    @Override
    public CvcorpDTO save(CvcorpDTO cvcorpDTO) {
        log.debug("Request to save Cvcorp : {}", cvcorpDTO);
        Cvcorp cvcorp = cvcorpMapper.toEntity(cvcorpDTO);
        cvcorp = cvcorpRepository.save(cvcorp);
        CvcorpDTO result = cvcorpMapper.toDto(cvcorp);
        cvcorpSearchRepository.save(cvcorp);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CvcorpDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cvcorps");
        return cvcorpRepository.findAll(pageable)
            .map(cvcorpMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CvcorpDTO> findOne(Long id) {
        log.debug("Request to get Cvcorp : {}", id);
        return cvcorpRepository.findById(id)
            .map(cvcorpMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cvcorp : {}", id);
        cvcorpRepository.deleteById(id);
        cvcorpSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CvcorpDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cvcorps for query {}", query);
        return cvcorpSearchRepository.search(queryStringQuery(query), pageable)
            .map(cvcorpMapper::toDto);
    }
}
