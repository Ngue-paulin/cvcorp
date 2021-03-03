package com.cv.cvcorp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cv.cvcorp.web.rest.TestUtil;

public class StageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stage.class);
        Stage stage1 = new Stage();
        stage1.setId(1L);
        Stage stage2 = new Stage();
        stage2.setId(stage1.getId());
        assertThat(stage1).isEqualTo(stage2);
        stage2.setId(2L);
        assertThat(stage1).isNotEqualTo(stage2);
        stage1.setId(null);
        assertThat(stage1).isNotEqualTo(stage2);
    }
}
