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

import com.cv.cvcorp.domain.Formation;
import com.cv.cvcorp.domain.*; // for static metamodels
import com.cv.cvcorp.repository.FormationRepository;
import com.cv.cvcorp.repository.search.FormationSearchRepository;
import com.cv.cvcorp.service.dto.FormationCriteria;
import com.cv.cvcorp.service.dto.FormationDTO;
import com.cv.cvcorp.service.mapper.FormationMapper;

/**
 * Service for executing complex queries for {@link Formation} entities in the database.
 * The main input is a {@link FormationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormationDTO} or a {@link Page} of {@link FormationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormationQueryService extends QueryService<Formation> {

    private final Logger log = LoggerFactory.getLogger(FormationQueryService.class);

    private final FormationRepository formationRepository;

    private final FormationMapper formationMapper;

    private final FormationSearchRepository formationSearchRepository;

    public FormationQueryService(FormationRepository formationRepository, FormationMapper formationMapper, FormationSearchRepository formationSearchRepository) {
        this.formationRepository = formationRepository;
        this.formationMapper = formationMapper;
        this.formationSearchRepository = formationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FormationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormationDTO> findByCriteria(FormationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Formation> specification = createSpecification(criteria);
        return formationMapper.toDto(formationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FormationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormationDTO> findByCriteria(FormationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Formation> specification = createSpecification(criteria);
        return formationRepository.findAll(specification, page)
            .map(formationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Formation> specification = createSpecification(criteria);
        return formationRepository.count(specification);
    }

    /**
     * Function to convert {@link FormationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Formation> createSpecification(FormationCriteria criteria) {
        Specification<Formation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Formation_.id));
            }
            if (criteria.getPeriodBigin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodBigin(), Formation_.periodBigin));
            }
            if (criteria.getPeriodEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodEnd(), Formation_.periodEnd));
            }
            if (criteria.getPeriodNow() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodNow(), Formation_.periodNow));
            }
            if (criteria.getDiplome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiplome(), Formation_.diplome));
            }
            if (criteria.getVille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVille(), Formation_.ville));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Formation_.description));
            }
            if (criteria.getCvcorpId() != null) {
                specification = specification.and(buildSpecification(criteria.getCvcorpId(),
                    root -> root.join(Formation_.cvcorp, JoinType.LEFT).get(Cvcorp_.id)));
            }
        }
        return specification;
    }
}
