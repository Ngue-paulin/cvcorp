package com.cv.cvcorp.service.mapper;


import com.cv.cvcorp.domain.*;
import com.cv.cvcorp.service.dto.ExperienceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Experience} and its DTO {@link ExperienceDTO}.
 */
@Mapper(componentModel = "spring", uses = {CvcorpMapper.class})
public interface ExperienceMapper extends EntityMapper<ExperienceDTO, Experience> {

    @Mapping(source = "cvcorp.id", target = "cvcorpId")
    ExperienceDTO toDto(Experience experience);

    @Mapping(source = "cvcorpId", target = "cvcorp")
    Experience toEntity(ExperienceDTO experienceDTO);

    default Experience fromId(Long id) {
        if (id == null) {
            return null;
        }
        Experience experience = new Experience();
        experience.setId(id);
        return experience;
    }
}
