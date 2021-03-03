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
 * Criteria class for the {@link com.cv.cvcorp.domain.Formation} entity. This class is used
 * in {@link com.cv.cvcorp.web.rest.FormationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /formations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FormationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter periodBigin;

    private InstantFilter periodEnd;

    private BooleanFilter periodNow;

    private StringFilter diplome;

    private StringFilter ville;

    private StringFilter description;

    private LongFilter cvcorpId;

    public FormationCriteria() {
    }

    public FormationCriteria(FormationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.periodBigin = other.periodBigin == null ? null : other.periodBigin.copy();
        this.periodEnd = other.periodEnd == null ? null : other.periodEnd.copy();
        this.periodNow = other.periodNow == null ? null : other.periodNow.copy();
        this.diplome = other.diplome == null ? null : other.diplome.copy();
        this.ville = other.ville == null ? null : other.ville.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.cvcorpId = other.cvcorpId == null ? null : other.cvcorpId.copy();
    }

    @Override
    public FormationCriteria copy() {
        return new FormationCriteria(this);
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

    public StringFilter getDiplome() {
        return diplome;
    }

    public void setDiplome(StringFilter diplome) {
        this.diplome = diplome;
    }

    public StringFilter getVille() {
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
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
        final FormationCriteria that = (FormationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(periodBigin, that.periodBigin) &&
            Objects.equals(periodEnd, that.periodEnd) &&
            Objects.equals(periodNow, that.periodNow) &&
            Objects.equals(diplome, that.diplome) &&
            Objects.equals(ville, that.ville) &&
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
        diplome,
        ville,
        description,
        cvcorpId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (periodBigin != null ? "periodBigin=" + periodBigin + ", " : "") +
                (periodEnd != null ? "periodEnd=" + periodEnd + ", " : "") +
                (periodNow != null ? "periodNow=" + periodNow + ", " : "") +
                (diplome != null ? "diplome=" + diplome + ", " : "") +
                (ville != null ? "ville=" + ville + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (cvcorpId != null ? "cvcorpId=" + cvcorpId + ", " : "") +
            "}";
    }

}
