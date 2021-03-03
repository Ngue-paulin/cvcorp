package com.cv.cvcorp.repository;

import com.cv.cvcorp.domain.Cvcorp;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Cvcorp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CvcorpRepository extends JpaRepository<Cvcorp, Long>, JpaSpecificationExecutor<Cvcorp> {
}
