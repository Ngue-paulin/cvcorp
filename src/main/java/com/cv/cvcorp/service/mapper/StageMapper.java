package com.cv.cvcorp.service.mapper;


import com.cv.cvcorp.domain.*;
import com.cv.cvcorp.service.dto.StageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stage} and its DTO {@link StageDTO}.
 */
@Mapper(componentModel = "spring", uses = {CvcorpMapper.class})
public interface StageMapper extends EntityMapper<StageDTO, Stage> {

    @Mapping(source = "cvcorp.id", target = "cvcorpId")
    StageDTO toDto(Stage stage);

    @Mapping(source = "cvcorpId", target = "cvcorp")
    Stage toEntity(StageDTO stageDTO);

    default Stage fromId(Long id) {
        if (id == null) {
            return null;
        }
        Stage stage = new Stage();
        stage.setId(id);
        return stage;
    }
}
