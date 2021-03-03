package com.cv.cvcorp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cv.cvcorp.web.rest.TestUtil;

public class StageDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StageDTO.class);
        StageDTO stageDTO1 = new StageDTO();
        stageDTO1.setId(1L);
        StageDTO stageDTO2 = new StageDTO();
        assertThat(stageDTO1).isNotEqualTo(stageDTO2);
        stageDTO2.setId(stageDTO1.getId());
        assertThat(stageDTO1).isEqualTo(stageDTO2);
        stageDTO2.setId(2L);
        assertThat(stageDTO1).isNotEqualTo(stageDTO2);
        stageDTO1.setId(null);
        assertThat(stageDTO1).isNotEqualTo(stageDTO2);
    }
}
