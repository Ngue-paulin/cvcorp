package com.cv.cvcorp.repository.search;

import com.cv.cvcorp.domain.Competence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Competence} entity.
 */
public interface CompetenceSearchRepository extends ElasticsearchRepository<Competence, Long> {
}
