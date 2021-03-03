package com.cv.cvcorp.repository.search;

import com.cv.cvcorp.domain.Langue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Langue} entity.
 */
public interface LangueSearchRepository extends ElasticsearchRepository<Langue, Long> {
}
