package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.CvcorpApp;
import com.cv.cvcorp.config.SecurityBeanOverrideConfiguration;
import com.cv.cvcorp.domain.Cvcorp;
import com.cv.cvcorp.domain.Competence;
import com.cv.cvcorp.domain.Experience;
import com.cv.cvcorp.domain.Formation;
import com.cv.cvcorp.domain.Langue;
import com.cv.cvcorp.domain.Stage;
import com.cv.cvcorp.repository.CvcorpRepository;
import com.cv.cvcorp.repository.search.CvcorpSearchRepository;
import com.cv.cvcorp.service.CvcorpService;
import com.cv.cvcorp.service.dto.CvcorpDTO;
import com.cv.cvcorp.service.mapper.CvcorpMapper;
import com.cv.cvcorp.service.dto.CvcorpCriteria;
import com.cv.cvcorp.service.CvcorpQueryService;

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
 * Integration tests for the {@link CvcorpResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, CvcorpApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CvcorpResourceIT {

    private static final String DEFAULT_PROFIL = "AAAAAAAAAA";
    private static final String UPDATED_PROFIL = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_NAISSENCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAISSENCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAISSANCE_LIEU = "AAAAAAAAAA";
    private static final String UPDATED_NAISSANCE_LIEU = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT_CIVIL = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_CIVIL = "BBBBBBBBBB";

    private static final String DEFAULT_LINKED_ID = "AAAAAAAAAA";
    private static final String UPDATED_LINKED_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_SEXE = "AAAAAAAAAA";
    private static final String UPDATED_SEXE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    @Autowired
    private CvcorpRepository cvcorpRepository;

    @Autowired
    private CvcorpMapper cvcorpMapper;

    @Autowired
    private CvcorpService cvcorpService;

    /**
     * This repository is mocked in the com.cv.cvcorp.repository.search test package.
     *
     * @see com.cv.cvcorp.repository.search.CvcorpSearchRepositoryMockConfiguration
     */
    @Autowired
    private CvcorpSearchRepository mockCvcorpSearchRepository;

    @Autowired
    private CvcorpQueryService cvcorpQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCvcorpMockMvc;

    private Cvcorp cvcorp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cvcorp createEntity(EntityManager em) {
        Cvcorp cvcorp = new Cvcorp()
            .profil(DEFAULT_PROFIL)
            .pays(DEFAULT_PAYS)
            .ville(DEFAULT_VILLE)
            .dateNaissence(DEFAULT_DATE_NAISSENCE)
            .naissanceLieu(DEFAULT_NAISSANCE_LIEU)
            .etatCivil(DEFAULT_ETAT_CIVIL)
            .linkedId(DEFAULT_LINKED_ID)
            .adresse(DEFAULT_ADRESSE)
            .sexe(DEFAULT_SEXE)
            .codePostal(DEFAULT_CODE_POSTAL);
        return cvcorp;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cvcorp createUpdatedEntity(EntityManager em) {
        Cvcorp cvcorp = new Cvcorp()
            .profil(UPDATED_PROFIL)
            .pays(UPDATED_PAYS)
            .ville(UPDATED_VILLE)
            .dateNaissence(UPDATED_DATE_NAISSENCE)
            .naissanceLieu(UPDATED_NAISSANCE_LIEU)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .linkedId(UPDATED_LINKED_ID)
            .adresse(UPDATED_ADRESSE)
            .sexe(UPDATED_SEXE)
            .codePostal(UPDATED_CODE_POSTAL);
        return cvcorp;
    }

    @BeforeEach
    public void initTest() {
        cvcorp = createEntity(em);
    }

    @Test
    @Transactional
    public void createCvcorp() throws Exception {
        int databaseSizeBeforeCreate = cvcorpRepository.findAll().size();
        // Create the Cvcorp
        CvcorpDTO cvcorpDTO = cvcorpMapper.toDto(cvcorp);
        restCvcorpMockMvc.perform(post("/api/cvcorps").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cvcorpDTO)))
            .andExpect(status().isCreated());

        // Validate the Cvcorp in the database
        List<Cvcorp> cvcorpList = cvcorpRepository.findAll();
        assertThat(cvcorpList).hasSize(databaseSizeBeforeCreate + 1);
        Cvcorp testCvcorp = cvcorpList.get(cvcorpList.size() - 1);
        assertThat(testCvcorp.getProfil()).isEqualTo(DEFAULT_PROFIL);
        assertThat(testCvcorp.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testCvcorp.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testCvcorp.getDateNaissence()).isEqualTo(DEFAULT_DATE_NAISSENCE);
        assertThat(testCvcorp.getNaissanceLieu()).isEqualTo(DEFAULT_NAISSANCE_LIEU);
        assertThat(testCvcorp.getEtatCivil()).isEqualTo(DEFAULT_ETAT_CIVIL);
        assertThat(testCvcorp.getLinkedId()).isEqualTo(DEFAULT_LINKED_ID);
        assertThat(testCvcorp.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testCvcorp.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testCvcorp.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);

        // Validate the Cvcorp in Elasticsearch
        verify(mockCvcorpSearchRepository, times(1)).save(testCvcorp);
    }

    @Test
    @Transactional
    public void createCvcorpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cvcorpRepository.findAll().size();

        // Create the Cvcorp with an existing ID
        cvcorp.setId(1L);
        CvcorpDTO cvcorpDTO = cvcorpMapper.toDto(cvcorp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCvcorpMockMvc.perform(post("/api/cvcorps").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cvcorpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cvcorp in the database
        List<Cvcorp> cvcorpList = cvcorpRepository.findAll();
        assertThat(cvcorpList).hasSize(databaseSizeBeforeCreate);

        // Validate the Cvcorp in Elasticsearch
        verify(mockCvcorpSearchRepository, times(0)).save(cvcorp);
    }


    @Test
    @Transactional
    public void getAllCvcorps() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList
        restCvcorpMockMvc.perform(get("/api/cvcorps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cvcorp.getId().intValue())))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(DEFAULT_PROFIL)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].dateNaissence").value(hasItem(DEFAULT_DATE_NAISSENCE.toString())))
            .andExpect(jsonPath("$.[*].naissanceLieu").value(hasItem(DEFAULT_NAISSANCE_LIEU)))
            .andExpect(jsonPath("$.[*].etatCivil").value(hasItem(DEFAULT_ETAT_CIVIL)))
            .andExpect(jsonPath("$.[*].linkedId").value(hasItem(DEFAULT_LINKED_ID)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)));
    }
    
    @Test
    @Transactional
    public void getCvcorp() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get the cvcorp
        restCvcorpMockMvc.perform(get("/api/cvcorps/{id}", cvcorp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cvcorp.getId().intValue()))
            .andExpect(jsonPath("$.profil").value(DEFAULT_PROFIL))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.dateNaissence").value(DEFAULT_DATE_NAISSENCE.toString()))
            .andExpect(jsonPath("$.naissanceLieu").value(DEFAULT_NAISSANCE_LIEU))
            .andExpect(jsonPath("$.etatCivil").value(DEFAULT_ETAT_CIVIL))
            .andExpect(jsonPath("$.linkedId").value(DEFAULT_LINKED_ID))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL));
    }


    @Test
    @Transactional
    public void getCvcorpsByIdFiltering() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        Long id = cvcorp.getId();

        defaultCvcorpShouldBeFound("id.equals=" + id);
        defaultCvcorpShouldNotBeFound("id.notEquals=" + id);

        defaultCvcorpShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCvcorpShouldNotBeFound("id.greaterThan=" + id);

        defaultCvcorpShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCvcorpShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCvcorpsByProfilIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where profil equals to DEFAULT_PROFIL
        defaultCvcorpShouldBeFound("profil.equals=" + DEFAULT_PROFIL);

        // Get all the cvcorpList where profil equals to UPDATED_PROFIL
        defaultCvcorpShouldNotBeFound("profil.equals=" + UPDATED_PROFIL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByProfilIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where profil not equals to DEFAULT_PROFIL
        defaultCvcorpShouldNotBeFound("profil.notEquals=" + DEFAULT_PROFIL);

        // Get all the cvcorpList where profil not equals to UPDATED_PROFIL
        defaultCvcorpShouldBeFound("profil.notEquals=" + UPDATED_PROFIL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByProfilIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where profil in DEFAULT_PROFIL or UPDATED_PROFIL
        defaultCvcorpShouldBeFound("profil.in=" + DEFAULT_PROFIL + "," + UPDATED_PROFIL);

        // Get all the cvcorpList where profil equals to UPDATED_PROFIL
        defaultCvcorpShouldNotBeFound("profil.in=" + UPDATED_PROFIL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByProfilIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where profil is not null
        defaultCvcorpShouldBeFound("profil.specified=true");

        // Get all the cvcorpList where profil is null
        defaultCvcorpShouldNotBeFound("profil.specified=false");
    }
                @Test
    @Transactional
    public void getAllCvcorpsByProfilContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where profil contains DEFAULT_PROFIL
        defaultCvcorpShouldBeFound("profil.contains=" + DEFAULT_PROFIL);

        // Get all the cvcorpList where profil contains UPDATED_PROFIL
        defaultCvcorpShouldNotBeFound("profil.contains=" + UPDATED_PROFIL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByProfilNotContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where profil does not contain DEFAULT_PROFIL
        defaultCvcorpShouldNotBeFound("profil.doesNotContain=" + DEFAULT_PROFIL);

        // Get all the cvcorpList where profil does not contain UPDATED_PROFIL
        defaultCvcorpShouldBeFound("profil.doesNotContain=" + UPDATED_PROFIL);
    }


    @Test
    @Transactional
    public void getAllCvcorpsByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where pays equals to DEFAULT_PAYS
        defaultCvcorpShouldBeFound("pays.equals=" + DEFAULT_PAYS);

        // Get all the cvcorpList where pays equals to UPDATED_PAYS
        defaultCvcorpShouldNotBeFound("pays.equals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByPaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where pays not equals to DEFAULT_PAYS
        defaultCvcorpShouldNotBeFound("pays.notEquals=" + DEFAULT_PAYS);

        // Get all the cvcorpList where pays not equals to UPDATED_PAYS
        defaultCvcorpShouldBeFound("pays.notEquals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByPaysIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where pays in DEFAULT_PAYS or UPDATED_PAYS
        defaultCvcorpShouldBeFound("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS);

        // Get all the cvcorpList where pays equals to UPDATED_PAYS
        defaultCvcorpShouldNotBeFound("pays.in=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByPaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where pays is not null
        defaultCvcorpShouldBeFound("pays.specified=true");

        // Get all the cvcorpList where pays is null
        defaultCvcorpShouldNotBeFound("pays.specified=false");
    }
                @Test
    @Transactional
    public void getAllCvcorpsByPaysContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where pays contains DEFAULT_PAYS
        defaultCvcorpShouldBeFound("pays.contains=" + DEFAULT_PAYS);

        // Get all the cvcorpList where pays contains UPDATED_PAYS
        defaultCvcorpShouldNotBeFound("pays.contains=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByPaysNotContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where pays does not contain DEFAULT_PAYS
        defaultCvcorpShouldNotBeFound("pays.doesNotContain=" + DEFAULT_PAYS);

        // Get all the cvcorpList where pays does not contain UPDATED_PAYS
        defaultCvcorpShouldBeFound("pays.doesNotContain=" + UPDATED_PAYS);
    }


    @Test
    @Transactional
    public void getAllCvcorpsByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where ville equals to DEFAULT_VILLE
        defaultCvcorpShouldBeFound("ville.equals=" + DEFAULT_VILLE);

        // Get all the cvcorpList where ville equals to UPDATED_VILLE
        defaultCvcorpShouldNotBeFound("ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByVilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where ville not equals to DEFAULT_VILLE
        defaultCvcorpShouldNotBeFound("ville.notEquals=" + DEFAULT_VILLE);

        // Get all the cvcorpList where ville not equals to UPDATED_VILLE
        defaultCvcorpShouldBeFound("ville.notEquals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where ville in DEFAULT_VILLE or UPDATED_VILLE
        defaultCvcorpShouldBeFound("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE);

        // Get all the cvcorpList where ville equals to UPDATED_VILLE
        defaultCvcorpShouldNotBeFound("ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where ville is not null
        defaultCvcorpShouldBeFound("ville.specified=true");

        // Get all the cvcorpList where ville is null
        defaultCvcorpShouldNotBeFound("ville.specified=false");
    }
                @Test
    @Transactional
    public void getAllCvcorpsByVilleContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where ville contains DEFAULT_VILLE
        defaultCvcorpShouldBeFound("ville.contains=" + DEFAULT_VILLE);

        // Get all the cvcorpList where ville contains UPDATED_VILLE
        defaultCvcorpShouldNotBeFound("ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where ville does not contain DEFAULT_VILLE
        defaultCvcorpShouldNotBeFound("ville.doesNotContain=" + DEFAULT_VILLE);

        // Get all the cvcorpList where ville does not contain UPDATED_VILLE
        defaultCvcorpShouldBeFound("ville.doesNotContain=" + UPDATED_VILLE);
    }


    @Test
    @Transactional
    public void getAllCvcorpsByDateNaissenceIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where dateNaissence equals to DEFAULT_DATE_NAISSENCE
        defaultCvcorpShouldBeFound("dateNaissence.equals=" + DEFAULT_DATE_NAISSENCE);

        // Get all the cvcorpList where dateNaissence equals to UPDATED_DATE_NAISSENCE
        defaultCvcorpShouldNotBeFound("dateNaissence.equals=" + UPDATED_DATE_NAISSENCE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByDateNaissenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where dateNaissence not equals to DEFAULT_DATE_NAISSENCE
        defaultCvcorpShouldNotBeFound("dateNaissence.notEquals=" + DEFAULT_DATE_NAISSENCE);

        // Get all the cvcorpList where dateNaissence not equals to UPDATED_DATE_NAISSENCE
        defaultCvcorpShouldBeFound("dateNaissence.notEquals=" + UPDATED_DATE_NAISSENCE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByDateNaissenceIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where dateNaissence in DEFAULT_DATE_NAISSENCE or UPDATED_DATE_NAISSENCE
        defaultCvcorpShouldBeFound("dateNaissence.in=" + DEFAULT_DATE_NAISSENCE + "," + UPDATED_DATE_NAISSENCE);

        // Get all the cvcorpList where dateNaissence equals to UPDATED_DATE_NAISSENCE
        defaultCvcorpShouldNotBeFound("dateNaissence.in=" + UPDATED_DATE_NAISSENCE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByDateNaissenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where dateNaissence is not null
        defaultCvcorpShouldBeFound("dateNaissence.specified=true");

        // Get all the cvcorpList where dateNaissence is null
        defaultCvcorpShouldNotBeFound("dateNaissence.specified=false");
    }

    @Test
    @Transactional
    public void getAllCvcorpsByNaissanceLieuIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where naissanceLieu equals to DEFAULT_NAISSANCE_LIEU
        defaultCvcorpShouldBeFound("naissanceLieu.equals=" + DEFAULT_NAISSANCE_LIEU);

        // Get all the cvcorpList where naissanceLieu equals to UPDATED_NAISSANCE_LIEU
        defaultCvcorpShouldNotBeFound("naissanceLieu.equals=" + UPDATED_NAISSANCE_LIEU);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByNaissanceLieuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where naissanceLieu not equals to DEFAULT_NAISSANCE_LIEU
        defaultCvcorpShouldNotBeFound("naissanceLieu.notEquals=" + DEFAULT_NAISSANCE_LIEU);

        // Get all the cvcorpList where naissanceLieu not equals to UPDATED_NAISSANCE_LIEU
        defaultCvcorpShouldBeFound("naissanceLieu.notEquals=" + UPDATED_NAISSANCE_LIEU);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByNaissanceLieuIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where naissanceLieu in DEFAULT_NAISSANCE_LIEU or UPDATED_NAISSANCE_LIEU
        defaultCvcorpShouldBeFound("naissanceLieu.in=" + DEFAULT_NAISSANCE_LIEU + "," + UPDATED_NAISSANCE_LIEU);

        // Get all the cvcorpList where naissanceLieu equals to UPDATED_NAISSANCE_LIEU
        defaultCvcorpShouldNotBeFound("naissanceLieu.in=" + UPDATED_NAISSANCE_LIEU);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByNaissanceLieuIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where naissanceLieu is not null
        defaultCvcorpShouldBeFound("naissanceLieu.specified=true");

        // Get all the cvcorpList where naissanceLieu is null
        defaultCvcorpShouldNotBeFound("naissanceLieu.specified=false");
    }
                @Test
    @Transactional
    public void getAllCvcorpsByNaissanceLieuContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where naissanceLieu contains DEFAULT_NAISSANCE_LIEU
        defaultCvcorpShouldBeFound("naissanceLieu.contains=" + DEFAULT_NAISSANCE_LIEU);

        // Get all the cvcorpList where naissanceLieu contains UPDATED_NAISSANCE_LIEU
        defaultCvcorpShouldNotBeFound("naissanceLieu.contains=" + UPDATED_NAISSANCE_LIEU);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByNaissanceLieuNotContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where naissanceLieu does not contain DEFAULT_NAISSANCE_LIEU
        defaultCvcorpShouldNotBeFound("naissanceLieu.doesNotContain=" + DEFAULT_NAISSANCE_LIEU);

        // Get all the cvcorpList where naissanceLieu does not contain UPDATED_NAISSANCE_LIEU
        defaultCvcorpShouldBeFound("naissanceLieu.doesNotContain=" + UPDATED_NAISSANCE_LIEU);
    }


    @Test
    @Transactional
    public void getAllCvcorpsByEtatCivilIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where etatCivil equals to DEFAULT_ETAT_CIVIL
        defaultCvcorpShouldBeFound("etatCivil.equals=" + DEFAULT_ETAT_CIVIL);

        // Get all the cvcorpList where etatCivil equals to UPDATED_ETAT_CIVIL
        defaultCvcorpShouldNotBeFound("etatCivil.equals=" + UPDATED_ETAT_CIVIL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByEtatCivilIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where etatCivil not equals to DEFAULT_ETAT_CIVIL
        defaultCvcorpShouldNotBeFound("etatCivil.notEquals=" + DEFAULT_ETAT_CIVIL);

        // Get all the cvcorpList where etatCivil not equals to UPDATED_ETAT_CIVIL
        defaultCvcorpShouldBeFound("etatCivil.notEquals=" + UPDATED_ETAT_CIVIL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByEtatCivilIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where etatCivil in DEFAULT_ETAT_CIVIL or UPDATED_ETAT_CIVIL
        defaultCvcorpShouldBeFound("etatCivil.in=" + DEFAULT_ETAT_CIVIL + "," + UPDATED_ETAT_CIVIL);

        // Get all the cvcorpList where etatCivil equals to UPDATED_ETAT_CIVIL
        defaultCvcorpShouldNotBeFound("etatCivil.in=" + UPDATED_ETAT_CIVIL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByEtatCivilIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where etatCivil is not null
        defaultCvcorpShouldBeFound("etatCivil.specified=true");

        // Get all the cvcorpList where etatCivil is null
        defaultCvcorpShouldNotBeFound("etatCivil.specified=false");
    }
                @Test
    @Transactional
    public void getAllCvcorpsByEtatCivilContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where etatCivil contains DEFAULT_ETAT_CIVIL
        defaultCvcorpShouldBeFound("etatCivil.contains=" + DEFAULT_ETAT_CIVIL);

        // Get all the cvcorpList where etatCivil contains UPDATED_ETAT_CIVIL
        defaultCvcorpShouldNotBeFound("etatCivil.contains=" + UPDATED_ETAT_CIVIL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByEtatCivilNotContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where etatCivil does not contain DEFAULT_ETAT_CIVIL
        defaultCvcorpShouldNotBeFound("etatCivil.doesNotContain=" + DEFAULT_ETAT_CIVIL);

        // Get all the cvcorpList where etatCivil does not contain UPDATED_ETAT_CIVIL
        defaultCvcorpShouldBeFound("etatCivil.doesNotContain=" + UPDATED_ETAT_CIVIL);
    }


    @Test
    @Transactional
    public void getAllCvcorpsByLinkedIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where linkedId equals to DEFAULT_LINKED_ID
        defaultCvcorpShouldBeFound("linkedId.equals=" + DEFAULT_LINKED_ID);

        // Get all the cvcorpList where linkedId equals to UPDATED_LINKED_ID
        defaultCvcorpShouldNotBeFound("linkedId.equals=" + UPDATED_LINKED_ID);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByLinkedIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where linkedId not equals to DEFAULT_LINKED_ID
        defaultCvcorpShouldNotBeFound("linkedId.notEquals=" + DEFAULT_LINKED_ID);

        // Get all the cvcorpList where linkedId not equals to UPDATED_LINKED_ID
        defaultCvcorpShouldBeFound("linkedId.notEquals=" + UPDATED_LINKED_ID);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByLinkedIdIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where linkedId in DEFAULT_LINKED_ID or UPDATED_LINKED_ID
        defaultCvcorpShouldBeFound("linkedId.in=" + DEFAULT_LINKED_ID + "," + UPDATED_LINKED_ID);

        // Get all the cvcorpList where linkedId equals to UPDATED_LINKED_ID
        defaultCvcorpShouldNotBeFound("linkedId.in=" + UPDATED_LINKED_ID);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByLinkedIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where linkedId is not null
        defaultCvcorpShouldBeFound("linkedId.specified=true");

        // Get all the cvcorpList where linkedId is null
        defaultCvcorpShouldNotBeFound("linkedId.specified=false");
    }
                @Test
    @Transactional
    public void getAllCvcorpsByLinkedIdContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where linkedId contains DEFAULT_LINKED_ID
        defaultCvcorpShouldBeFound("linkedId.contains=" + DEFAULT_LINKED_ID);

        // Get all the cvcorpList where linkedId contains UPDATED_LINKED_ID
        defaultCvcorpShouldNotBeFound("linkedId.contains=" + UPDATED_LINKED_ID);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByLinkedIdNotContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where linkedId does not contain DEFAULT_LINKED_ID
        defaultCvcorpShouldNotBeFound("linkedId.doesNotContain=" + DEFAULT_LINKED_ID);

        // Get all the cvcorpList where linkedId does not contain UPDATED_LINKED_ID
        defaultCvcorpShouldBeFound("linkedId.doesNotContain=" + UPDATED_LINKED_ID);
    }


    @Test
    @Transactional
    public void getAllCvcorpsByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where adresse equals to DEFAULT_ADRESSE
        defaultCvcorpShouldBeFound("adresse.equals=" + DEFAULT_ADRESSE);

        // Get all the cvcorpList where adresse equals to UPDATED_ADRESSE
        defaultCvcorpShouldNotBeFound("adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByAdresseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where adresse not equals to DEFAULT_ADRESSE
        defaultCvcorpShouldNotBeFound("adresse.notEquals=" + DEFAULT_ADRESSE);

        // Get all the cvcorpList where adresse not equals to UPDATED_ADRESSE
        defaultCvcorpShouldBeFound("adresse.notEquals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where adresse in DEFAULT_ADRESSE or UPDATED_ADRESSE
        defaultCvcorpShouldBeFound("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE);

        // Get all the cvcorpList where adresse equals to UPDATED_ADRESSE
        defaultCvcorpShouldNotBeFound("adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where adresse is not null
        defaultCvcorpShouldBeFound("adresse.specified=true");

        // Get all the cvcorpList where adresse is null
        defaultCvcorpShouldNotBeFound("adresse.specified=false");
    }
                @Test
    @Transactional
    public void getAllCvcorpsByAdresseContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where adresse contains DEFAULT_ADRESSE
        defaultCvcorpShouldBeFound("adresse.contains=" + DEFAULT_ADRESSE);

        // Get all the cvcorpList where adresse contains UPDATED_ADRESSE
        defaultCvcorpShouldNotBeFound("adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where adresse does not contain DEFAULT_ADRESSE
        defaultCvcorpShouldNotBeFound("adresse.doesNotContain=" + DEFAULT_ADRESSE);

        // Get all the cvcorpList where adresse does not contain UPDATED_ADRESSE
        defaultCvcorpShouldBeFound("adresse.doesNotContain=" + UPDATED_ADRESSE);
    }


    @Test
    @Transactional
    public void getAllCvcorpsBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where sexe equals to DEFAULT_SEXE
        defaultCvcorpShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the cvcorpList where sexe equals to UPDATED_SEXE
        defaultCvcorpShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where sexe not equals to DEFAULT_SEXE
        defaultCvcorpShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the cvcorpList where sexe not equals to UPDATED_SEXE
        defaultCvcorpShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultCvcorpShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the cvcorpList where sexe equals to UPDATED_SEXE
        defaultCvcorpShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where sexe is not null
        defaultCvcorpShouldBeFound("sexe.specified=true");

        // Get all the cvcorpList where sexe is null
        defaultCvcorpShouldNotBeFound("sexe.specified=false");
    }
                @Test
    @Transactional
    public void getAllCvcorpsBySexeContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where sexe contains DEFAULT_SEXE
        defaultCvcorpShouldBeFound("sexe.contains=" + DEFAULT_SEXE);

        // Get all the cvcorpList where sexe contains UPDATED_SEXE
        defaultCvcorpShouldNotBeFound("sexe.contains=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllCvcorpsBySexeNotContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where sexe does not contain DEFAULT_SEXE
        defaultCvcorpShouldNotBeFound("sexe.doesNotContain=" + DEFAULT_SEXE);

        // Get all the cvcorpList where sexe does not contain UPDATED_SEXE
        defaultCvcorpShouldBeFound("sexe.doesNotContain=" + UPDATED_SEXE);
    }


    @Test
    @Transactional
    public void getAllCvcorpsByCodePostalIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where codePostal equals to DEFAULT_CODE_POSTAL
        defaultCvcorpShouldBeFound("codePostal.equals=" + DEFAULT_CODE_POSTAL);

        // Get all the cvcorpList where codePostal equals to UPDATED_CODE_POSTAL
        defaultCvcorpShouldNotBeFound("codePostal.equals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByCodePostalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where codePostal not equals to DEFAULT_CODE_POSTAL
        defaultCvcorpShouldNotBeFound("codePostal.notEquals=" + DEFAULT_CODE_POSTAL);

        // Get all the cvcorpList where codePostal not equals to UPDATED_CODE_POSTAL
        defaultCvcorpShouldBeFound("codePostal.notEquals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByCodePostalIsInShouldWork() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where codePostal in DEFAULT_CODE_POSTAL or UPDATED_CODE_POSTAL
        defaultCvcorpShouldBeFound("codePostal.in=" + DEFAULT_CODE_POSTAL + "," + UPDATED_CODE_POSTAL);

        // Get all the cvcorpList where codePostal equals to UPDATED_CODE_POSTAL
        defaultCvcorpShouldNotBeFound("codePostal.in=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByCodePostalIsNullOrNotNull() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where codePostal is not null
        defaultCvcorpShouldBeFound("codePostal.specified=true");

        // Get all the cvcorpList where codePostal is null
        defaultCvcorpShouldNotBeFound("codePostal.specified=false");
    }
                @Test
    @Transactional
    public void getAllCvcorpsByCodePostalContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where codePostal contains DEFAULT_CODE_POSTAL
        defaultCvcorpShouldBeFound("codePostal.contains=" + DEFAULT_CODE_POSTAL);

        // Get all the cvcorpList where codePostal contains UPDATED_CODE_POSTAL
        defaultCvcorpShouldNotBeFound("codePostal.contains=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllCvcorpsByCodePostalNotContainsSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        // Get all the cvcorpList where codePostal does not contain DEFAULT_CODE_POSTAL
        defaultCvcorpShouldNotBeFound("codePostal.doesNotContain=" + DEFAULT_CODE_POSTAL);

        // Get all the cvcorpList where codePostal does not contain UPDATED_CODE_POSTAL
        defaultCvcorpShouldBeFound("codePostal.doesNotContain=" + UPDATED_CODE_POSTAL);
    }


    @Test
    @Transactional
    public void getAllCvcorpsByCompetenceIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);
        Competence competence = CompetenceResourceIT.createEntity(em);
        em.persist(competence);
        em.flush();
        cvcorp.addCompetence(competence);
        cvcorpRepository.saveAndFlush(cvcorp);
        Long competenceId = competence.getId();

        // Get all the cvcorpList where competence equals to competenceId
        defaultCvcorpShouldBeFound("competenceId.equals=" + competenceId);

        // Get all the cvcorpList where competence equals to competenceId + 1
        defaultCvcorpShouldNotBeFound("competenceId.equals=" + (competenceId + 1));
    }


    @Test
    @Transactional
    public void getAllCvcorpsByExperienceIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);
        Experience experience = ExperienceResourceIT.createEntity(em);
        em.persist(experience);
        em.flush();
        cvcorp.addExperience(experience);
        cvcorpRepository.saveAndFlush(cvcorp);
        Long experienceId = experience.getId();

        // Get all the cvcorpList where experience equals to experienceId
        defaultCvcorpShouldBeFound("experienceId.equals=" + experienceId);

        // Get all the cvcorpList where experience equals to experienceId + 1
        defaultCvcorpShouldNotBeFound("experienceId.equals=" + (experienceId + 1));
    }


    @Test
    @Transactional
    public void getAllCvcorpsByFormationIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);
        Formation formation = FormationResourceIT.createEntity(em);
        em.persist(formation);
        em.flush();
        cvcorp.addFormation(formation);
        cvcorpRepository.saveAndFlush(cvcorp);
        Long formationId = formation.getId();

        // Get all the cvcorpList where formation equals to formationId
        defaultCvcorpShouldBeFound("formationId.equals=" + formationId);

        // Get all the cvcorpList where formation equals to formationId + 1
        defaultCvcorpShouldNotBeFound("formationId.equals=" + (formationId + 1));
    }


    @Test
    @Transactional
    public void getAllCvcorpsByLangueIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);
        Langue langue = LangueResourceIT.createEntity(em);
        em.persist(langue);
        em.flush();
        cvcorp.addLangue(langue);
        cvcorpRepository.saveAndFlush(cvcorp);
        Long langueId = langue.getId();

        // Get all the cvcorpList where langue equals to langueId
        defaultCvcorpShouldBeFound("langueId.equals=" + langueId);

        // Get all the cvcorpList where langue equals to langueId + 1
        defaultCvcorpShouldNotBeFound("langueId.equals=" + (langueId + 1));
    }


    @Test
    @Transactional
    public void getAllCvcorpsByStageIsEqualToSomething() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);
        Stage stage = StageResourceIT.createEntity(em);
        em.persist(stage);
        em.flush();
        cvcorp.addStage(stage);
        cvcorpRepository.saveAndFlush(cvcorp);
        Long stageId = stage.getId();

        // Get all the cvcorpList where stage equals to stageId
        defaultCvcorpShouldBeFound("stageId.equals=" + stageId);

        // Get all the cvcorpList where stage equals to stageId + 1
        defaultCvcorpShouldNotBeFound("stageId.equals=" + (stageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCvcorpShouldBeFound(String filter) throws Exception {
        restCvcorpMockMvc.perform(get("/api/cvcorps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cvcorp.getId().intValue())))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(DEFAULT_PROFIL)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].dateNaissence").value(hasItem(DEFAULT_DATE_NAISSENCE.toString())))
            .andExpect(jsonPath("$.[*].naissanceLieu").value(hasItem(DEFAULT_NAISSANCE_LIEU)))
            .andExpect(jsonPath("$.[*].etatCivil").value(hasItem(DEFAULT_ETAT_CIVIL)))
            .andExpect(jsonPath("$.[*].linkedId").value(hasItem(DEFAULT_LINKED_ID)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)));

        // Check, that the count call also returns 1
        restCvcorpMockMvc.perform(get("/api/cvcorps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCvcorpShouldNotBeFound(String filter) throws Exception {
        restCvcorpMockMvc.perform(get("/api/cvcorps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCvcorpMockMvc.perform(get("/api/cvcorps/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCvcorp() throws Exception {
        // Get the cvcorp
        restCvcorpMockMvc.perform(get("/api/cvcorps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCvcorp() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        int databaseSizeBeforeUpdate = cvcorpRepository.findAll().size();

        // Update the cvcorp
        Cvcorp updatedCvcorp = cvcorpRepository.findById(cvcorp.getId()).get();
        // Disconnect from session so that the updates on updatedCvcorp are not directly saved in db
        em.detach(updatedCvcorp);
        updatedCvcorp
            .profil(UPDATED_PROFIL)
            .pays(UPDATED_PAYS)
            .ville(UPDATED_VILLE)
            .dateNaissence(UPDATED_DATE_NAISSENCE)
            .naissanceLieu(UPDATED_NAISSANCE_LIEU)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .linkedId(UPDATED_LINKED_ID)
            .adresse(UPDATED_ADRESSE)
            .sexe(UPDATED_SEXE)
            .codePostal(UPDATED_CODE_POSTAL);
        CvcorpDTO cvcorpDTO = cvcorpMapper.toDto(updatedCvcorp);

        restCvcorpMockMvc.perform(put("/api/cvcorps").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cvcorpDTO)))
            .andExpect(status().isOk());

        // Validate the Cvcorp in the database
        List<Cvcorp> cvcorpList = cvcorpRepository.findAll();
        assertThat(cvcorpList).hasSize(databaseSizeBeforeUpdate);
        Cvcorp testCvcorp = cvcorpList.get(cvcorpList.size() - 1);
        assertThat(testCvcorp.getProfil()).isEqualTo(UPDATED_PROFIL);
        assertThat(testCvcorp.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testCvcorp.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testCvcorp.getDateNaissence()).isEqualTo(UPDATED_DATE_NAISSENCE);
        assertThat(testCvcorp.getNaissanceLieu()).isEqualTo(UPDATED_NAISSANCE_LIEU);
        assertThat(testCvcorp.getEtatCivil()).isEqualTo(UPDATED_ETAT_CIVIL);
        assertThat(testCvcorp.getLinkedId()).isEqualTo(UPDATED_LINKED_ID);
        assertThat(testCvcorp.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testCvcorp.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testCvcorp.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);

        // Validate the Cvcorp in Elasticsearch
        verify(mockCvcorpSearchRepository, times(1)).save(testCvcorp);
    }

    @Test
    @Transactional
    public void updateNonExistingCvcorp() throws Exception {
        int databaseSizeBeforeUpdate = cvcorpRepository.findAll().size();

        // Create the Cvcorp
        CvcorpDTO cvcorpDTO = cvcorpMapper.toDto(cvcorp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCvcorpMockMvc.perform(put("/api/cvcorps").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cvcorpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cvcorp in the database
        List<Cvcorp> cvcorpList = cvcorpRepository.findAll();
        assertThat(cvcorpList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Cvcorp in Elasticsearch
        verify(mockCvcorpSearchRepository, times(0)).save(cvcorp);
    }

    @Test
    @Transactional
    public void deleteCvcorp() throws Exception {
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);

        int databaseSizeBeforeDelete = cvcorpRepository.findAll().size();

        // Delete the cvcorp
        restCvcorpMockMvc.perform(delete("/api/cvcorps/{id}", cvcorp.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cvcorp> cvcorpList = cvcorpRepository.findAll();
        assertThat(cvcorpList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Cvcorp in Elasticsearch
        verify(mockCvcorpSearchRepository, times(1)).deleteById(cvcorp.getId());
    }

    @Test
    @Transactional
    public void searchCvcorp() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cvcorpRepository.saveAndFlush(cvcorp);
        when(mockCvcorpSearchRepository.search(queryStringQuery("id:" + cvcorp.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cvcorp), PageRequest.of(0, 1), 1));

        // Search the cvcorp
        restCvcorpMockMvc.perform(get("/api/_search/cvcorps?query=id:" + cvcorp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cvcorp.getId().intValue())))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(DEFAULT_PROFIL)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].dateNaissence").value(hasItem(DEFAULT_DATE_NAISSENCE.toString())))
            .andExpect(jsonPath("$.[*].naissanceLieu").value(hasItem(DEFAULT_NAISSANCE_LIEU)))
            .andExpect(jsonPath("$.[*].etatCivil").value(hasItem(DEFAULT_ETAT_CIVIL)))
            .andExpect(jsonPath("$.[*].linkedId").value(hasItem(DEFAULT_LINKED_ID)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)));
    }
}
