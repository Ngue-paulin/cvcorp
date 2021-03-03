package com.cv.cvcorp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cv.cvcorp.web.rest.TestUtil;

public class CvcorpDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CvcorpDTO.class);
        CvcorpDTO cvcorpDTO1 = new CvcorpDTO();
        cvcorpDTO1.setId(1L);
        CvcorpDTO cvcorpDTO2 = new CvcorpDTO();
        assertThat(cvcorpDTO1).isNotEqualTo(cvcorpDTO2);
        cvcorpDTO2.setId(cvcorpDTO1.getId());
        assertThat(cvcorpDTO1).isEqualTo(cvcorpDTO2);
        cvcorpDTO2.setId(2L);
        assertThat(cvcorpDTO1).isNotEqualTo(cvcorpDTO2);
        cvcorpDTO1.setId(null);
        assertThat(cvcorpDTO1).isNotEqualTo(cvcorpDTO2);
    }
}
