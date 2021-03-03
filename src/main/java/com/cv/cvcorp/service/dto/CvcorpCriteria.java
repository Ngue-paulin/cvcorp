package com.cv.cvcorp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.cv.cvcorp.domain.Cvcorp} entity. This class is used
 * in {@link com.cv.cvcorp.web.rest.CvcorpResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cvcorps?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CvcorpCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter profil;

    private StringFilter pays;

    private StringFilter ville;

    private InstantFilter dateNaissence;

    private StringFilter naissanceLieu;

    private StringFilter etatCivil;

    private StringFilter linkedId;

    private StringFilter adresse;

    private StringFilter sexe;

    private StringFilter codePostal;

    private LongFilter competenceId;

    private LongFilter experienceId;

    private LongFilter formationId;

    private LongFilter langueId;

    private LongFilter stageId;

    public CvcorpCriteria() {
    }

    public CvcorpCriteria(CvcorpCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.profil = other.profil == null ? null : other.profil.copy();
        this.pays = other.pays == null ? null : other.pays.copy();
        this.ville = other.ville == null ? null : other.ville.copy();
        this.dateNaissence = other.dateNaissence == null ? null : other.dateNaissence.copy();
        this.naissanceLieu = other.naissanceLieu == null ? null : other.naissanceLieu.copy();
        this.etatCivil = other.etatCivil == null ? null : other.etatCivil.copy();
        this.linkedId = other.linkedId == null ? null : other.linkedId.copy();
        this.adresse = other.adresse == null ? null : other.adresse.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.codePostal = other.codePostal == null ? null : other.codePostal.copy();
        this.competenceId = other.competenceId == null ? null : other.competenceId.copy();
        this.experienceId = other.experienceId == null ? null : other.experienceId.copy();
        this.formationId = other.formationId == null ? null : other.formationId.copy();
        this.langueId = other.langueId == null ? null : other.langueId.copy();
        this.stageId = other.stageId == null ? null : other.stageId.copy();
    }

    @Override
    public CvcorpCriteria copy() {
        return new CvcorpCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProfil() {
        return profil;
    }

    public void setProfil(StringFilter profil) {
        this.profil = profil;
    }

    public StringFilter getPays() {
        return pays;
    }

    public void setPays(StringFilter pays) {
        this.pays = pays;
    }

    public StringFilter getVille() {
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
    }

    public InstantFilter getDateNaissence() {
        return dateNaissence;
    }

    public void setDateNaissence(InstantFilter dateNaissence) {
        this.dateNaissence = dateNaissence;
    }

    public StringFilter getNaissanceLieu() {
        return naissanceLieu;
    }

    public void setNaissanceLieu(StringFilter naissanceLieu) {
        this.naissanceLieu = naissanceLieu;
    }

    public StringFilter getEtatCivil() {
        return etatCivil;
    }

    public void setEtatCivil(StringFilter etatCivil) {
        this.etatCivil = etatCivil;
    }

    public StringFilter getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(StringFilter linkedId) {
        this.linkedId = linkedId;
    }

    public StringFilter getAdresse() {
        return adresse;
    }

    public void setAdresse(StringFilter adresse) {
        this.adresse = adresse;
    }

    public StringFilter getSexe() {
        return sexe;
    }

    public void setSexe(StringFilter sexe) {
        this.sexe = sexe;
    }

    public StringFilter getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(StringFilter codePostal) {
        this.codePostal = codePostal;
    }

    public LongFilter getCompetenceId() {
        return competenceId;
    }

    public void setCompetenceId(LongFilter competenceId) {
        this.competenceId = competenceId;
    }

    public LongFilter getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(LongFilter experienceId) {
        this.experienceId = experienceId;
    }

    public LongFilter getFormationId() {
        return formationId;
    }

    public void setFormationId(LongFilter formationId) {
        this.formationId = formationId;
    }

    public LongFilter getLangueId() {
        return langueId;
    }

    public void setLangueId(LongFilter langueId) {
        this.langueId = langueId;
    }

    public LongFilter getStageId() {
        return stageId;
    }

    public void setStageId(LongFilter stageId) {
        this.stageId = stageId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CvcorpCriteria that = (CvcorpCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(profil, that.profil) &&
            Objects.equals(pays, that.pays) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(dateNaissence, that.dateNaissence) &&
            Objects.equals(naissanceLieu, that.naissanceLieu) &&
            Objects.equals(etatCivil, that.etatCivil) &&
            Objects.equals(linkedId, that.linkedId) &&
            Objects.equals(adresse, that.adresse) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(codePostal, that.codePostal) &&
            Objects.equals(competenceId, that.competenceId) &&
            Objects.equals(experienceId, that.experienceId) &&
            Objects.equals(formationId, that.formationId) &&
            Objects.equals(langueId, that.langueId) &&
            Objects.equals(stageId, that.stageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        profil,
        pays,
        ville,
        dateNaissence,
        naissanceLieu,
        etatCivil,
        linkedId,
        adresse,
        sexe,
        codePostal,
        competenceId,
        experienceId,
        formationId,
        langueId,
        stageId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CvcorpCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (profil != null ? "profil=" + profil + ", " : "") +
                (pays != null ? "pays=" + pays + ", " : "") +
                (ville != null ? "ville=" + ville + ", " : "") +
                (dateNaissence != null ? "dateNaissence=" + dateNaissence + ", " : "") +
                (naissanceLieu != null ? "naissanceLieu=" + naissanceLieu + ", " : "") +
                (etatCivil != null ? "etatCivil=" + etatCivil + ", " : "") +
                (linkedId != null ? "linkedId=" + linkedId + ", " : "") +
                (adresse != null ? "adresse=" + adresse + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
                (codePostal != null ? "codePostal=" + codePostal + ", " : "") +
                (competenceId != null ? "competenceId=" + competenceId + ", " : "") +
                (experienceId != null ? "experienceId=" + experienceId + ", " : "") +
                (formationId != null ? "formationId=" + formationId + ", " : "") +
                (langueId != null ? "langueId=" + langueId + ", " : "") +
                (stageId != null ? "stageId=" + stageId + ", " : "") +
            "}";
    }

}
