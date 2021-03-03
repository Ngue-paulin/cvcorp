package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.CvcorpApp;
import com.cv.cvcorp.config.SecurityBeanOverrideConfiguration;
import com.cv.cvcorp.domain.Competence;
import com.cv.cvcorp.domain.Cvcorp;
import com.cv.cvcorp.repository.CompetenceRepository;
import com.cv.cvcorp.repository.search.CompetenceSearchRepository;
import com.cv.cvcorp.service.CompetenceService;
import com.cv.cvcorp.service.dto.CompetenceDTO;
import com.cv.cvcorp.service.mapper.CompetenceMapper;
import com.cv.cvcorp.service.dto.CompetenceCriteria;
import com.cv.cvcorp.service.CompetenceQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cv.cvcorp.domain.enumeration.Niveau;
/**
 * Integration tests for the {@link CompetenceResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, CvcorpApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CompetenceResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final Niveau DEFAULT_NIVEAU = Niveau.DEBUTANT;
    private static final Niveau UPDATED_NIVEAU = Niveau.INTERMEDIAIR;

    @Autowired
    private CompetenceRepository competenceRepository;

    @Autowired
    private CompetenceMapper competenceMapper;

    @Autowired
    private CompetenceService competenceService;

    /**
     * This repository is mocked in the com.cv.cvcorp.repository.search test package.
     *
     * @see com.cv.cvcorp.repository.search.CompetenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private CompetenceSearchRepository mockCompetenceSearchRepository;

    @Autowired
    private CompetenceQueryService competenceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompetenceMockMvc;

    private Competence competence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competence createEntity(EntityManager em) {
        Competence competence = new Competence()
            .description(DEFAULT_DESCRIPTION)
            .libele(DEFAULT_LIBELE)
            .niveau(DEFAULT_NIVEAU);
        return competence;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competence createUpdatedEntity(EntityManager em) {
        Competence competence = new Competence()
            .description(UPDATED_DESCRIPTION)
            .libele(UPDATED_LIBELE)
            .niveau(UPDATED_NIVEAU);
        return competence;
    }

    @BeforeEach
    public void initTest() {
        competence = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompetence() throws Exception {
        int databaseSizeBeforeCreate = competenceRepository.findAll().size();
        // Create the Competence
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);
        restCompetenceMockMvc.perform(post("/api/competences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(competenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeCreate + 1);
        Competence testCompetence = competenceList.get(competenceList.size() - 1);
        assertThat(testCompetence.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCompetence.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testCompetence.getNiveau()).isEqualTo(DEFAULT_NIVEAU);

        // Validate the Competence in Elasticsearch
        verify(mockCompetenceSearchRepository, times(1)).save(testCompetence);
    }

    @Test
    @Transactional
    public void createCompetenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = competenceRepository.findAll().size();

        // Create the Competence with an existing ID
        competence.setId(1L);
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetenceMockMvc.perform(post("/api/competences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(competenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Competence in Elasticsearch
        verify(mockCompetenceSearchRepository, times(0)).save(competence);
    }


    @Test
    @Transactional
    public void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = competenceRepository.findAll().size();
        // set the field null
        competence.setLibele(null);

        // Create the Competence, which fails.
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);


        restCompetenceMockMvc.perform(post("/api/competences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(competenceDTO)))
            .andExpect(status().isBadRequest());

        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompetences() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList
        restCompetenceMockMvc.perform(get("/api/competences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competence.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())));
    }
    
    @Test
    @Transactional
    public void getCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get the competence
        restCompetenceMockMvc.perform(get("/api/competences/{id}", competence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(competence.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU.toString()));
    }


    @Test
    @Transactional
    public void getCompetencesByIdFiltering() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        Long id = competence.getId();

        defaultCompetenceShouldBeFound("id.equals=" + id);
        defaultCompetenceShouldNotBeFound("id.notEquals=" + id);

        defaultCompetenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompetenceShouldNotBeFound("id.greaterThan=" + id);

        defaultCompetenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompetenceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCompetencesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where description equals to DEFAULT_DESCRIPTION
        defaultCompetenceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the competenceList where description equals to UPDATED_DESCRIPTION
        defaultCompetenceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCompetencesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where description not equals to DEFAULT_DESCRIPTION
        defaultCompetenceShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the competenceList where description not equals to UPDATED_DESCRIPTION
        defaultCompetenceShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCompetencesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCompetenceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the competenceList where description equals to UPDATED_DESCRIPTION
        defaultCompetenceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCompetencesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where description is not null
        defaultCompetenceShouldBeFound("description.specified=true");

        // Get all the competenceList where description is null
        defaultCompetenceShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompetencesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where description contains DEFAULT_DESCRIPTION
        defaultCompetenceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the competenceList where description contains UPDATED_DESCRIPTION
        defaultCompetenceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCompetencesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where description does not contain DEFAULT_DESCRIPTION
        defaultCompetenceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the competenceList where description does not contain UPDATED_DESCRIPTION
        defaultCompetenceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCompetencesByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where libele equals to DEFAULT_LIBELE
        defaultCompetenceShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the competenceList where libele equals to UPDATED_LIBELE
        defaultCompetenceShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllCompetencesByLibeleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where libele not equals to DEFAULT_LIBELE
        defaultCompetenceShouldNotBeFound("libele.notEquals=" + DEFAULT_LIBELE);

        // Get all the competenceList where libele not equals to UPDATED_LIBELE
        defaultCompetenceShouldBeFound("libele.notEquals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllCompetencesByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultCompetenceShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the competenceList where libele equals to UPDATED_LIBELE
        defaultCompetenceShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllCompetencesByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where libele is not null
        defaultCompetenceShouldBeFound("libele.specified=true");

        // Get all the competenceList where libele is null
        defaultCompetenceShouldNotBeFound("libele.specified=false");
    }
                @Test
    @Transactional
    public void getAllCompetencesByLibeleContainsSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where libele contains DEFAULT_LIBELE
        defaultCompetenceShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the competenceList where libele contains UPDATED_LIBELE
        defaultCompetenceShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllCompetencesByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where libele does not contain DEFAULT_LIBELE
        defaultCompetenceShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the competenceList where libele does not contain UPDATED_LIBELE
        defaultCompetenceShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }


    @Test
    @Transactional
    public void getAllCompetencesByNiveauIsEqualToSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where niveau equals to DEFAULT_NIVEAU
        defaultCompetenceShouldBeFound("niveau.equals=" + DEFAULT_NIVEAU);

        // Get all the competenceList where niveau equals to UPDATED_NIVEAU
        defaultCompetenceShouldNotBeFound("niveau.equals=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    public void getAllCompetencesByNiveauIsNotEqualToSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where niveau not equals to DEFAULT_NIVEAU
        defaultCompetenceShouldNotBeFound("niveau.notEquals=" + DEFAULT_NIVEAU);

        // Get all the competenceList where niveau not equals to UPDATED_NIVEAU
        defaultCompetenceShouldBeFound("niveau.notEquals=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    public void getAllCompetencesByNiveauIsInShouldWork() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where niveau in DEFAULT_NIVEAU or UPDATED_NIVEAU
        defaultCompetenceShouldBeFound("niveau.in=" + DEFAULT_NIVEAU + "," + UPDATED_NIVEAU);

        // Get all the competenceList where niveau equals to UPDATED_NIVEAU
        defaultCompetenceShouldNotBeFound("niveau.in=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    public void getAllCompetencesByNiveauIsNullOrNotNull() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competenceList where niveau is not null
        defaultCompetenceShouldBeFound("niveau.specified=true");

        // Get all the competenceList where niveau is null
        defaultCompetenceShouldNotBeFound("niveau.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompetencesByCvcorpIsEqualToSomething() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);
        Cvcorp cvcorp = CvcorpResourceIT.createEntity(em);
        em.persist(cvcorp);
        em.flush();
        competence.setCvcorp(cvcorp);
        competenceRepository.saveAndFlush(competence);
        Long cvcorpId = cvcorp.getId();

        // Get all the competenceList where cvcorp equals to cvcorpId
        defaultCompetenceShouldBeFound("cvcorpId.equals=" + cvcorpId);

        // Get all the competenceList where cvcorp equals to cvcorpId + 1
        defaultCompetenceShouldNotBeFound("cvcorpId.equals=" + (cvcorpId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompetenceShouldBeFound(String filter) throws Exception {
        restCompetenceMockMvc.perform(get("/api/competences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competence.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())));

        // Check, that the count call also returns 1
        restCompetenceMockMvc.perform(get("/api/competences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompetenceShouldNotBeFound(String filter) throws Exception {
        restCompetenceMockMvc.perform(get("/api/competences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompetenceMockMvc.perform(get("/api/competences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCompetence() throws Exception {
        // Get the competence
        restCompetenceMockMvc.perform(get("/api/competences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();

        // Update the competence
        Competence updatedCompetence = competenceRepository.findById(competence.getId()).get();
        // Disconnect from session so that the updates on updatedCompetence are not directly saved in db
        em.detach(updatedCompetence);
        updatedCompetence
            .description(UPDATED_DESCRIPTION)
            .libele(UPDATED_LIBELE)
            .niveau(UPDATED_NIVEAU);
        CompetenceDTO competenceDTO = competenceMapper.toDto(updatedCompetence);

        restCompetenceMockMvc.perform(put("/api/competences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(competenceDTO)))
            .andExpect(status().isOk());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);
        Competence testCompetence = competenceList.get(competenceList.size() - 1);
        assertThat(testCompetence.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCompetence.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testCompetence.getNiveau()).isEqualTo(UPDATED_NIVEAU);

        // Validate the Competence in Elasticsearch
        verify(mockCompetenceSearchRepository, times(1)).save(testCompetence);
    }

    @Test
    @Transactional
    public void updateNonExistingCompetence() throws Exception {
        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();

        // Create the Competence
        CompetenceDTO competenceDTO = competenceMapper.toDto(competence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetenceMockMvc.perform(put("/api/competences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(competenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Competence in the database
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Competence in Elasticsearch
        verify(mockCompetenceSearchRepository, times(0)).save(competence);
    }

    @Test
    @Transactional
    public void deleteCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        int databaseSizeBeforeDelete = competenceRepository.findAll().size();

        // Delete the competence
        restCompetenceMockMvc.perform(delete("/api/competences/{id}", competence.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Competence> competenceList = competenceRepository.findAll();
        assertThat(competenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Competence in Elasticsearch
        verify(mockCompetenceSearchRepository, times(1)).deleteById(competence.getId());
    }

    @Test
    @Transactional
    public void searchCompetence() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        competenceRepository.saveAndFlush(competence);
        when(mockCompetenceSearchRepository.search(queryStringQuery("id:" + competence.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(competence), PageRequest.of(0, 1), 1));

        // Search the competence
        restCompetenceMockMvc.perform(get("/api/_search/competences?query=id:" + competence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competence.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())));
    }
}
