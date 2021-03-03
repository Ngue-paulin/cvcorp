package com.cv.cvcorp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cv.cvcorp.web.rest.TestUtil;

public class LangueTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Langue.class);
        Langue langue1 = new Langue();
        langue1.setId(1L);
        Langue langue2 = new Langue();
        langue2.setId(langue1.getId());
        assertThat(langue1).isEqualTo(langue2);
        langue2.setId(2L);
        assertThat(langue1).isNotEqualTo(langue2);
        langue1.setId(null);
        assertThat(langue1).isNotEqualTo(langue2);
    }
}
