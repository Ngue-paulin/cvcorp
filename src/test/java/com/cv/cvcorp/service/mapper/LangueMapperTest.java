package com.cv.cvcorp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LangueMapperTest {

    private LangueMapper langueMapper;

    @BeforeEach
    public void setUp() {
        langueMapper = new LangueMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(langueMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(langueMapper.fromId(null)).isNull();
    }
}
