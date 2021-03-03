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

import com.cv.cvcorp.domain.Competence;
import com.cv.cvcorp.domain.*; // for static metamodels
import com.cv.cvcorp.repository.CompetenceRepository;
import com.cv.cvcorp.repository.search.CompetenceSearchRepository;
import com.cv.cvcorp.service.dto.CompetenceCriteria;
import com.cv.cvcorp.service.dto.CompetenceDTO;
import com.cv.cvcorp.service.mapper.CompetenceMapper;

/**
 * Service for executing complex queries for {@link Competence} entities in the database.
 * The main input is a {@link CompetenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompetenceDTO} or a {@link Page} of {@link CompetenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompetenceQueryService extends QueryService<Competence> {

    private final Logger log = LoggerFactory.getLogger(CompetenceQueryService.class);

    private final CompetenceRepository competenceRepository;

    private final CompetenceMapper competenceMapper;

    private final CompetenceSearchRepository competenceSearchRepository;

    public CompetenceQueryService(CompetenceRepository competenceRepository, CompetenceMapper competenceMapper, CompetenceSearchRepository competenceSearchRepository) {
        this.competenceRepository = competenceRepository;
        this.competenceMapper = competenceMapper;
        this.competenceSearchRepository = competenceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CompetenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompetenceDTO> findByCriteria(CompetenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Competence> specification = createSpecification(criteria);
        return competenceMapper.toDto(competenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CompetenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompetenceDTO> findByCriteria(CompetenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Competence> specification = createSpecification(criteria);
        return competenceRepository.findAll(specification, page)
            .map(competenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompetenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Competence> specification = createSpecification(criteria);
        return competenceRepository.count(specification);
    }

    /**
     * Function to convert {@link CompetenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Competence> createSpecification(CompetenceCriteria criteria) {
        Specification<Competence> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Competence_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Competence_.description));
            }
            if (criteria.getLibele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibele(), Competence_.libele));
            }
            if (criteria.getNiveau() != null) {
                specification = specification.and(buildSpecification(criteria.getNiveau(), Competence_.niveau));
            }
            if (criteria.getCvcorpId() != null) {
                specification = specification.and(buildSpecification(criteria.getCvcorpId(),
                    root -> root.join(Competence_.cvcorp, JoinType.LEFT).get(Cvcorp_.id)));
            }
        }
        return specification;
    }
}
