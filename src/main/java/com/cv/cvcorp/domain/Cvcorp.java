package com.cv.cvcorp.domain;


import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * LES ENTITES
 */
@Entity
@Table(name = "cvcorp")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cvcorp")
public class Cvcorp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profil")
    private String profil;

    @Column(name = "pays")
    private String pays;

    @Column(name = "ville")
    private String ville;

    @Column(name = "date_naissence")
    private Instant dateNaissence;

    @Column(name = "naissance_lieu")
    private String naissanceLieu;

    @Column(name = "etat_civil")
    private String etatCivil;

    @Column(name = "linked_id")
    private String linkedId;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "code_postal")
    private String codePostal;

    @OneToMany(mappedBy = "cvcorp")
    private Set<Competence> competences = new HashSet<>();

    @OneToMany(mappedBy = "cvcorp")
    private Set<Experience> experiences = new HashSet<>();

    /**
     * formations du client
     */
    @OneToMany(mappedBy = "cvcorp")
    private Set<Formation> formations = new HashSet<>();

    @OneToMany(mappedBy = "cvcorp")
    private Set<Langue> langues = new HashSet<>();

    @OneToMany(mappedBy = "cvcorp")
    private Set<Stage> stages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfil() {
        return profil;
    }

    public Cvcorp profil(String profil) {
        this.profil = profil;
        return this;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getPays() {
        return pays;
    }

    public Cvcorp pays(String pays) {
        this.pays = pays;
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public Cvcorp ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Instant getDateNaissence() {
        return dateNaissence;
    }

    public Cvcorp dateNaissence(Instant dateNaissence) {
        this.dateNaissence = dateNaissence;
        return this;
    }

    public void setDateNaissence(Instant dateNaissence) {
        this.dateNaissence = dateNaissence;
    }

    public String getNaissanceLieu() {
        return naissanceLieu;
    }

    public Cvcorp naissanceLieu(String naissanceLieu) {
        this.naissanceLieu = naissanceLieu;
        return this;
    }

    public void setNaissanceLieu(String naissanceLieu) {
        this.naissanceLieu = naissanceLieu;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    public Cvcorp etatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
        return this;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public Cvcorp linkedId(String linkedId) {
        this.linkedId = linkedId;
        return this;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }

    public String getAdresse() {
        return adresse;
    }

    public Cvcorp adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSexe() {
        return sexe;
    }

    public Cvcorp sexe(String sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public Cvcorp codePostal(String codePostal) {
        this.codePostal = codePostal;
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public Set<Competence> getCompetences() {
        return competences;
    }

    public Cvcorp competences(Set<Competence> competences) {
        this.competences = competences;
        return this;
    }

    public Cvcorp addCompetence(Competence competence) {
        this.competences.add(competence);
        competence.setCvcorp(this);
        return this;
    }

    public Cvcorp removeCompetence(Competence competence) {
        this.competences.remove(competence);
        competence.setCvcorp(null);
        return this;
    }

    public void setCompetences(Set<Competence> competences) {
        this.competences = competences;
    }

    public Set<Experience> getExperiences() {
        return experiences;
    }

    public Cvcorp experiences(Set<Experience> experiences) {
        this.experiences = experiences;
        return this;
    }

    public Cvcorp addExperience(Experience experience) {
        this.experiences.add(experience);
        experience.setCvcorp(this);
        return this;
    }

    public Cvcorp removeExperience(Experience experience) {
        this.experiences.remove(experience);
        experience.setCvcorp(null);
        return this;
    }

    public void setExperiences(Set<Experience> experiences) {
        this.experiences = experiences;
    }

    public Set<Formation> getFormations() {
        return formations;
    }

    public Cvcorp formations(Set<Formation> formations) {
        this.formations = formations;
        return this;
    }

    public Cvcorp addFormation(Formation formation) {
        this.formations.add(formation);
        formation.setCvcorp(this);
        return this;
    }

    public Cvcorp removeFormation(Formation formation) {
        this.formations.remove(formation);
        formation.setCvcorp(null);
        return this;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }

    public Set<Langue> getLangues() {
        return langues;
    }

    public Cvcorp langues(Set<Langue> langues) {
        this.langues = langues;
        return this;
    }

    public Cvcorp addLangue(Langue langue) {
        this.langues.add(langue);
        langue.setCvcorp(this);
        return this;
    }

    public Cvcorp removeLangue(Langue langue) {
        this.langues.remove(langue);
        langue.setCvcorp(null);
        return this;
    }

    public void setLangues(Set<Langue> langues) {
        this.langues = langues;
    }

    public Set<Stage> getStages() {
        return stages;
    }

    public Cvcorp stages(Set<Stage> stages) {
        this.stages = stages;
        return this;
    }

    public Cvcorp addStage(Stage stage) {
        this.stages.add(stage);
        stage.setCvcorp(this);
        return this;
    }

    public Cvcorp removeStage(Stage stage) {
        this.stages.remove(stage);
        stage.setCvcorp(null);
        return this;
    }

    public void setStages(Set<Stage> stages) {
        this.stages = stages;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cvcorp)) {
            return false;
        }
        return id != null && id.equals(((Cvcorp) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cvcorp{" +
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
