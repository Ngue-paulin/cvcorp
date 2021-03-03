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

import com.cv.cvcorp.domain.Cvcorp;
import com.cv.cvcorp.domain.*; // for static metamodels
import com.cv.cvcorp.repository.CvcorpRepository;
import com.cv.cvcorp.repository.search.CvcorpSearchRepository;
import com.cv.cvcorp.service.dto.CvcorpCriteria;
import com.cv.cvcorp.service.dto.CvcorpDTO;
import com.cv.cvcorp.service.mapper.CvcorpMapper;

/**
 * Service for executing complex queries for {@link Cvcorp} entities in the database.
 * The main input is a {@link CvcorpCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CvcorpDTO} or a {@link Page} of {@link CvcorpDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CvcorpQueryService extends QueryService<Cvcorp> {

    private final Logger log = LoggerFactory.getLogger(CvcorpQueryService.class);

    private final CvcorpRepository cvcorpRepository;

    private final CvcorpMapper cvcorpMapper;

    private final CvcorpSearchRepository cvcorpSearchRepository;

    public CvcorpQueryService(CvcorpRepository cvcorpRepository, CvcorpMapper cvcorpMapper, CvcorpSearchRepository cvcorpSearchRepository) {
        this.cvcorpRepository = cvcorpRepository;
        this.cvcorpMapper = cvcorpMapper;
        this.cvcorpSearchRepository = cvcorpSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CvcorpDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CvcorpDTO> findByCriteria(CvcorpCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cvcorp> specification = createSpecification(criteria);
        return cvcorpMapper.toDto(cvcorpRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CvcorpDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CvcorpDTO> findByCriteria(CvcorpCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cvcorp> specification = createSpecification(criteria);
        return cvcorpRepository.findAll(specification, page)
            .map(cvcorpMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CvcorpCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cvcorp> specification = createSpecification(criteria);
        return cvcorpRepository.count(specification);
    }

    /**
     * Function to convert {@link CvcorpCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cvcorp> createSpecification(CvcorpCriteria criteria) {
        Specification<Cvcorp> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cvcorp_.id));
            }
            if (criteria.getProfil() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfil(), Cvcorp_.profil));
            }
            if (criteria.getPays() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPays(), Cvcorp_.pays));
            }
            if (criteria.getVille() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVille(), Cvcorp_.ville));
            }
            if (criteria.getDateNaissence() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissence(), Cvcorp_.dateNaissence));
            }
            if (criteria.getNaissanceLieu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNaissanceLieu(), Cvcorp_.naissanceLieu));
            }
            if (criteria.getEtatCivil() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtatCivil(), Cvcorp_.etatCivil));
            }
            if (criteria.getLinkedId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkedId(), Cvcorp_.linkedId));
            }
            if (criteria.getAdresse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdresse(), Cvcorp_.adresse));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSexe(), Cvcorp_.sexe));
            }
            if (criteria.getCodePostal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodePostal(), Cvcorp_.codePostal));
            }
            if (criteria.getCompetenceId() != null) {
                specification = specification.and(buildSpecification(criteria.getCompetenceId(),
                    root -> root.join(Cvcorp_.competences, JoinType.LEFT).get(Competence_.id)));
            }
            if (criteria.getExperienceId() != null) {
                specification = specification.and(buildSpecification(criteria.getExperienceId(),
                    root -> root.join(Cvcorp_.experiences, JoinType.LEFT).get(Experience_.id)));
            }
            if (criteria.getFormationId() != null) {
                specification = specification.and(buildSpecification(criteria.getFormationId(),
                    root -> root.join(Cvcorp_.formations, JoinType.LEFT).get(Formation_.id)));
            }
            if (criteria.getLangueId() != null) {
                specification = specification.and(buildSpecification(criteria.getLangueId(),
                    root -> root.join(Cvcorp_.langues, JoinType.LEFT).get(Langue_.id)));
            }
            if (criteria.getStageId() != null) {
                specification = specification.and(buildSpecification(criteria.getStageId(),
                    root -> root.join(Cvcorp_.stages, JoinType.LEFT).get(Stage_.id)));
            }
        }
        return specification;
    }
}
