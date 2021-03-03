package com.cv.cvcorp.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.cv.cvcorp.domain.Stage} entity.
 */
public class StageDTO implements Serializable {
    
    private Long id;

    private Instant periodBigin;

    private Instant periodEnd;

    private Boolean periodNow;

    private String ville;

    private String poste;

    private String employeur;

    private String description;


    private Long cvcorpId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPeriodBigin() {
        return periodBigin;
    }

    public void setPeriodBigin(Instant periodBigin) {
        this.periodBigin = periodBigin;
    }

    public Instant getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Instant periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Boolean isPeriodNow() {
        return periodNow;
    }

    public void setPeriodNow(Boolean periodNow) {
        this.periodNow = periodNow;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getEmployeur() {
        return employeur;
    }

    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof StageDTO)) {
            return false;
        }

        return id != null && id.equals(((StageDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StageDTO{" +
            "id=" + getId() +
            ", periodBigin='" + getPeriodBigin() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            ", periodNow='" + isPeriodNow() + "'" +
            ", ville='" + getVille() + "'" +
            ", poste='" + getPoste() + "'" +
            ", employeur='" + getEmployeur() + "'" +
            ", description='" + getDescription() + "'" +
            ", cvcorpId=" + getCvcorpId() +
            "}";
    }
}
