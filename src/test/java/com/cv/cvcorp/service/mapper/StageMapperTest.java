package com.cv.cvcorp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StageMapperTest {

    private StageMapper stageMapper;

    @BeforeEach
    public void setUp() {
        stageMapper = new StageMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(stageMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(stageMapper.fromId(null)).isNull();
    }
}
