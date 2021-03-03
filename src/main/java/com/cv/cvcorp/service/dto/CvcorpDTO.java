package com.cv.cvcorp.service.dto;

import io.swagger.annotations.ApiModel;
import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.cv.cvcorp.domain.Cvcorp} entity.
 */
@ApiModel(description = "LES ENTITES")
public class CvcorpDTO implements Serializable {
    
    private Long id;

    private String profil;

    private String pays;

    private String ville;

    private Instant dateNaissence;

    private String naissanceLieu;

    private String etatCivil;

    private String linkedId;

    private String adresse;

    private String sexe;

    private String codePostal;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Instant getDateNaissence() {
        return dateNaissence;
    }

    public void setDateNaissence(Instant dateNaissence) {
        this.dateNaissence = dateNaissence;
    }

    public String getNaissanceLieu() {
        return naissanceLieu;
    }

    public void setNaissanceLieu(String naissanceLieu) {
        this.naissanceLieu = naissanceLieu;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CvcorpDTO)) {
            return false;
        }

        return id != null && id.equals(((CvcorpDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CvcorpDTO{" +
            "id=" + getId() +
            ", profil='" + getProfil() + "'" +
            ", pays='" + getPays() + "'" +
            ", ville='" + getVille() + "'" +
            ", dateNaissence='" + getDateNaissence() + "'" +
            ", naissanceLieu='" + getNaissanceLieu() + "'" +
            ", etatCivil='" + getEtatCivil() + "'" +
            ", linkedId='" + getLinkedId() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            "}";
    }
}
