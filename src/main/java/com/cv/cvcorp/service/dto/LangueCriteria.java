package com.cv.cvcorp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.cv.cvcorp.domain.enumeration.Niveau;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.cv.cvcorp.domain.Langue} entity. This class is used
 * in {@link com.cv.cvcorp.web.rest.LangueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /langues?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LangueCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Niveau
     */
    public static class NiveauFilter extends Filter<Niveau> {

        public NiveauFilter() {
        }

        public NiveauFilter(NiveauFilter filter) {
            super(filter);
        }

        @Override
        public NiveauFilter copy() {
            return new NiveauFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter libele;

    private NiveauFilter niveau;

    private LongFilter cvcorpId;

    public LangueCriteria() {
    }

    public LangueCriteria(LangueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.niveau = other.niveau == null ? null : other.niveau.copy();
        this.cvcorpId = other.cvcorpId == null ? null : other.cvcorpId.copy();
    }

    @Override
    public LangueCriteria copy() {
        return new LangueCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getLibele() {
        return libele;
    }

    public void setLibele(StringFilter libele) {
        this.libele = libele;
    }

    public NiveauFilter getNiveau() {
        return niveau;
    }

    public void setNiveau(NiveauFilter niveau) {
        this.niveau = niveau;
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
        final LangueCriteria that = (LangueCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(niveau, that.niveau) &&
            Objects.equals(cvcorpId, that.cvcorpId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        description,
        libele,
        niveau,
        cvcorpId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LangueCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (libele != null ? "libele=" + libele + ", " : "") +
                (niveau != null ? "niveau=" + niveau + ", " : "") +
                (cvcorpId != null ? "cvcorpId=" + cvcorpId + ", " : "") +
            "}";
    }

}
