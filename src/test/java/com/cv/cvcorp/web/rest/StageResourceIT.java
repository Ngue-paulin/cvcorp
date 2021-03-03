package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.CvcorpApp;
import com.cv.cvcorp.config.SecurityBeanOverrideConfiguration;
import com.cv.cvcorp.domain.Stage;
import com.cv.cvcorp.domain.Cvcorp;
import com.cv.cvcorp.repository.StageRepository;
import com.cv.cvcorp.repository.search.StageSearchRepository;
import com.cv.cvcorp.service.StageService;
import com.cv.cvcorp.service.dto.StageDTO;
import com.cv.cvcorp.service.mapper.StageMapper;
import com.cv.cvcorp.service.dto.StageCriteria;
import com.cv.cvcorp.service.StageQueryService;

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
 * Integration tests for the {@link StageResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, CvcorpApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class StageResourceIT {

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
    private StageRepository stageRepository;

    @Autowired
    private StageMapper stageMapper;

    @Autowired
    private StageService stageService;

    /**
     * This repository is mocked in the com.cv.cvcorp.repository.search test package.
     *
     * @see com.cv.cvcorp.repository.search.StageSearchRepositoryMockConfiguration
     */
    @Autowired
    private StageSearchRepository mockStageSearchRepository;

    @Autowired
    private StageQueryService stageQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStageMockMvc;

    private Stage stage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stage createEntity(EntityManager em) {
        Stage stage = new Stage()
            .periodBigin(DEFAULT_PERIOD_BIGIN)
            .periodEnd(DEFAULT_PERIOD_END)
            .periodNow(DEFAULT_PERIOD_NOW)
            .ville(DEFAULT_VILLE)
            .poste(DEFAULT_POSTE)
            .employeur(DEFAULT_EMPLOYEUR)
            .description(DEFAULT_DESCRIPTION);
        return stage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stage createUpdatedEntity(EntityManager em) {
        Stage stage = new Stage()
            .periodBigin(UPDATED_PERIOD_BIGIN)
            .periodEnd(UPDATED_PERIOD_END)
            .periodNow(UPDATED_PERIOD_NOW)
            .ville(UPDATED_VILLE)
            .poste(UPDATED_POSTE)
            .employeur(UPDATED_EMPLOYEUR)
            .description(UPDATED_DESCRIPTION);
        return stage;
    }

    @BeforeEach
    public void initTest() {
        stage = createEntity(em);
    }

    @Test
    @Transactional
    public void createStage() throws Exception {
        int databaseSizeBeforeCreate = stageRepository.findAll().size();
        // Create the Stage
        StageDTO stageDTO = stageMapper.toDto(stage);
        restStageMockMvc.perform(post("/api/stages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stageDTO)))
            .andExpect(status().isCreated());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeCreate + 1);
        Stage testStage = stageList.get(stageList.size() - 1);
        assertThat(testStage.getPeriodBigin()).isEqualTo(DEFAULT_PERIOD_BIGIN);
        assertThat(testStage.getPeriodEnd()).isEqualTo(DEFAULT_PERIOD_END);
        assertThat(testStage.isPeriodNow()).isEqualTo(DEFAULT_PERIOD_NOW);
        assertThat(testStage.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testStage.getPoste()).isEqualTo(DEFAULT_POSTE);
        assertThat(testStage.getEmployeur()).isEqualTo(DEFAULT_EMPLOYEUR);
        assertThat(testStage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Stage in Elasticsearch
        verify(mockStageSearchRepository, times(1)).save(testStage);
    }

    @Test
    @Transactional
    public void createStageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stageRepository.findAll().size();

        // Create the Stage with an existing ID
        stage.setId(1L);
        StageDTO stageDTO = stageMapper.toDto(stage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStageMockMvc.perform(post("/api/stages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeCreate);

        // Validate the Stage in Elasticsearch
        verify(mockStageSearchRepository, times(0)).save(stage);
    }


    @Test
    @Transactional
    public void getAllStages() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList
        restStageMockMvc.perform(get("/api/stages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stage.getId().intValue())))
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
    public void getStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get the stage
        restStageMockMvc.perform(get("/api/stages/{id}", stage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stage.getId().intValue()))
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
    public void getStagesByIdFiltering() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        Long id = stage.getId();

        defaultStageShouldBeFound("id.equals=" + id);
        defaultStageShouldNotBeFound("id.notEquals=" + id);

        defaultStageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStageShouldNotBeFound("id.greaterThan=" + id);

        defaultStageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStageShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStagesByPeriodBiginIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodBigin equals to DEFAULT_PERIOD_BIGIN
        defaultStageShouldBeFound("periodBigin.equals=" + DEFAULT_PERIOD_BIGIN);

        // Get all the stageList where periodBigin equals to UPDATED_PERIOD_BIGIN
        defaultStageShouldNotBeFound("periodBigin.equals=" + UPDATED_PERIOD_BIGIN);
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodBiginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodBigin not equals to DEFAULT_PERIOD_BIGIN
        defaultStageShouldNotBeFound("periodBigin.notEquals=" + DEFAULT_PERIOD_BIGIN);

        // Get all the stageList where periodBigin not equals to UPDATED_PERIOD_BIGIN
        defaultStageShouldBeFound("periodBigin.notEquals=" + UPDATED_PERIOD_BIGIN);
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodBiginIsInShouldWork() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodBigin in DEFAULT_PERIOD_BIGIN or UPDATED_PERIOD_BIGIN
        defaultStageShouldBeFound("periodBigin.in=" + DEFAULT_PERIOD_BIGIN + "," + UPDATED_PERIOD_BIGIN);

        // Get all the stageList where periodBigin equals to UPDATED_PERIOD_BIGIN
        defaultStageShouldNotBeFound("periodBigin.in=" + UPDATED_PERIOD_BIGIN);
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodBiginIsNullOrNotNull() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodBigin is not null
        defaultStageShouldBeFound("periodBigin.specified=true");

        // Get all the stageList where periodBigin is null
        defaultStageShouldNotBeFound("periodBigin.specified=false");
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodEndIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodEnd equals to DEFAULT_PERIOD_END
        defaultStageShouldBeFound("periodEnd.equals=" + DEFAULT_PERIOD_END);

        // Get all the stageList where periodEnd equals to UPDATED_PERIOD_END
        defaultStageShouldNotBeFound("periodEnd.equals=" + UPDATED_PERIOD_END);
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodEnd not equals to DEFAULT_PERIOD_END
        defaultStageShouldNotBeFound("periodEnd.notEquals=" + DEFAULT_PERIOD_END);

        // Get all the stageList where periodEnd not equals to UPDATED_PERIOD_END
        defaultStageShouldBeFound("periodEnd.notEquals=" + UPDATED_PERIOD_END);
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodEndIsInShouldWork() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodEnd in DEFAULT_PERIOD_END or UPDATED_PERIOD_END
        defaultStageShouldBeFound("periodEnd.in=" + DEFAULT_PERIOD_END + "," + UPDATED_PERIOD_END);

        // Get all the stageList where periodEnd equals to UPDATED_PERIOD_END
        defaultStageShouldNotBeFound("periodEnd.in=" + UPDATED_PERIOD_END);
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodEnd is not null
        defaultStageShouldBeFound("periodEnd.specified=true");

        // Get all the stageList where periodEnd is null
        defaultStageShouldNotBeFound("periodEnd.specified=false");
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodNowIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodNow equals to DEFAULT_PERIOD_NOW
        defaultStageShouldBeFound("periodNow.equals=" + DEFAULT_PERIOD_NOW);

        // Get all the stageList where periodNow equals to UPDATED_PERIOD_NOW
        defaultStageShouldNotBeFound("periodNow.equals=" + UPDATED_PERIOD_NOW);
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodNowIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodNow not equals to DEFAULT_PERIOD_NOW
        defaultStageShouldNotBeFound("periodNow.notEquals=" + DEFAULT_PERIOD_NOW);

        // Get all the stageList where periodNow not equals to UPDATED_PERIOD_NOW
        defaultStageShouldBeFound("periodNow.notEquals=" + UPDATED_PERIOD_NOW);
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodNowIsInShouldWork() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodNow in DEFAULT_PERIOD_NOW or UPDATED_PERIOD_NOW
        defaultStageShouldBeFound("periodNow.in=" + DEFAULT_PERIOD_NOW + "," + UPDATED_PERIOD_NOW);

        // Get all the stageList where periodNow equals to UPDATED_PERIOD_NOW
        defaultStageShouldNotBeFound("periodNow.in=" + UPDATED_PERIOD_NOW);
    }

    @Test
    @Transactional
    public void getAllStagesByPeriodNowIsNullOrNotNull() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where periodNow is not null
        defaultStageShouldBeFound("periodNow.specified=true");

        // Get all the stageList where periodNow is null
        defaultStageShouldNotBeFound("periodNow.specified=false");
    }

    @Test
    @Transactional
    public void getAllStagesByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where ville equals to DEFAULT_VILLE
        defaultStageShouldBeFound("ville.equals=" + DEFAULT_VILLE);

        // Get all the stageList where ville equals to UPDATED_VILLE
        defaultStageShouldNotBeFound("ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllStagesByVilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where ville not equals to DEFAULT_VILLE
        defaultStageShouldNotBeFound("ville.notEquals=" + DEFAULT_VILLE);

        // Get all the stageList where ville not equals to UPDATED_VILLE
        defaultStageShouldBeFound("ville.notEquals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllStagesByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where ville in DEFAULT_VILLE or UPDATED_VILLE
        defaultStageShouldBeFound("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE);

        // Get all the stageList where ville equals to UPDATED_VILLE
        defaultStageShouldNotBeFound("ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllStagesByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where ville is not null
        defaultStageShouldBeFound("ville.specified=true");

        // Get all the stageList where ville is null
        defaultStageShouldNotBeFound("ville.specified=false");
    }
                @Test
    @Transactional
    public void getAllStagesByVilleContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where ville contains DEFAULT_VILLE
        defaultStageShouldBeFound("ville.contains=" + DEFAULT_VILLE);

        // Get all the stageList where ville contains UPDATED_VILLE
        defaultStageShouldNotBeFound("ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllStagesByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where ville does not contain DEFAULT_VILLE
        defaultStageShouldNotBeFound("ville.doesNotContain=" + DEFAULT_VILLE);

        // Get all the stageList where ville does not contain UPDATED_VILLE
        defaultStageShouldBeFound("ville.doesNotContain=" + UPDATED_VILLE);
    }


    @Test
    @Transactional
    public void getAllStagesByPosteIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where poste equals to DEFAULT_POSTE
        defaultStageShouldBeFound("poste.equals=" + DEFAULT_POSTE);

        // Get all the stageList where poste equals to UPDATED_POSTE
        defaultStageShouldNotBeFound("poste.equals=" + UPDATED_POSTE);
    }

    @Test
    @Transactional
    public void getAllStagesByPosteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where poste not equals to DEFAULT_POSTE
        defaultStageShouldNotBeFound("poste.notEquals=" + DEFAULT_POSTE);

        // Get all the stageList where poste not equals to UPDATED_POSTE
        defaultStageShouldBeFound("poste.notEquals=" + UPDATED_POSTE);
    }

    @Test
    @Transactional
    public void getAllStagesByPosteIsInShouldWork() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where poste in DEFAULT_POSTE or UPDATED_POSTE
        defaultStageShouldBeFound("poste.in=" + DEFAULT_POSTE + "," + UPDATED_POSTE);

        // Get all the stageList where poste equals to UPDATED_POSTE
        defaultStageShouldNotBeFound("poste.in=" + UPDATED_POSTE);
    }

    @Test
    @Transactional
    public void getAllStagesByPosteIsNullOrNotNull() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where poste is not null
        defaultStageShouldBeFound("poste.specified=true");

        // Get all the stageList where poste is null
        defaultStageShouldNotBeFound("poste.specified=false");
    }
                @Test
    @Transactional
    public void getAllStagesByPosteContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where poste contains DEFAULT_POSTE
        defaultStageShouldBeFound("poste.contains=" + DEFAULT_POSTE);

        // Get all the stageList where poste contains UPDATED_POSTE
        defaultStageShouldNotBeFound("poste.contains=" + UPDATED_POSTE);
    }

    @Test
    @Transactional
    public void getAllStagesByPosteNotContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where poste does not contain DEFAULT_POSTE
        defaultStageShouldNotBeFound("poste.doesNotContain=" + DEFAULT_POSTE);

        // Get all the stageList where poste does not contain UPDATED_POSTE
        defaultStageShouldBeFound("poste.doesNotContain=" + UPDATED_POSTE);
    }


    @Test
    @Transactional
    public void getAllStagesByEmployeurIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where employeur equals to DEFAULT_EMPLOYEUR
        defaultStageShouldBeFound("employeur.equals=" + DEFAULT_EMPLOYEUR);

        // Get all the stageList where employeur equals to UPDATED_EMPLOYEUR
        defaultStageShouldNotBeFound("employeur.equals=" + UPDATED_EMPLOYEUR);
    }

    @Test
    @Transactional
    public void getAllStagesByEmployeurIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where employeur not equals to DEFAULT_EMPLOYEUR
        defaultStageShouldNotBeFound("employeur.notEquals=" + DEFAULT_EMPLOYEUR);

        // Get all the stageList where employeur not equals to UPDATED_EMPLOYEUR
        defaultStageShouldBeFound("employeur.notEquals=" + UPDATED_EMPLOYEUR);
    }

    @Test
    @Transactional
    public void getAllStagesByEmployeurIsInShouldWork() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where employeur in DEFAULT_EMPLOYEUR or UPDATED_EMPLOYEUR
        defaultStageShouldBeFound("employeur.in=" + DEFAULT_EMPLOYEUR + "," + UPDATED_EMPLOYEUR);

        // Get all the stageList where employeur equals to UPDATED_EMPLOYEUR
        defaultStageShouldNotBeFound("employeur.in=" + UPDATED_EMPLOYEUR);
    }

    @Test
    @Transactional
    public void getAllStagesByEmployeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where employeur is not null
        defaultStageShouldBeFound("employeur.specified=true");

        // Get all the stageList where employeur is null
        defaultStageShouldNotBeFound("employeur.specified=false");
    }
                @Test
    @Transactional
    public void getAllStagesByEmployeurContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where employeur contains DEFAULT_EMPLOYEUR
        defaultStageShouldBeFound("employeur.contains=" + DEFAULT_EMPLOYEUR);

        // Get all the stageList where employeur contains UPDATED_EMPLOYEUR
        defaultStageShouldNotBeFound("employeur.contains=" + UPDATED_EMPLOYEUR);
    }

    @Test
    @Transactional
    public void getAllStagesByEmployeurNotContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where employeur does not contain DEFAULT_EMPLOYEUR
        defaultStageShouldNotBeFound("employeur.doesNotContain=" + DEFAULT_EMPLOYEUR);

        // Get all the stageList where employeur does not contain UPDATED_EMPLOYEUR
        defaultStageShouldBeFound("employeur.doesNotContain=" + UPDATED_EMPLOYEUR);
    }


    @Test
    @Transactional
    public void getAllStagesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where description equals to DEFAULT_DESCRIPTION
        defaultStageShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the stageList where description equals to UPDATED_DESCRIPTION
        defaultStageShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStagesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where description not equals to DEFAULT_DESCRIPTION
        defaultStageShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the stageList where description not equals to UPDATED_DESCRIPTION
        defaultStageShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStagesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultStageShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the stageList where description equals to UPDATED_DESCRIPTION
        defaultStageShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStagesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where description is not null
        defaultStageShouldBeFound("description.specified=true");

        // Get all the stageList where description is null
        defaultStageShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllStagesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where description contains DEFAULT_DESCRIPTION
        defaultStageShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the stageList where description contains UPDATED_DESCRIPTION
        defaultStageShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStagesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where description does not contain DEFAULT_DESCRIPTION
        defaultStageShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the stageList where description does not contain UPDATED_DESCRIPTION
        defaultStageShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllStagesByCvcorpIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);
        Cvcorp cvcorp = CvcorpResourceIT.createEntity(em);
        em.persist(cvcorp);
        em.flush();
        stage.setCvcorp(cvcorp);
        stageRepository.saveAndFlush(stage);
        Long cvcorpId = cvcorp.getId();

        // Get all the stageList where cvcorp equals to cvcorpId
        defaultStageShouldBeFound("cvcorpId.equals=" + cvcorpId);

        // Get all the stageList where cvcorp equals to cvcorpId + 1
        defaultStageShouldNotBeFound("cvcorpId.equals=" + (cvcorpId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStageShouldBeFound(String filter) throws Exception {
        restStageMockMvc.perform(get("/api/stages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stage.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodBigin").value(hasItem(DEFAULT_PERIOD_BIGIN.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].periodNow").value(hasItem(DEFAULT_PERIOD_NOW.booleanValue())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].poste").value(hasItem(DEFAULT_POSTE)))
            .andExpect(jsonPath("$.[*].employeur").value(hasItem(DEFAULT_EMPLOYEUR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restStageMockMvc.perform(get("/api/stages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStageShouldNotBeFound(String filter) throws Exception {
        restStageMockMvc.perform(get("/api/stages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStageMockMvc.perform(get("/api/stages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingStage() throws Exception {
        // Get the stage
        restStageMockMvc.perform(get("/api/stages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        int databaseSizeBeforeUpdate = stageRepository.findAll().size();

        // Update the stage
        Stage updatedStage = stageRepository.findById(stage.getId()).get();
        // Disconnect from session so that the updates on updatedStage are not directly saved in db
        em.detach(updatedStage);
        updatedStage
            .periodBigin(UPDATED_PERIOD_BIGIN)
            .periodEnd(UPDATED_PERIOD_END)
            .periodNow(UPDATED_PERIOD_NOW)
            .ville(UPDATED_VILLE)
            .poste(UPDATED_POSTE)
            .employeur(UPDATED_EMPLOYEUR)
            .description(UPDATED_DESCRIPTION);
        StageDTO stageDTO = stageMapper.toDto(updatedStage);

        restStageMockMvc.perform(put("/api/stages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stageDTO)))
            .andExpect(status().isOk());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
        Stage testStage = stageList.get(stageList.size() - 1);
        assertThat(testStage.getPeriodBigin()).isEqualTo(UPDATED_PERIOD_BIGIN);
        assertThat(testStage.getPeriodEnd()).isEqualTo(UPDATED_PERIOD_END);
        assertThat(testStage.isPeriodNow()).isEqualTo(UPDATED_PERIOD_NOW);
        assertThat(testStage.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testStage.getPoste()).isEqualTo(UPDATED_POSTE);
        assertThat(testStage.getEmployeur()).isEqualTo(UPDATED_EMPLOYEUR);
        assertThat(testStage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Stage in Elasticsearch
        verify(mockStageSearchRepository, times(1)).save(testStage);
    }

    @Test
    @Transactional
    public void updateNonExistingStage() throws Exception {
        int databaseSizeBeforeUpdate = stageRepository.findAll().size();

        // Create the Stage
        StageDTO stageDTO = stageMapper.toDto(stage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStageMockMvc.perform(put("/api/stages").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Stage in Elasticsearch
        verify(mockStageSearchRepository, times(0)).save(stage);
    }

    @Test
    @Transactional
    public void deleteStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        int databaseSizeBeforeDelete = stageRepository.findAll().size();

        // Delete the stage
        restStageMockMvc.perform(delete("/api/stages/{id}", stage.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Stage in Elasticsearch
        verify(mockStageSearchRepository, times(1)).deleteById(stage.getId());
    }

    @Test
    @Transactional
    public void searchStage() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        stageRepository.saveAndFlush(stage);
        when(mockStageSearchRepository.search(queryStringQuery("id:" + stage.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stage), PageRequest.of(0, 1), 1));

        // Search the stage
        restStageMockMvc.perform(get("/api/_search/stages?query=id:" + stage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stage.getId().intValue())))
            .andExpect(jsonPath("$.[*].periodBigin").value(hasItem(DEFAULT_PERIOD_BIGIN.toString())))
            .andExpect(jsonPath("$.[*].periodEnd").value(hasItem(DEFAULT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].periodNow").value(hasItem(DEFAULT_PERIOD_NOW.booleanValue())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].poste").value(hasItem(DEFAULT_POSTE)))
            .andExpect(jsonPath("$.[*].employeur").value(hasItem(DEFAULT_EMPLOYEUR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
