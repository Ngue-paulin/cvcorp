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
 * Criteria class for the {@link com.cv.cvcorp.domain.Stage} entity. This class is used
 * in {@link com.cv.cvcorp.web.rest.StageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter periodBigin;

    private InstantFilter periodEnd;

    private BooleanFilter periodNow;

    private StringFilter ville;

    private StringFilter poste;

    private StringFilter employeur;

    private StringFilter description;

    private LongFilter cvcorpId;

    public StageCriteria() {
    }

    public StageCriteria(StageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.periodBigin = other.periodBigin == null ? null : other.periodBigin.copy();
        this.periodEnd = other.periodEnd == null ? null : other.periodEnd.copy();
        this.periodNow = other.periodNow == null ? null : other.periodNow.copy();
        this.ville = other.ville == null ? null : other.ville.copy();
        this.poste = other.poste == null ? null : other.poste.copy();
        this.employeur = other.employeur == null ? null : other.employeur.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.cvcorpId = other.cvcorpId == null ? null : other.cvcorpId.copy();
    }

    @Override
    public StageCriteria copy() {
        return new StageCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getPeriodBigin() {
        return periodBigin;
    }

    public void setPeriodBigin(InstantFilter periodBigin) {
        this.periodBigin = periodBigin;
    }

    public InstantFilter getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(InstantFilter periodEnd) {
        this.periodEnd = periodEnd;
    }

    public BooleanFilter getPeriodNow() {
        return periodNow;
    }

    public void setPeriodNow(BooleanFilter periodNow) {
        this.periodNow = periodNow;
    }

    public StringFilter getVille() {
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
    }

    public StringFilter getPoste() {
        return poste;
    }

    public void setPoste(StringFilter poste) {
        this.poste = poste;
    }

    public StringFilter getEmployeur() {
        return employeur;
    }

    public void setEmployeur(StringFilter employeur) {
        this.employeur = employeur;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getCvcorpId() {
        return cvcorpId;
    }

    public void setCvcorpId(LongFilter cvcorpId) {
        this.cvcorpId = cvcorpId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StageCriteria that = (StageCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(periodBigin, that.periodBigin) &&
            Objects.equals(periodEnd, that.periodEnd) &&
            Objects.equals(periodNow, that.periodNow) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(poste, that.poste) &&
            Objects.equals(employeur, that.employeur) &&
            Objects.equals(description, that.description) &&
            Objects.equals(cvcorpId, that.cvcorpId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        periodBigin,
        periodEnd,
        periodNow,
        ville,
        poste,
        employeur,
        description,
        cvcorpId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (periodBigin != null ? "periodBigin=" + periodBigin + ", " : "") +
                (periodEnd != null ? "periodEnd=" + periodEnd + ", " : "") +
                (periodNow != null ? "periodNow=" + periodNow + ", " : "") +
                (ville != null ? "ville=" + ville + ", " : "") +
                (poste != null ? "poste=" + poste + ", " : "") +
                (employeur != null ? "employeur=" + employeur + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (cvcorpId != null ? "cvcorpId=" + cvcorpId + ", " : "") +
            "}";
    }

}
