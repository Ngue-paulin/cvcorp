package com.cv.cvcorp.repository.search;

import com.cv.cvcorp.domain.Experience;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Experience} entity.
 */
public interface ExperienceSearchRepository extends ElasticsearchRepository<Experience, Long> {
}
