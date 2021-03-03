package com.cv.cvcorp.repository;

import com.cv.cvcorp.domain.Competence;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Competence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Long>, JpaSpecificationExecutor<Competence> {
}
