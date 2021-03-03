package com.cv.cvcorp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FormationMapperTest {

    private FormationMapper formationMapper;

    @BeforeEach
    public void setUp() {
        formationMapper = new FormationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(formationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(formationMapper.fromId(null)).isNull();
    }
}
