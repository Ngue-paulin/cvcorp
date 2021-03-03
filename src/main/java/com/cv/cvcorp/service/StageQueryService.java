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

import com.cv.cvcorp.domain.Stage;
import com.cv.cvcorp.domain.*; // for static metamodels
import com.cv.cvcorp.repository.StageRepository;
import com.cv.cvcorp.repository.search.StageSearchRepository;
import com.cv.cvcorp.service.dto.StageCriteria;
import com.cv.cvcorp.service.dto.StageDTO;
import com.cv.cvcorp.service.mapper.StageMapper;

/**
 * Service for executing complex queries for {@link Stage} entities in the database.
 * The main input is a {@link StageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StageDTO} or a {@link Page} of {@link StageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StageQueryService extends QueryService<Stage> {

    private final Logger log = LoggerFactory.getLogger(StageQueryService.class);

    private final StageRepository stageRepository;

    private final StageMapper stageMapper;

    private final StageSearchRepository stageSearchRepository;

    public StageQueryService(StageRepository stageRepository, StageMapper stageMapper, StageSearchRepository stageSearchRepository) {
        this.stageRepository = stageRepository;
        this.stageMapper = stageMapper;
        this.stageSearchRepository = stageSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StageDTO> findByCriteria(StageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Stage> specification = createSpecification(criteria);
        return stageMapper.toDto(stageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StageDTO> findByCriteria(StageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Stage> specification = createSpecification(criteria);
        return stageRepository.findAll(specification, page)
            .map(stageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Stage> specification = createSpecification(criteria);
        return stageRepository.count(specification);
    }

    /**
     * Function to convert {@link StageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Stage> createSpecification(StageCriteria criteria) {
        Specification<Stage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Stage_.id));
            }
            if (criteria.getPeriodBigin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodBigin(), Stage_.periodBigin));
            }
            if (criteria.getPeriodEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodEnd(), Stage_.periodEnd));
            }
            if (criteria.getPeriodNow() != null) {
                specification = specification.and(buildSpecification(criteria.getPeriodNow(), Stage_.periodNow));
            }
            if (criteria.getVille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVille(), Stage_.ville));
            }
            if (criteria.getPoste() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPoste(), Stage_.poste));
            }
            if (criteria.getEmployeur() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmployeur(), Stage_.employeur));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Stage_.description));
            }
            if (criteria.getCvcorpId() != null) {
                specification = specification.and(buildSpecification(criteria.getCvcorpId(),
                    root -> root.join(Stage_.cvcorp, JoinType.LEFT).get(Cvcorp_.id)));
            }
        }
        return specification;
    }
}
