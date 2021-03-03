package com.cv.cvcorp.service.mapper;


import com.cv.cvcorp.domain.*;
import com.cv.cvcorp.service.dto.FormationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formation} and its DTO {@link FormationDTO}.
 */
@Mapper(componentModel = "spring", uses = {CvcorpMapper.class})
public interface FormationMapper extends EntityMapper<FormationDTO, Formation> {

    @Mapping(source = "cvcorp.id", target = "cvcorpId")
    FormationDTO toDto(Formation formation);

    @Mapping(source = "cvcorpId", target = "cvcorp")
    Formation toEntity(FormationDTO formationDTO);

    default Formation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Formation formation = new Formation();
        formation.setId(id);
        return formation;
    }
}
