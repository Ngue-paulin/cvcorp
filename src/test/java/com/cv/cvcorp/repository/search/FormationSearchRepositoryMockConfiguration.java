package com.cv.cvcorp.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FormationSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FormationSearchRepositoryMockConfiguration {

    @MockBean
    private FormationSearchRepository mockFormationSearchRepository;

}
