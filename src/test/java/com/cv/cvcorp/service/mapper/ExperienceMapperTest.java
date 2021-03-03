package com.cv.cvcorp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ExperienceMapperTest {

    private ExperienceMapper experienceMapper;

    @BeforeEach
    public void setUp() {
        experienceMapper = new ExperienceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(experienceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(experienceMapper.fromId(null)).isNull();
    }
}
