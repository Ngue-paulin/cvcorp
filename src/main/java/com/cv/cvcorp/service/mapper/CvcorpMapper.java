package com.cv.cvcorp.service.mapper;


import com.cv.cvcorp.domain.*;
import com.cv.cvcorp.service.dto.CvcorpDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cvcorp} and its DTO {@link CvcorpDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CvcorpMapper extends EntityMapper<CvcorpDTO, Cvcorp> {


    @Mapping(target = "competences", ignore = true)
    @Mapping(target = "removeCompetence", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "removeExperience", ignore = true)
    @Mapping(target = "formations", ignore = true)
    @Mapping(target = "removeFormation", ignore = true)
    @Mapping(target = "langues", ignore = true)
    @Mapping(target = "removeLangue", ignore = true)
    @Mapping(target = "stages", ignore = true)
    @Mapping(target = "removeStage", ignore = true)
    Cvcorp toEntity(CvcorpDTO cvcorpDTO);

    default Cvcorp fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cvcorp cvcorp = new Cvcorp();
        cvcorp.setId(id);
        return cvcorp;
    }
}
