package com.cv.cvcorp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cv.cvcorp.web.rest.TestUtil;

public class LangueDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LangueDTO.class);
        LangueDTO langueDTO1 = new LangueDTO();
        langueDTO1.setId(1L);
        LangueDTO langueDTO2 = new LangueDTO();
        assertThat(langueDTO1).isNotEqualTo(langueDTO2);
        langueDTO2.setId(langueDTO1.getId());
        assertThat(langueDTO1).isEqualTo(langueDTO2);
        langueDTO2.setId(2L);
        assertThat(langueDTO1).isNotEqualTo(langueDTO2);
        langueDTO1.setId(null);
        assertThat(langueDTO1).isNotEqualTo(langueDTO2);
    }
}
