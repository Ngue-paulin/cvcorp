package com.cv.cvcorp.repository.search;

import com.cv.cvcorp.domain.Formation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Formation} entity.
 */
public interface FormationSearchRepository extends ElasticsearchRepository<Formation, Long> {
}
