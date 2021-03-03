package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.CvcorpApp;
import com.cv.cvcorp.config.SecurityBeanOverrideConfiguration;
import com.cv.cvcorp.domain.Formation;
import com.cv.cvcorp.domain.Cvcorp;
import com.cv.cvcorp.repository.FormationRepository;
import com.cv.cvcorp.repository.search.FormationSearchRepository;
import com.cv.cvcorp.service.FormationService;
import com.cv.cvcorp.service.dto.FormationDTO;
import com.cv.cvcorp.service.mapper.FormationMapper;
import com.cv.cvcorp.service.dto.FormationCriteria;
import com.cv.cvcorp.service.FormationQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FormationResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, CvcorpApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FormationResourceIT {

    private static final Instant DEFAULT_PERIOD_BIGIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PERIOD_BIGIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PERIOD_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PERIOD_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PERIOD_NOW = false;
    private static final Boolean UPDATED_PERIOD_NOW = true;

    private static final String DEFAULT_DIPLOME = "AAAAAAAAAA";
    private static final String UPDATED_DIPLOME = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationMapper formationMapper;

    @Autowired
    private FormationService formationService;

    /**
     * This repository is mocked in the com.cv.cvcorp.repository.search test package.
     *
     * @see com.cv.cvcorp.repository.search.FormationSearchRepositoryMockConfiguration
     */
    @Autowired
    private FormationSearchRepository mockFormationSearchRepository;

    @Autowired
    private FormationQueryService formationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationMockMvc;

    private Formation formation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createEntity(EntityManager em) {
        Formation formation = new Formation()
            .periodBigin(DEFAULT_PERIOD_BIGIN)
            .periodEnd(DEFAULT_PERIOD_END)
            .periodNow(DEFAULT_PERIOD_NOW)
            .diplome(DEFAULT_DIPLOME)
            .ville(DEFAULT_VILLE)
            .description(DEFAULT_DESCRIPTION);
        return formation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createUpdatedEntity(EntityManager em) {
        Formation formation = new Formation()
            .periodBigin(UPDATED_PERIOD_BIGIN)
            .periodEnd(UPDATED_PERIOD_END)
            .periodNow(UPDATED_PERIOD_NOW)
            .diplome(UPDATED_DIPLOME)
            .ville(UPDATED_VILLE)
            .description(UPDATED_DESCRIPTION);
        return formation;
    }

    @BeforeEach
    public void initTest() {
        formation = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();
        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);
        restFormationMockMvc.perform(post("/api/formations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate + 1);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getPeriodBigin()).isEqualTo(DEFAULT_PERIOD_BIGIN);
        assertThat(testFormation.getPeriodEnd()).isEqualTo(DEFAULT_PERIOD_END);
        assertThat(testFormation.isPeriodNow()).isEqualTo(DEFAULT_PERIOD_NOW);
        assertThat(testFormation.getDiplome()).isEqualTo(DEFAULT_DIPLOME);
        assertThat(testFormation.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testFormation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Formation in Elasticsearch
        verify(mockFormationSearchRepository, times(1)).save(testFormation);
    }

    @Test
    @Transactional
    public void createFormationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // Create the Formation with an existing ID
        formation.setId(1L);
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationMockMvc.perform(post("/api/formations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Formation in Elasticsearch
        verify(mockFormationSearchRepository, times(0)).save(formation);
    }


    @Test
    @Transactional
    public void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList
        restFormationMockMvc.perform(get("/api/formations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodBigin").value(hasItem(DEFAULT_PERIOD_BIGIN.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].periodNow").value(hasItem(DEFAULT_PERIOD_NOW.booleanValue())))
            .andExpect(jsonPath("$.[*].diplome").value(hasItem(DEFAULT_DIPLOME)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc.perform(get("/api/formations/{id}", formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.periodBigin").value(DEFAULT_PERIOD_BIGIN.toString()))
            .andExpect(jsonPath("$.periodEnd").value(DEFAULT_PERIOD_END.toString()))
            .andExpect(jsonPath("$.periodNow").value(DEFAULT_PERIOD_NOW.booleanValue()))
            .andExpect(jsonPath("$.diplome").value(DEFAULT_DIPLOME))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getFormationsByIdFiltering() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        Long id = formation.getId();

        defaultFormationShouldBeFound("id.equals=" + id);
        defaultFormationShouldNotBeFound("id.notEquals=" + id);

        defaultFormationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormationShouldNotBeFound("id.greaterThan=" + id);

        defaultFormationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFormationsByPeriodBiginIsEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodBigin equals to DEFAULT_PERIOD_BIGIN
        defaultFormationShouldBeFound("periodBigin.equals=" + DEFAULT_PERIOD_BIGIN);

        // Get all the formationList where periodBigin equals to UPDATED_PERIOD_BIGIN
        defaultFormationShouldNotBeFound("periodBigin.equals=" + UPDATED_PERIOD_BIGIN);
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodBiginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodBigin not equals to DEFAULT_PERIOD_BIGIN
        defaultFormationShouldNotBeFound("periodBigin.notEquals=" + DEFAULT_PERIOD_BIGIN);

        // Get all the formationList where periodBigin not equals to UPDATED_PERIOD_BIGIN
        defaultFormationShouldBeFound("periodBigin.notEquals=" + UPDATED_PERIOD_BIGIN);
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodBiginIsInShouldWork() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodBigin in DEFAULT_PERIOD_BIGIN or UPDATED_PERIOD_BIGIN
        defaultFormationShouldBeFound("periodBigin.in=" + DEFAULT_PERIOD_BIGIN + "," + UPDATED_PERIOD_BIGIN);

        // Get all the formationList where periodBigin equals to UPDATED_PERIOD_BIGIN
        defaultFormationShouldNotBeFound("periodBigin.in=" + UPDATED_PERIOD_BIGIN);
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodBiginIsNullOrNotNull() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodBigin is not null
        defaultFormationShouldBeFound("periodBigin.specified=true");

        // Get all the formationList where periodBigin is null
        defaultFormationShouldNotBeFound("periodBigin.specified=false");
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodEndIsEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodEnd equals to DEFAULT_PERIOD_END
        defaultFormationShouldBeFound("periodEnd.equals=" + DEFAULT_PERIOD_END);

        // Get all the formationList where periodEnd equals to UPDATED_PERIOD_END
        defaultFormationShouldNotBeFound("periodEnd.equals=" + UPDATED_PERIOD_END);
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodEnd not equals to DEFAULT_PERIOD_END
        defaultFormationShouldNotBeFound("periodEnd.notEquals=" + DEFAULT_PERIOD_END);

        // Get all the formationList where periodEnd not equals to UPDATED_PERIOD_END
        defaultFormationShouldBeFound("periodEnd.notEquals=" + UPDATED_PERIOD_END);
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodEndIsInShouldWork() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodEnd in DEFAULT_PERIOD_END or UPDATED_PERIOD_END
        defaultFormationShouldBeFound("periodEnd.in=" + DEFAULT_PERIOD_END + "," + UPDATED_PERIOD_END);

        // Get all the formationList where periodEnd equals to UPDATED_PERIOD_END
        defaultFormationShouldNotBeFound("periodEnd.in=" + UPDATED_PERIOD_END);
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodEnd is not null
        defaultFormationShouldBeFound("periodEnd.specified=true");

        // Get all the formationList where periodEnd is null
        defaultFormationShouldNotBeFound("periodEnd.specified=false");
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodNowIsEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodNow equals to DEFAULT_PERIOD_NOW
        defaultFormationShouldBeFound("periodNow.equals=" + DEFAULT_PERIOD_NOW);

        // Get all the formationList where periodNow equals to UPDATED_PERIOD_NOW
        defaultFormationShouldNotBeFound("periodNow.equals=" + UPDATED_PERIOD_NOW);
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodNowIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodNow not equals to DEFAULT_PERIOD_NOW
        defaultFormationShouldNotBeFound("periodNow.notEquals=" + DEFAULT_PERIOD_NOW);

        // Get all the formationList where periodNow not equals to UPDATED_PERIOD_NOW
        defaultFormationShouldBeFound("periodNow.notEquals=" + UPDATED_PERIOD_NOW);
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodNowIsInShouldWork() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodNow in DEFAULT_PERIOD_NOW or UPDATED_PERIOD_NOW
        defaultFormationShouldBeFound("periodNow.in=" + DEFAULT_PERIOD_NOW + "," + UPDATED_PERIOD_NOW);

        // Get all the formationList where periodNow equals to UPDATED_PERIOD_NOW
        defaultFormationShouldNotBeFound("periodNow.in=" + UPDATED_PERIOD_NOW);
    }

    @Test
    @Transactional
    public void getAllFormationsByPeriodNowIsNullOrNotNull() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where periodNow is not null
        defaultFormationShouldBeFound("periodNow.specified=true");

        // Get all the formationList where periodNow is null
        defaultFormationShouldNotBeFound("periodNow.specified=false");
    }

    @Test
    @Transactional
    public void getAllFormationsByDiplomeIsEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where diplome equals to DEFAULT_DIPLOME
        defaultFormationShouldBeFound("diplome.equals=" + DEFAULT_DIPLOME);

        // Get all the formationList where diplome equals to UPDATED_DIPLOME
        defaultFormationShouldNotBeFound("diplome.equals=" + UPDATED_DIPLOME);
    }

    @Test
    @Transactional
    public void getAllFormationsByDiplomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where diplome not equals to DEFAULT_DIPLOME
        defaultFormationShouldNotBeFound("diplome.notEquals=" + DEFAULT_DIPLOME);

        // Get all the formationList where diplome not equals to UPDATED_DIPLOME
        defaultFormationShouldBeFound("diplome.notEquals=" + UPDATED_DIPLOME);
    }

    @Test
    @Transactional
    public void getAllFormationsByDiplomeIsInShouldWork() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where diplome in DEFAULT_DIPLOME or UPDATED_DIPLOME
        defaultFormationShouldBeFound("diplome.in=" + DEFAULT_DIPLOME + "," + UPDATED_DIPLOME);

        // Get all the formationList where diplome equals to UPDATED_DIPLOME
        defaultFormationShouldNotBeFound("diplome.in=" + UPDATED_DIPLOME);
    }

    @Test
    @Transactional
    public void getAllFormationsByDiplomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where diplome is not null
        defaultFormationShouldBeFound("diplome.specified=true");

        // Get all the formationList where diplome is null
        defaultFormationShouldNotBeFound("diplome.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormationsByDiplomeContainsSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where diplome contains DEFAULT_DIPLOME
        defaultFormationShouldBeFound("diplome.contains=" + DEFAULT_DIPLOME);

        // Get all the formationList where diplome contains UPDATED_DIPLOME
        defaultFormationShouldNotBeFound("diplome.contains=" + UPDATED_DIPLOME);
    }

    @Test
    @Transactional
    public void getAllFormationsByDiplomeNotContainsSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where diplome does not contain DEFAULT_DIPLOME
        defaultFormationShouldNotBeFound("diplome.doesNotContain=" + DEFAULT_DIPLOME);

        // Get all the formationList where diplome does not contain UPDATED_DIPLOME
        defaultFormationShouldBeFound("diplome.doesNotContain=" + UPDATED_DIPLOME);
    }


    @Test
    @Transactional
    public void getAllFormationsByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where ville equals to DEFAULT_VILLE
        defaultFormationShouldBeFound("ville.equals=" + DEFAULT_VILLE);

        // Get all the formationList where ville equals to UPDATED_VILLE
        defaultFormationShouldNotBeFound("ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllFormationsByVilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where ville not equals to DEFAULT_VILLE
        defaultFormationShouldNotBeFound("ville.notEquals=" + DEFAULT_VILLE);

        // Get all the formationList where ville not equals to UPDATED_VILLE
        defaultFormationShouldBeFound("ville.notEquals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllFormationsByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where ville in DEFAULT_VILLE or UPDATED_VILLE
        defaultFormationShouldBeFound("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE);

        // Get all the formationList where ville equals to UPDATED_VILLE
        defaultFormationShouldNotBeFound("ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllFormationsByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where ville is not null
        defaultFormationShouldBeFound("ville.specified=true");

        // Get all the formationList where ville is null
        defaultFormationShouldNotBeFound("ville.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormationsByVilleContainsSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where ville contains DEFAULT_VILLE
        defaultFormationShouldBeFound("ville.contains=" + DEFAULT_VILLE);

        // Get all the formationList where ville contains UPDATED_VILLE
        defaultFormationShouldNotBeFound("ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllFormationsByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where ville does not contain DEFAULT_VILLE
        defaultFormationShouldNotBeFound("ville.doesNotContain=" + DEFAULT_VILLE);

        // Get all the formationList where ville does not contain UPDATED_VILLE
        defaultFormationShouldBeFound("ville.doesNotContain=" + UPDATED_VILLE);
    }


    @Test
    @Transactional
    public void getAllFormationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where description equals to DEFAULT_DESCRIPTION
        defaultFormationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the formationList where description equals to UPDATED_DESCRIPTION
        defaultFormationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where description not equals to DEFAULT_DESCRIPTION
        defaultFormationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the formationList where description not equals to UPDATED_DESCRIPTION
        defaultFormationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFormationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the formationList where description equals to UPDATED_DESCRIPTION
        defaultFormationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where description is not null
        defaultFormationShouldBeFound("description.specified=true");

        // Get all the formationList where description is null
        defaultFormationShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where description contains DEFAULT_DESCRIPTION
        defaultFormationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the formationList where description contains UPDATED_DESCRIPTION
        defaultFormationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList where description does not contain DEFAULT_DESCRIPTION
        defaultFormationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the formationList where description does not contain UPDATED_DESCRIPTION
        defaultFormationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllFormationsByCvcorpIsEqualToSomething() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);
        Cvcorp cvcorp = CvcorpResourceIT.createEntity(em);
        em.persist(cvcorp);
        em.flush();
        formation.setCvcorp(cvcorp);
        formationRepository.saveAndFlush(formation);
        Long cvcorpId = cvcorp.getId();

        // Get all the formationList where cvcorp equals to cvcorpId
        defaultFormationShouldBeFound("cvcorpId.equals=" + cvcorpId);

        // Get all the formationList where cvcorp equals to cvcorpId + 1
        defaultFormationShouldNotBeFound("cvcorpId.equals=" + (cvcorpId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormationShouldBeFound(String filter) throws Exception {
        restFormationMockMvc.perform(get("/api/formations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodBigin").value(hasItem(DEFAULT_PERIOD_BIGIN.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].periodNow").value(hasItem(DEFAULT_PERIOD_NOW.booleanValue())))
            .andExpect(jsonPath("$.[*].diplome").value(hasItem(DEFAULT_DIPLOME)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFormationMockMvc.perform(get("/api/formations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormationShouldNotBeFound(String filter) throws Exception {
        restFormationMockMvc.perform(get("/api/formations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormationMockMvc.perform(get("/api/formations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get("/api/formations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation
        Formation updatedFormation = formationRepository.findById(formation.getId()).get();
        // Disconnect from session so that the updates on updatedFormation are not directly saved in db
        em.detach(updatedFormation);
        updatedFormation
            .periodBigin(UPDATED_PERIOD_BIGIN)
            .periodEnd(UPDATED_PERIOD_END)
            .periodNow(UPDATED_PERIOD_NOW)
            .diplome(UPDATED_DIPLOME)
            .ville(UPDATED_VILLE)
            .description(UPDATED_DESCRIPTION);
        FormationDTO formationDTO = formationMapper.toDto(updatedFormation);

        restFormationMockMvc.perform(put("/api/formations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getPeriodBigin()).isEqualTo(UPDATED_PERIOD_BIGIN);
        assertThat(testFormation.getPeriodEnd()).isEqualTo(UPDATED_PERIOD_END);
        assertThat(testFormation.isPeriodNow()).isEqualTo(UPDATED_PERIOD_NOW);
        assertThat(testFormation.getDiplome()).isEqualTo(UPDATED_DIPLOME);
        assertThat(testFormation.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFormation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Formation in Elasticsearch
        verify(mockFormationSearchRepository, times(1)).save(testFormation);
    }

    @Test
    @Transactional
    public void updateNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc.perform(put("/api/formations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Formation in Elasticsearch
        verify(mockFormationSearchRepository, times(0)).save(formation);
    }

    @Test
    @Transactional
    public void deleteFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeDelete = formationRepository.findAll().size();

        // Delete the formation
        restFormationMockMvc.perform(delete("/api/formations/{id}", formation.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Formation in Elasticsearch
        verify(mockFormationSearchRepository, times(1)).deleteById(formation.getId());
    }

    @Test
    @Transactional
    public void searchFormation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        formationRepository.saveAndFlush(formation);
        when(mockFormationSearchRepository.search(queryStringQuery("id:" + formation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(formation), PageRequest.of(0, 1), 1));

        // Search the formation
        restFormationMockMvc.perform(get("/api/_search/formations?query=id:" + formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodBigin").value(hasItem(DEFAULT_PERIOD_BIGIN.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].periodNow").value(hasItem(DEFAULT_PERIOD_NOW.booleanValue())))
            .andExpect(jsonPath("$.[*].diplome").value(hasItem(DEFAULT_DIPLOME)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
