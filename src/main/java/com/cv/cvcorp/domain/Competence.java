package com.cv.cvcorp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import com.cv.cvcorp.domain.enumeration.Niveau;

/**
 * A Competence.
 */
@Entity
@Table(name = "competence")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "competence")
public class Competence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "libele", nullable = false)
    private String libele;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private Niveau niveau;

    @ManyToOne
    @JsonIgnoreProperties(value = "competences", allowSetters = true)
    private Cvcorp cvcorp;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Competence description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLibele() {
        return libele;
    }

    public Competence libele(String libele) {
        this.libele = libele;
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public Competence niveau(Niveau niveau) {
        this.niveau = niveau;
        return this;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Cvcorp getCvcorp() {
        return cvcorp;
    }

    public Competence cvcorp(Cvcorp cvcorp) {
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
        if (!(o instanceof Competence)) {
            return false;
        }
        return id != null && id.equals(((Competence) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Competence{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", libele='" + getLibele() + "'" +
            ", niveau='" + getNiveau() + "'" +
            "}";
    }
}
