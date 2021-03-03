package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.CvcorpApp;
import com.cv.cvcorp.config.SecurityBeanOverrideConfiguration;
import com.cv.cvcorp.domain.Experience;
import com.cv.cvcorp.domain.Cvcorp;
import com.cv.cvcorp.repository.ExperienceRepository;
import com.cv.cvcorp.repository.search.ExperienceSearchRepository;
import com.cv.cvcorp.service.ExperienceService;
import com.cv.cvcorp.service.dto.ExperienceDTO;
import com.cv.cvcorp.service.mapper.ExperienceMapper;
import com.cv.cvcorp.service.dto.ExperienceCriteria;
import com.cv.cvcorp.service.ExperienceQueryService;

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
 * Integration tests for the {@link ExperienceResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, CvcorpApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ExperienceResourceIT {

    private static final Instant DEFAULT_PERIOD_BIGIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PERIOD_BIGIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PERIOD_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PERIOD_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PERIOD_NOW = false;
    private static final Boolean UPDATED_PERIOD_NOW = true;

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_POSTE = "AAAAAAAAAA";
    private static final String UPDATED_POSTE = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEUR = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEUR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private ExperienceMapper experienceMapper;

    @Autowired
    private ExperienceService experienceService;

    /**
     * This repository is mocked in the com.cv.cvcorp.repository.search test package.
     *
     * @see com.cv.cvcorp.repository.search.ExperienceSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExperienceSearchRepository mockExperienceSearchRepository;

    @Autowired
    private ExperienceQueryService experienceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExperienceMockMvc;

    private Experience experience;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experience createEntity(EntityManager em) {
        Experience experience = new Experience()
            .periodBigin(DEFAULT_PERIOD_BIGIN)
            .periodEnd(DEFAULT_PERIOD_END)
            .periodNow(DEFAULT_PERIOD_NOW)
            .ville(DEFAULT_VILLE)
            .poste(DEFAULT_POSTE)
            .employeur(DEFAULT_EMPLOYEUR)
            .description(DEFAULT_DESCRIPTION);
        return experience;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experience createUpdatedEntity(EntityManager em) {
        Experience experience = new Experience()
            .periodBigin(UPDATED_PERIOD_BIGIN)
            .periodEnd(UPDATED_PERIOD_END)
            .periodNow(UPDATED_PERIOD_NOW)
            .ville(UPDATED_VILLE)
            .poste(UPDATED_POSTE)
            .employeur(UPDATED_EMPLOYEUR)
            .description(UPDATED_DESCRIPTION);
        return experience;
    }

    @BeforeEach
    public void initTest() {
        experience = createEntity(em);
    }

    @Test
    @Transactional
    public void createExperience() throws Exception {
        int databaseSizeBeforeCreate = experienceRepository.findAll().size();
        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);
        restExperienceMockMvc.perform(post("/api/experiences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isCreated());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeCreate + 1);
        Experience testExperience = experienceList.get(experienceList.size() - 1);
        assertThat(testExperience.getPeriodBigin()).isEqualTo(DEFAULT_PERIOD_BIGIN);
        assertThat(testExperience.getPeriodEnd()).isEqualTo(DEFAULT_PERIOD_END);
        assertThat(testExperience.isPeriodNow()).isEqualTo(DEFAULT_PERIOD_NOW);
        assertThat(testExperience.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testExperience.getPoste()).isEqualTo(DEFAULT_POSTE);
        assertThat(testExperience.getEmployeur()).isEqualTo(DEFAULT_EMPLOYEUR);
        assertThat(testExperience.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Experience in Elasticsearch
        verify(mockExperienceSearchRepository, times(1)).save(testExperience);
    }

    @Test
    @Transactional
    public void createExperienceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = experienceRepository.findAll().size();

        // Create the Experience with an existing ID
        experience.setId(1L);
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperienceMockMvc.perform(post("/api/experiences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Experience in Elasticsearch
        verify(mockExperienceSearchRepository, times(0)).save(experience);
    }


    @Test
    @Transactional
    public void getAllExperiences() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList
        restExperienceMockMvc.perform(get("/api/experiences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experience.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodBigin").value(hasItem(DEFAULT_PERIOD_BIGIN.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].periodNow").value(hasItem(DEFAULT_PERIOD_NOW.booleanValue())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].poste").value(hasItem(DEFAULT_POSTE)))
            .andExpect(jsonPath("$.[*].employeur").value(hasItem(DEFAULT_EMPLOYEUR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get the experience
        restExperienceMockMvc.perform(get("/api/experiences/{id}", experience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(experience.getId().intValue()))
            .andExpect(jsonPath("$.periodBigin").value(DEFAULT_PERIOD_BIGIN.toString()))
            .andExpect(jsonPath("$.periodEnd").value(DEFAULT_PERIOD_END.toString()))
            .andExpect(jsonPath("$.periodNow").value(DEFAULT_PERIOD_NOW.booleanValue()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.poste").value(DEFAULT_POSTE))
            .andExpect(jsonPath("$.employeur").value(DEFAULT_EMPLOYEUR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getExperiencesByIdFiltering() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        Long id = experience.getId();

        defaultExperienceShouldBeFound("id.equals=" + id);
        defaultExperienceShouldNotBeFound("id.notEquals=" + id);

        defaultExperienceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExperienceShouldNotBeFound("id.greaterThan=" + id);

        defaultExperienceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExperienceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExperiencesByPeriodBiginIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodBigin equals to DEFAULT_PERIOD_BIGIN
        defaultExperienceShouldBeFound("periodBigin.equals=" + DEFAULT_PERIOD_BIGIN);

        // Get all the experienceList where periodBigin equals to UPDATED_PERIOD_BIGIN
        defaultExperienceShouldNotBeFound("periodBigin.equals=" + UPDATED_PERIOD_BIGIN);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodBiginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodBigin not equals to DEFAULT_PERIOD_BIGIN
        defaultExperienceShouldNotBeFound("periodBigin.notEquals=" + DEFAULT_PERIOD_BIGIN);

        // Get all the experienceList where periodBigin not equals to UPDATED_PERIOD_BIGIN
        defaultExperienceShouldBeFound("periodBigin.notEquals=" + UPDATED_PERIOD_BIGIN);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodBiginIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodBigin in DEFAULT_PERIOD_BIGIN or UPDATED_PERIOD_BIGIN
        defaultExperienceShouldBeFound("periodBigin.in=" + DEFAULT_PERIOD_BIGIN + "," + UPDATED_PERIOD_BIGIN);

        // Get all the experienceList where periodBigin equals to UPDATED_PERIOD_BIGIN
        defaultExperienceShouldNotBeFound("periodBigin.in=" + UPDATED_PERIOD_BIGIN);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodBiginIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodBigin is not null
        defaultExperienceShouldBeFound("periodBigin.specified=true");

        // Get all the experienceList where periodBigin is null
        defaultExperienceShouldNotBeFound("periodBigin.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodEndIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodEnd equals to DEFAULT_PERIOD_END
        defaultExperienceShouldBeFound("periodEnd.equals=" + DEFAULT_PERIOD_END);

        // Get all the experienceList where periodEnd equals to UPDATED_PERIOD_END
        defaultExperienceShouldNotBeFound("periodEnd.equals=" + UPDATED_PERIOD_END);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodEnd not equals to DEFAULT_PERIOD_END
        defaultExperienceShouldNotBeFound("periodEnd.notEquals=" + DEFAULT_PERIOD_END);

        // Get all the experienceList where periodEnd not equals to UPDATED_PERIOD_END
        defaultExperienceShouldBeFound("periodEnd.notEquals=" + UPDATED_PERIOD_END);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodEndIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodEnd in DEFAULT_PERIOD_END or UPDATED_PERIOD_END
        defaultExperienceShouldBeFound("periodEnd.in=" + DEFAULT_PERIOD_END + "," + UPDATED_PERIOD_END);

        // Get all the experienceList where periodEnd equals to UPDATED_PERIOD_END
        defaultExperienceShouldNotBeFound("periodEnd.in=" + UPDATED_PERIOD_END);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodEnd is not null
        defaultExperienceShouldBeFound("periodEnd.specified=true");

        // Get all the experienceList where periodEnd is null
        defaultExperienceShouldNotBeFound("periodEnd.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodNowIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodNow equals to DEFAULT_PERIOD_NOW
        defaultExperienceShouldBeFound("periodNow.equals=" + DEFAULT_PERIOD_NOW);

        // Get all the experienceList where periodNow equals to UPDATED_PERIOD_NOW
        defaultExperienceShouldNotBeFound("periodNow.equals=" + UPDATED_PERIOD_NOW);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodNowIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodNow not equals to DEFAULT_PERIOD_NOW
        defaultExperienceShouldNotBeFound("periodNow.notEquals=" + DEFAULT_PERIOD_NOW);

        // Get all the experienceList where periodNow not equals to UPDATED_PERIOD_NOW
        defaultExperienceShouldBeFound("periodNow.notEquals=" + UPDATED_PERIOD_NOW);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodNowIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodNow in DEFAULT_PERIOD_NOW or UPDATED_PERIOD_NOW
        defaultExperienceShouldBeFound("periodNow.in=" + DEFAULT_PERIOD_NOW + "," + UPDATED_PERIOD_NOW);

        // Get all the experienceList where periodNow equals to UPDATED_PERIOD_NOW
        defaultExperienceShouldNotBeFound("periodNow.in=" + UPDATED_PERIOD_NOW);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPeriodNowIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where periodNow is not null
        defaultExperienceShouldBeFound("periodNow.specified=true");

        // Get all the experienceList where periodNow is null
        defaultExperienceShouldNotBeFound("periodNow.specified=false");
    }

    @Test
    @Transactional
    public void getAllExperiencesByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where ville equals to DEFAULT_VILLE
        defaultExperienceShouldBeFound("ville.equals=" + DEFAULT_VILLE);

        // Get all the experienceList where ville equals to UPDATED_VILLE
        defaultExperienceShouldNotBeFound("ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByVilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where ville not equals to DEFAULT_VILLE
        defaultExperienceShouldNotBeFound("ville.notEquals=" + DEFAULT_VILLE);

        // Get all the experienceList where ville not equals to UPDATED_VILLE
        defaultExperienceShouldBeFound("ville.notEquals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where ville in DEFAULT_VILLE or UPDATED_VILLE
        defaultExperienceShouldBeFound("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE);

        // Get all the experienceList where ville equals to UPDATED_VILLE
        defaultExperienceShouldNotBeFound("ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where ville is not null
        defaultExperienceShouldBeFound("ville.specified=true");

        // Get all the experienceList where ville is null
        defaultExperienceShouldNotBeFound("ville.specified=false");
    }
                @Test
    @Transactional
    public void getAllExperiencesByVilleContainsSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where ville contains DEFAULT_VILLE
        defaultExperienceShouldBeFound("ville.contains=" + DEFAULT_VILLE);

        // Get all the experienceList where ville contains UPDATED_VILLE
        defaultExperienceShouldNotBeFound("ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where ville does not contain DEFAULT_VILLE
        defaultExperienceShouldNotBeFound("ville.doesNotContain=" + DEFAULT_VILLE);

        // Get all the experienceList where ville does not contain UPDATED_VILLE
        defaultExperienceShouldBeFound("ville.doesNotContain=" + UPDATED_VILLE);
    }


    @Test
    @Transactional
    public void getAllExperiencesByPosteIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where poste equals to DEFAULT_POSTE
        defaultExperienceShouldBeFound("poste.equals=" + DEFAULT_POSTE);

        // Get all the experienceList where poste equals to UPDATED_POSTE
        defaultExperienceShouldNotBeFound("poste.equals=" + UPDATED_POSTE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPosteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where poste not equals to DEFAULT_POSTE
        defaultExperienceShouldNotBeFound("poste.notEquals=" + DEFAULT_POSTE);

        // Get all the experienceList where poste not equals to UPDATED_POSTE
        defaultExperienceShouldBeFound("poste.notEquals=" + UPDATED_POSTE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPosteIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where poste in DEFAULT_POSTE or UPDATED_POSTE
        defaultExperienceShouldBeFound("poste.in=" + DEFAULT_POSTE + "," + UPDATED_POSTE);

        // Get all the experienceList where poste equals to UPDATED_POSTE
        defaultExperienceShouldNotBeFound("poste.in=" + UPDATED_POSTE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPosteIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where poste is not null
        defaultExperienceShouldBeFound("poste.specified=true");

        // Get all the experienceList where poste is null
        defaultExperienceShouldNotBeFound("poste.specified=false");
    }
                @Test
    @Transactional
    public void getAllExperiencesByPosteContainsSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where poste contains DEFAULT_POSTE
        defaultExperienceShouldBeFound("poste.contains=" + DEFAULT_POSTE);

        // Get all the experienceList where poste contains UPDATED_POSTE
        defaultExperienceShouldNotBeFound("poste.contains=" + UPDATED_POSTE);
    }

    @Test
    @Transactional
    public void getAllExperiencesByPosteNotContainsSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where poste does not contain DEFAULT_POSTE
        defaultExperienceShouldNotBeFound("poste.doesNotContain=" + DEFAULT_POSTE);

        // Get all the experienceList where poste does not contain UPDATED_POSTE
        defaultExperienceShouldBeFound("poste.doesNotContain=" + UPDATED_POSTE);
    }


    @Test
    @Transactional
    public void getAllExperiencesByEmployeurIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where employeur equals to DEFAULT_EMPLOYEUR
        defaultExperienceShouldBeFound("employeur.equals=" + DEFAULT_EMPLOYEUR);

        // Get all the experienceList where employeur equals to UPDATED_EMPLOYEUR
        defaultExperienceShouldNotBeFound("employeur.equals=" + UPDATED_EMPLOYEUR);
    }

    @Test
    @Transactional
    public void getAllExperiencesByEmployeurIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where employeur not equals to DEFAULT_EMPLOYEUR
        defaultExperienceShouldNotBeFound("employeur.notEquals=" + DEFAULT_EMPLOYEUR);

        // Get all the experienceList where employeur not equals to UPDATED_EMPLOYEUR
        defaultExperienceShouldBeFound("employeur.notEquals=" + UPDATED_EMPLOYEUR);
    }

    @Test
    @Transactional
    public void getAllExperiencesByEmployeurIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where employeur in DEFAULT_EMPLOYEUR or UPDATED_EMPLOYEUR
        defaultExperienceShouldBeFound("employeur.in=" + DEFAULT_EMPLOYEUR + "," + UPDATED_EMPLOYEUR);

        // Get all the experienceList where employeur equals to UPDATED_EMPLOYEUR
        defaultExperienceShouldNotBeFound("employeur.in=" + UPDATED_EMPLOYEUR);
    }

    @Test
    @Transactional
    public void getAllExperiencesByEmployeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where employeur is not null
        defaultExperienceShouldBeFound("employeur.specified=true");

        // Get all the experienceList where employeur is null
        defaultExperienceShouldNotBeFound("employeur.specified=false");
    }
                @Test
    @Transactional
    public void getAllExperiencesByEmployeurContainsSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where employeur contains DEFAULT_EMPLOYEUR
        defaultExperienceShouldBeFound("employeur.contains=" + DEFAULT_EMPLOYEUR);

        // Get all the experienceList where employeur contains UPDATED_EMPLOYEUR
        defaultExperienceShouldNotBeFound("employeur.contains=" + UPDATED_EMPLOYEUR);
    }

    @Test
    @Transactional
    public void getAllExperiencesByEmployeurNotContainsSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where employeur does not contain DEFAULT_EMPLOYEUR
        defaultExperienceShouldNotBeFound("employeur.doesNotContain=" + DEFAULT_EMPLOYEUR);

        // Get all the experienceList where employeur does not contain UPDATED_EMPLOYEUR
        defaultExperienceShouldBeFound("employeur.doesNotContain=" + UPDATED_EMPLOYEUR);
    }


    @Test
    @Transactional
    public void getAllExperiencesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where description equals to DEFAULT_DESCRIPTION
        defaultExperienceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the experienceList where description equals to UPDATED_DESCRIPTION
        defaultExperienceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExperiencesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where description not equals to DEFAULT_DESCRIPTION
        defaultExperienceShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the experienceList where description not equals to UPDATED_DESCRIPTION
        defaultExperienceShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExperiencesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultExperienceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the experienceList where description equals to UPDATED_DESCRIPTION
        defaultExperienceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExperiencesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where description is not null
        defaultExperienceShouldBeFound("description.specified=true");

        // Get all the experienceList where description is null
        defaultExperienceShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllExperiencesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where description contains DEFAULT_DESCRIPTION
        defaultExperienceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the experienceList where description contains UPDATED_DESCRIPTION
        defaultExperienceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExperiencesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experienceList where description does not contain DEFAULT_DESCRIPTION
        defaultExperienceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the experienceList where description does not contain UPDATED_DESCRIPTION
        defaultExperienceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllExperiencesByCvcorpIsEqualToSomething() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);
        Cvcorp cvcorp = CvcorpResourceIT.createEntity(em);
        em.persist(cvcorp);
        em.flush();
        experience.setCvcorp(cvcorp);
        experienceRepository.saveAndFlush(experience);
        Long cvcorpId = cvcorp.getId();

        // Get all the experienceList where cvcorp equals to cvcorpId
        defaultExperienceShouldBeFound("cvcorpId.equals=" + cvcorpId);

        // Get all the experienceList where cvcorp equals to cvcorpId + 1
        defaultExperienceShouldNotBeFound("cvcorpId.equals=" + (cvcorpId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExperienceShouldBeFound(String filter) throws Exception {
        restExperienceMockMvc.perform(get("/api/experiences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experience.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodBigin").value(hasItem(DEFAULT_PERIOD_BIGIN.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].periodNow").value(hasItem(DEFAULT_PERIOD_NOW.booleanValue())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].poste").value(hasItem(DEFAULT_POSTE)))
            .andExpect(jsonPath("$.[*].employeur").value(hasItem(DEFAULT_EMPLOYEUR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restExperienceMockMvc.perform(get("/api/experiences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExperienceShouldNotBeFound(String filter) throws Exception {
        restExperienceMockMvc.perform(get("/api/experiences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExperienceMockMvc.perform(get("/api/experiences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingExperience() throws Exception {
        // Get the experience
        restExperienceMockMvc.perform(get("/api/experiences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();

        // Update the experience
        Experience updatedExperience = experienceRepository.findById(experience.getId()).get();
        // Disconnect from session so that the updates on updatedExperience are not directly saved in db
        em.detach(updatedExperience);
        updatedExperience
            .periodBigin(UPDATED_PERIOD_BIGIN)
            .periodEnd(UPDATED_PERIOD_END)
            .periodNow(UPDATED_PERIOD_NOW)
            .ville(UPDATED_VILLE)
            .poste(UPDATED_POSTE)
            .employeur(UPDATED_EMPLOYEUR)
            .description(UPDATED_DESCRIPTION);
        ExperienceDTO experienceDTO = experienceMapper.toDto(updatedExperience);

        restExperienceMockMvc.perform(put("/api/experiences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isOk());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);
        Experience testExperience = experienceList.get(experienceList.size() - 1);
        assertThat(testExperience.getPeriodBigin()).isEqualTo(UPDATED_PERIOD_BIGIN);
        assertThat(testExperience.getPeriodEnd()).isEqualTo(UPDATED_PERIOD_END);
        assertThat(testExperience.isPeriodNow()).isEqualTo(UPDATED_PERIOD_NOW);
        assertThat(testExperience.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testExperience.getPoste()).isEqualTo(UPDATED_POSTE);
        assertThat(testExperience.getEmployeur()).isEqualTo(UPDATED_EMPLOYEUR);
        assertThat(testExperience.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Experience in Elasticsearch
        verify(mockExperienceSearchRepository, times(1)).save(testExperience);
    }

    @Test
    @Transactional
    public void updateNonExistingExperience() throws Exception {
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();

        // Create the Experience
        ExperienceDTO experienceDTO = experienceMapper.toDto(experience);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperienceMockMvc.perform(put("/api/experiences").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(experienceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Experience in the database
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Experience in Elasticsearch
        verify(mockExperienceSearchRepository, times(0)).save(experience);
    }

    @Test
    @Transactional
    public void deleteExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        int databaseSizeBeforeDelete = experienceRepository.findAll().size();

        // Delete the experience
        restExperienceMockMvc.perform(delete("/api/experiences/{id}", experience.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Experience> experienceList = experienceRepository.findAll();
        assertThat(experienceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Experience in Elasticsearch
        verify(mockExperienceSearchRepository, times(1)).deleteById(experience.getId());
    }

    @Test
    @Transactional
    public void searchExperience() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        experienceRepository.saveAndFlush(experience);
        when(mockExperienceSearchRepository.search(queryStringQuery("id:" + experience.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(experience), PageRequest.of(0, 1), 1));

        // Search the experience
        restExperienceMockMvc.perform(get("/api/_search/experiences?query=id:" + experience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experience.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodBigin").value(hasItem(DEFAULT_PERIOD_BIGIN.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].periodNow").value(hasItem(DEFAULT_PERIOD_NOW.booleanValue())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].poste").value(hasItem(DEFAULT_POSTE)))
            .andExpect(jsonPath("$.[*].employeur").value(hasItem(DEFAULT_EMPLOYEUR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
