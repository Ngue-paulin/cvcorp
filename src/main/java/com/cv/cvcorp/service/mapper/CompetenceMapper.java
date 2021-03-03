package com.cv.cvcorp.service.mapper;


import com.cv.cvcorp.domain.*;
import com.cv.cvcorp.service.dto.CompetenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Competence} and its DTO {@link CompetenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {CvcorpMapper.class})
public interface CompetenceMapper extends EntityMapper<CompetenceDTO, Competence> {

    @Mapping(source = "cvcorp.id", target = "cvcorpId")
    CompetenceDTO toDto(Competence competence);

    @Mapping(source = "cvcorpId", target = "cvcorp")
    Competence toEntity(CompetenceDTO competenceDTO);

    default Competence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Competence competence = new Competence();
        competence.setId(id);
        return competence;
    }
}
