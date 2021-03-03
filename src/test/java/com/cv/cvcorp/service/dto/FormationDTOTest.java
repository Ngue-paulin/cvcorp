package com.cv.cvcorp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cv.cvcorp.web.rest.TestUtil;

public class FormationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormationDTO.class);
        FormationDTO formationDTO1 = new FormationDTO();
        formationDTO1.setId(1L);
        FormationDTO formationDTO2 = new FormationDTO();
        assertThat(formationDTO1).isNotEqualTo(formationDTO2);
        formationDTO2.setId(formationDTO1.getId());
        assertThat(formationDTO1).isEqualTo(formationDTO2);
        formationDTO2.setId(2L);
        assertThat(formationDTO1).isNotEqualTo(formationDTO2);
        formationDTO1.setId(null);
        assertThat(formationDTO1).isNotEqualTo(formationDTO2);
    }
}
