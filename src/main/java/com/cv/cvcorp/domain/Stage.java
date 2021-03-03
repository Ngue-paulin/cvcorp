package com.cv.cvcorp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Stage.
 */
@Entity
@Table(name = "stage")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "stage")
public class Stage implements Serializable {

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

    @Column(name = "ville")
    private String ville;

    @Column(name = "poste")
    private String poste;

    @Column(name = "employeur")
    private String employeur;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = "stages", allowSetters = true)
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

    public Stage periodBigin(Instant periodBigin) {
        this.periodBigin = periodBigin;
        return this;
    }

    public void setPeriodBigin(Instant periodBigin) {
        this.periodBigin = periodBigin;
    }

    public Instant getPeriodEnd() {
        return periodEnd;
    }

    public Stage periodEnd(Instant periodEnd) {
        this.periodEnd = periodEnd;
        return this;
    }

    public void setPeriodEnd(Instant periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Boolean isPeriodNow() {
        return periodNow;
    }

    public Stage periodNow(Boolean periodNow) {
        this.periodNow = periodNow;
        return this;
    }

    public void setPeriodNow(Boolean periodNow) {
        this.periodNow = periodNow;
    }

    public String getVille() {
        return ville;
    }

    public Stage ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPoste() {
        return poste;
    }

    public Stage poste(String poste) {
        this.poste = poste;
        return this;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getEmployeur() {
        return employeur;
    }

    public Stage employeur(String employeur) {
        this.employeur = employeur;
        return this;
    }

    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

    public String getDescription() {
        return description;
    }

    public Stage description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Cvcorp getCvcorp() {
        return cvcorp;
    }

    public Stage cvcorp(Cvcorp cvcorp) {
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
        if (!(o instanceof Stage)) {
            return false;
        }
        return id != null && id.equals(((Stage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stage{" +
            "id=" + getId() +
            ", periodBigin='" + getPeriodBigin() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            ", periodNow='" + isPeriodNow() + "'" +
            ", ville='" + getVille() + "'" +
            ", poste='" + getPoste() + "'" +
            ", employeur='" + getEmployeur() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
