package com.cv.cvcorp.service.mapper;


import com.cv.cvcorp.domain.*;
import com.cv.cvcorp.service.dto.LangueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Langue} and its DTO {@link LangueDTO}.
 */
@Mapper(componentModel = "spring", uses = {CvcorpMapper.class})
public interface LangueMapper extends EntityMapper<LangueDTO, Langue> {

    @Mapping(source = "cvcorp.id", target = "cvcorpId")
    LangueDTO toDto(Langue langue);

    @Mapping(source = "cvcorpId", target = "cvcorp")
    Langue toEntity(LangueDTO langueDTO);

    default Langue fromId(Long id) {
        if (id == null) {
            return null;
        }
        Langue langue = new Langue();
        langue.setId(id);
        return langue;
    }
}
