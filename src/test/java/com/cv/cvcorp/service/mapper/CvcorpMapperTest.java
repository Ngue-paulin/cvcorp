package com.cv.cvcorp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CvcorpMapperTest {

    private CvcorpMapper cvcorpMapper;

    @BeforeEach
    public void setUp() {
        cvcorpMapper = new CvcorpMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(cvcorpMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cvcorpMapper.fromId(null)).isNull();
    }
}
