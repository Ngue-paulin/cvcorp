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

import com.cv.cvcorp.domain.Experience;
import com.cv.cvcorp.domain.*; // for static metamodels
import com.cv.cvcorp.repository.ExperienceRepository;
import com.cv.cvcorp.repository.search.ExperienceSearchRepository;
import com.cv.cvcorp.service.dto.ExperienceCriteria;
import com.cv.cvcorp.service.dto.ExperienceDTO;
import com.cv.cvcorp.service.mapper.ExperienceMapper;

/**
 * Service for executing complex queries for {@link Experience} entities in the database.
 * The main input is a {@link ExperienceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExperienceDTO} or a {@link Page} of {@link ExperienceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExperienceQueryService extends QueryService<Experience> {

    private final Logger log = LoggerFactory.getLogger(ExperienceQueryService.class);

    private final ExperienceRepository experienceRepository;

    private final ExperienceMapper experienceMapper;

    private final ExperienceSearchRepository experienceSearchRepository;

    public ExperienceQueryService(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper, ExperienceSearchRepository experienceSearchRepository) {
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
        this.experienceSearchRepository = experienceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ExperienceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExperienceDTO> findByCriteria(ExperienceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Experience> specification = createSpecification(criteria);
        return experienceMapper.toDto(experienceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExperienceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExperienceDTO> findByCriteria(ExperienceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Experience> specification = createSpecification(criteria);
        return experienceRepository.findAll(specification, page)
            .map(experienceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExperienceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Experience> specification = createSpecification(criteria);
        return experienceRepository.count(specification);
    }

    /**
     * Function to convert {@link ExperienceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Experience> createSpecification(ExperienceCriteria criteria) {
        Specification<Experience> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Experience_.id));
            }
            if (criteria.getPeriodBigin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodBigin(), Experience_.periodBigin));
            }
            if (criteria.getPeriodEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodEnd(), Experience_.periodEnd));
            }
            if (criteria.getPeriodNow() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodNow(), Experience_.periodNow));
            }
            if (criteria.getVille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVille(), Experience_.ville));
            }
            if (criteria.getPoste() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPoste(), Experience_.poste));
            }
            if (criteria.getEmployeur() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmployeur(), Experience_.employeur));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Experience_.description));
            }
            if (criteria.getCvcorpId() != null) {
                specification = specification.and(buildSpecification(criteria.getCvcorpId(),
                    root -> root.join(Experience_.cvcorp, JoinType.LEFT).get(Cvcorp_.id)));
            }
        }
        return specification;
    }
}
