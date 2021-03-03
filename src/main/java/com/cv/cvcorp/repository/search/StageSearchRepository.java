package com.cv.cvcorp.repository.search;

import com.cv.cvcorp.domain.Stage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Stage} entity.
 */
public interface StageSearchRepository extends ElasticsearchRepository<Stage, Long> {
}
