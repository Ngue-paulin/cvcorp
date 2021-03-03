package com.cv.cvcorp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.cv.cvcorp.domain.Langue;
import com.cv.cvcorp.domain.*; // for static metamodels
import com.cv.cvcorp.repository.LangueRepository;
import com.cv.cvcorp.repository.search.LangueSearchRepository;
import com.cv.cvcorp.service.dto.LangueCriteria;
import com.cv.cvcorp.service.dto.LangueDTO;
import com.cv.cvcorp.service.mapper.LangueMapper;

/**
 * Service for executing complex queries for {@link Langue} entities in the database.
 * The main input is a {@link LangueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LangueDTO} or a {@link Page} of {@link LangueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LangueQueryService extends QueryService<Langue> {

    private final Logger log = LoggerFactory.getLogger(LangueQueryService.class);

    private final LangueRepository langueRepository;

    private final LangueMapper langueMapper;

    private final LangueSearchRepository langueSearchRepository;

    public LangueQueryService(LangueRepository langueRepository, LangueMapper langueMapper, LangueSearchRepository langueSearchRepository) {
        this.langueRepository = langueRepository;
        this.langueMapper = langueMapper;
        this.langueSearchRepository = langueSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LangueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LangueDTO> findByCriteria(LangueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Langue> specification = createSpecification(criteria);
        return langueMapper.toDto(langueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LangueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LangueDTO> findByCriteria(LangueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Langue> specification = createSpecification(criteria);
        return langueRepository.findAll(specification, page)
            .map(langueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LangueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Langue> specification = createSpecification(criteria);
        return langueRepository.count(specification);
    }

    /**
     * Function to convert {@link LangueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Langue> createSpecification(LangueCriteria criteria) {
        Specification<Langue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Langue_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Langue_.description));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), Langue_.libele));
            }
            if (criteria.getNiveau() != null) {
                specification = specification.and(buildSpecification(criteria.getNiveau(), Langue_.niveau));
            }
            if (criteria.getCvcorpId() != null) {
                specification = specification.and(buildSpecification(criteria.getCvcorpId(),
                    root -> root.join(Langue_.cvcorp, JoinType.LEFT).get(Cvcorp_.id)));
            }
        }
        return specification;
    }
}
