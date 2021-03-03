package com.cv.cvcorp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import com.cv.cvcorp.domain.enumeration.Niveau;

/**
 * A DTO for the {@link com.cv.cvcorp.domain.Competence} entity.
 */
public class CompetenceDTO implements Serializable {
    
    private Long id;

    private String description;

    @NotNull
    private String libele;

    private Niveau niveau;


    private Long cvcorpId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Long getCvcorpId() {
        return cvcorpId;
    }

    public void setCvcorpId(Long cvcorpId) {
        this.cvcorpId = cvcorpId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetenceDTO)) {
            return false;
        }

        return id != null && id.equals(((CompetenceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetenceDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", libele='" + getLibele() + "'" +
            ", niveau='" + getNiveau() + "'" +
            ", cvcorpId=" + getCvcorpId() +
            "}";
    }
}
