package com.cv.cvcorp.repository;

import com.cv.cvcorp.domain.Langue;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Langue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LangueRepository extends JpaRepository<Langue, Long>, JpaSpecificationExecutor<Langue> {
}
