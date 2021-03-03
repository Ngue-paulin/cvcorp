package com.cv.cvcorp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cv.cvcorp.web.rest.TestUtil;

public class CvcorpTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cvcorp.class);
        Cvcorp cvcorp1 = new Cvcorp();
        cvcorp1.setId(1L);
        Cvcorp cvcorp2 = new Cvcorp();
        cvcorp2.setId(cvcorp1.getId());
        assertThat(cvcorp1).isEqualTo(cvcorp2);
        cvcorp2.setId(2L);
        assertThat(cvcorp1).isNotEqualTo(cvcorp2);
        cvcorp1.setId(null);
        assertThat(cvcorp1).isNotEqualTo(cvcorp2);
    }
}
