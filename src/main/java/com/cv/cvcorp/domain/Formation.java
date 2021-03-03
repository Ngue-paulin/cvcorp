package com.cv.cvcorp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Formation.
 */
@Entity
@Table(name = "formation")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "formation")
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "period_bigin")
    private Instant periodBigin;

    @Column(name = "period_end")
    private Instant periodEnd;

    @Column(name = "period_now")
    private Boolean periodNow;

    @Column(name = "diplome")
    private String diplome;

    @Column(name = "ville")
    private String ville;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = "formations", allowSetters = true)
    private Cvcorp cvcorp;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPeriodBigin() {
        return periodBigin;
    }

    public Formation periodBigin(Instant periodBigin) {
        this.periodBigin = periodBigin;
        return this;
    }

    public void setPeriodBigin(Instant periodBigin) {
        this.periodBigin = periodBigin;
    }

    public Instant getPeriodEnd() {
        return periodEnd;
    }

    public Formation periodEnd(Instant periodEnd) {
        this.periodEnd = periodEnd;
        return this;
    }

    public void setPeriodEnd(Instant periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Boolean isPeriodNow() {
        return periodNow;
    }

    public Formation periodNow(Boolean periodNow) {
        this.periodNow = periodNow;
        return this;
    }

    public void setPeriodNow(Boolean periodNow) {
        this.periodNow = periodNow;
    }

    public String getDiplome() {
        return diplome;
    }

    public Formation diplome(String diplome) {
        this.diplome = diplome;
        return this;
    }

    public void setDiplome(String diplome) {
        this.diplome = diplome;
    }

    public String getVille() {
        return ville;
    }

    public Formation ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDescription() {
        return description;
    }

    public Formation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Cvcorp getCvcorp() {
        return cvcorp;
    }

    public Formation cvcorp(Cvcorp cvcorp) {
        this.cvcorp = cvcorp;
        return this;
    }

    public void setCvcorp(Cvcorp cvcorp) {
        this.cvcorp = cvcorp;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formation)) {
            return false;
        }
        return id != null && id.equals(((Formation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Formation{" +
            "id=" + getId() +
            ", periodBigin='" + getPeriodBigin() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            ", periodNow='" + isPeriodNow() + "'" +
            ", diplome='" + getDiplome() + "'" +
            ", ville='" + getVille() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
