package com.cv.cvcorp.repository;

import com.cv.cvcorp.domain.Stage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Stage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StageRepository extends JpaRepository<Stage, Long>, JpaSpecificationExecutor<Stage> {
}
