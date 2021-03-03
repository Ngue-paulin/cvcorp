package com.cv.cvcorp.repository.search;

import com.cv.cvcorp.domain.Cvcorp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Cvcorp} entity.
 */
public interface CvcorpSearchRepository extends ElasticsearchRepository<Cvcorp, Long> {
}
