package com.cv.cvcorp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cv.cvcorp.web.rest.TestUtil;

public class ExperienceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExperienceDTO.class);
        ExperienceDTO experienceDTO1 = new ExperienceDTO();
        experienceDTO1.setId(1L);
        ExperienceDTO experienceDTO2 = new ExperienceDTO();
        assertThat(experienceDTO1).isNotEqualTo(experienceDTO2);
        experienceDTO2.setId(experienceDTO1.getId());
        assertThat(experienceDTO1).isEqualTo(experienceDTO2);
        experienceDTO2.setId(2L);
        assertThat(experienceDTO1).isNotEqualTo(experienceDTO2);
        experienceDTO1.setId(null);
        assertThat(experienceDTO1).isNotEqualTo(experienceDTO2);
    }
}
