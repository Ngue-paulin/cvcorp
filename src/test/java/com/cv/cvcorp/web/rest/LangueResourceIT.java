package com.cv.cvcorp.web.rest;

import com.cv.cvcorp.CvcorpApp;
import com.cv.cvcorp.config.SecurityBeanOverrideConfiguration;
import com.cv.cvcorp.domain.Langue;
import com.cv.cvcorp.domain.Cvcorp;
import com.cv.cvcorp.repository.LangueRepository;
import com.cv.cvcorp.repository.search.LangueSearchRepository;
import com.cv.cvcorp.service.LangueService;
import com.cv.cvcorp.service.dto.LangueDTO;
import com.cv.cvcorp.service.mapper.LangueMapper;
import com.cv.cvcorp.service.dto.LangueCriteria;
import com.cv.cvcorp.service.LangueQueryService;

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
 * Integration tests for the {@link LangueResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, CvcorpApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LangueResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final Niveau DEFAULT_NIVEAU = Niveau.DEBUTANT;
    private static final Niveau UPDATED_NIVEAU = Niveau.INTERMEDIAIR;

    @Autowired
    private LangueRepository langueRepository;

    @Autowired
    private LangueMapper langueMapper;

    @Autowired
    private LangueService langueService;

    /**
     * This repository is mocked in the com.cv.cvcorp.repository.search test package.
     *
     * @see com.cv.cvcorp.repository.search.LangueSearchRepositoryMockConfiguration
     */
    @Autowired
    private LangueSearchRepository mockLangueSearchRepository;

    @Autowired
    private LangueQueryService langueQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLangueMockMvc;

    private Langue langue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Langue createEntity(EntityManager em) {
        Langue langue = new Langue()
            .description(DEFAULT_DESCRIPTION)
            .libele(DEFAULT_LIBELE)
            .niveau(DEFAULT_NIVEAU);
        return langue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Langue createUpdatedEntity(EntityManager em) {
        Langue langue = new Langue()
            .description(UPDATED_DESCRIPTION)
            .libele(UPDATED_LIBELE)
            .niveau(UPDATED_NIVEAU);
        return langue;
    }

    @BeforeEach
    public void initTest() {
        langue = createEntity(em);
    }

    @Test
    @Transactional
    public void createLangue() throws Exception {
        int databaseSizeBeforeCreate = langueRepository.findAll().size();
        // Create the Langue
        LangueDTO langueDTO = langueMapper.toDto(langue);
        restLangueMockMvc.perform(post("/api/langues").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(langueDTO)))
            .andExpect(status().isCreated());

        // Validate the Langue in the database
        List<Langue> langueList = langueRepository.findAll();
        assertThat(langueList).hasSize(databaseSizeBeforeCreate + 1);
        Langue testLangue = langueList.get(langueList.size() - 1);
        assertThat(testLangue.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLangue.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testLangue.getNiveau()).isEqualTo(DEFAULT_NIVEAU);

        // Validate the Langue in Elasticsearch
        verify(mockLangueSearchRepository, times(1)).save(testLangue);
    }

    @Test
    @Transactional
    public void createLangueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = langueRepository.findAll().size();

        // Create the Langue with an existing ID
        langue.setId(1L);
        LangueDTO langueDTO = langueMapper.toDto(langue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLangueMockMvc.perform(post("/api/langues").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(langueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Langue in the database
        List<Langue> langueList = langueRepository.findAll();
        assertThat(langueList).hasSize(databaseSizeBeforeCreate);

        // Validate the Langue in Elasticsearch
        verify(mockLangueSearchRepository, times(0)).save(langue);
    }


    @Test
    @Transactional
    public void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = langueRepository.findAll().size();
        // set the field null
        langue.setLibele(null);

        // Create the Langue, which fails.
        LangueDTO langueDTO = langueMapper.toDto(langue);


        restLangueMockMvc.perform(post("/api/langues").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(langueDTO)))
            .andExpect(status().isBadRequest());

        List<Langue> langueList = langueRepository.findAll();
        assertThat(langueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLangues() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList
        restLangueMockMvc.perform(get("/api/langues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(langue.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())));
    }
    
    @Test
    @Transactional
    public void getLangue() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get the langue
        restLangueMockMvc.perform(get("/api/langues/{id}", langue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(langue.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU.toString()));
    }


    @Test
    @Transactional
    public void getLanguesByIdFiltering() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        Long id = langue.getId();

        defaultLangueShouldBeFound("id.equals=" + id);
        defaultLangueShouldNotBeFound("id.notEquals=" + id);

        defaultLangueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLangueShouldNotBeFound("id.greaterThan=" + id);

        defaultLangueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLangueShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLanguesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where description equals to DEFAULT_DESCRIPTION
        defaultLangueShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the langueList where description equals to UPDATED_DESCRIPTION
        defaultLangueShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLanguesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where description not equals to DEFAULT_DESCRIPTION
        defaultLangueShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the langueList where description not equals to UPDATED_DESCRIPTION
        defaultLangueShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLanguesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLangueShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the langueList where description equals to UPDATED_DESCRIPTION
        defaultLangueShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLanguesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where description is not null
        defaultLangueShouldBeFound("description.specified=true");

        // Get all the langueList where description is null
        defaultLangueShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllLanguesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where description contains DEFAULT_DESCRIPTION
        defaultLangueShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the langueList where description contains UPDATED_DESCRIPTION
        defaultLangueShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLanguesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where description does not contain DEFAULT_DESCRIPTION
        defaultLangueShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the langueList where description does not contain UPDATED_DESCRIPTION
        defaultLangueShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllLanguesByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where libele equals to DEFAULT_LIBELE
        defaultLangueShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the langueList where libele equals to UPDATED_LIBELE
        defaultLangueShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllLanguesByLibeleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where libele not equals to DEFAULT_LIBELE
        defaultLangueShouldNotBeFound("libele.notEquals=" + DEFAULT_LIBELE);

        // Get all the langueList where libele not equals to UPDATED_LIBELE
        defaultLangueShouldBeFound("libele.notEquals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllLanguesByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultLangueShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the langueList where libele equals to UPDATED_LIBELE
        defaultLangueShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllLanguesByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where libele is not null
        defaultLangueShouldBeFound("libele.specified=true");

        // Get all the langueList where libele is null
        defaultLangueShouldNotBeFound("libele.specified=false");
    }
                @Test
    @Transactional
    public void getAllLanguesByLibeleContainsSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where libele contains DEFAULT_LIBELE
        defaultLangueShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the langueList where libele contains UPDATED_LIBELE
        defaultLangueShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    public void getAllLanguesByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where libele does not contain DEFAULT_LIBELE
        defaultLangueShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the langueList where libele does not contain UPDATED_LIBELE
        defaultLangueShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }


    @Test
    @Transactional
    public void getAllLanguesByNiveauIsEqualToSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where niveau equals to DEFAULT_NIVEAU
        defaultLangueShouldBeFound("niveau.equals=" + DEFAULT_NIVEAU);

        // Get all the langueList where niveau equals to UPDATED_NIVEAU
        defaultLangueShouldNotBeFound("niveau.equals=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    public void getAllLanguesByNiveauIsNotEqualToSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where niveau not equals to DEFAULT_NIVEAU
        defaultLangueShouldNotBeFound("niveau.notEquals=" + DEFAULT_NIVEAU);

        // Get all the langueList where niveau not equals to UPDATED_NIVEAU
        defaultLangueShouldBeFound("niveau.notEquals=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    public void getAllLanguesByNiveauIsInShouldWork() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where niveau in DEFAULT_NIVEAU or UPDATED_NIVEAU
        defaultLangueShouldBeFound("niveau.in=" + DEFAULT_NIVEAU + "," + UPDATED_NIVEAU);

        // Get all the langueList where niveau equals to UPDATED_NIVEAU
        defaultLangueShouldNotBeFound("niveau.in=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    public void getAllLanguesByNiveauIsNullOrNotNull() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        // Get all the langueList where niveau is not null
        defaultLangueShouldBeFound("niveau.specified=true");

        // Get all the langueList where niveau is null
        defaultLangueShouldNotBeFound("niveau.specified=false");
    }

    @Test
    @Transactional
    public void getAllLanguesByCvcorpIsEqualToSomething() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);
        Cvcorp cvcorp = CvcorpResourceIT.createEntity(em);
        em.persist(cvcorp);
        em.flush();
        langue.setCvcorp(cvcorp);
        langueRepository.saveAndFlush(langue);
        Long cvcorpId = cvcorp.getId();

        // Get all the langueList where cvcorp equals to cvcorpId
        defaultLangueShouldBeFound("cvcorpId.equals=" + cvcorpId);

        // Get all the langueList where cvcorp equals to cvcorpId + 1
        defaultLangueShouldNotBeFound("cvcorpId.equals=" + (cvcorpId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLangueShouldBeFound(String filter) throws Exception {
        restLangueMockMvc.perform(get("/api/langues?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(langue.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())));

        // Check, that the count call also returns 1
        restLangueMockMvc.perform(get("/api/langues/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLangueShouldNotBeFound(String filter) throws Exception {
        restLangueMockMvc.perform(get("/api/langues?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLangueMockMvc.perform(get("/api/langues/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLangue() throws Exception {
        // Get the langue
        restLangueMockMvc.perform(get("/api/langues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLangue() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        int databaseSizeBeforeUpdate = langueRepository.findAll().size();

        // Update the langue
        Langue updatedLangue = langueRepository.findById(langue.getId()).get();
        // Disconnect from session so that the updates on updatedLangue are not directly saved in db
        em.detach(updatedLangue);
        updatedLangue
            .description(UPDATED_DESCRIPTION)
            .libele(UPDATED_LIBELE)
            .niveau(UPDATED_NIVEAU);
        LangueDTO langueDTO = langueMapper.toDto(updatedLangue);

        restLangueMockMvc.perform(put("/api/langues").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(langueDTO)))
            .andExpect(status().isOk());

        // Validate the Langue in the database
        List<Langue> langueList = langueRepository.findAll();
        assertThat(langueList).hasSize(databaseSizeBeforeUpdate);
        Langue testLangue = langueList.get(langueList.size() - 1);
        assertThat(testLangue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLangue.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testLangue.getNiveau()).isEqualTo(UPDATED_NIVEAU);

        // Validate the Langue in Elasticsearch
        verify(mockLangueSearchRepository, times(1)).save(testLangue);
    }

    @Test
    @Transactional
    public void updateNonExistingLangue() throws Exception {
        int databaseSizeBeforeUpdate = langueRepository.findAll().size();

        // Create the Langue
        LangueDTO langueDTO = langueMapper.toDto(langue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLangueMockMvc.perform(put("/api/langues").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(langueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Langue in the database
        List<Langue> langueList = langueRepository.findAll();
        assertThat(langueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Langue in Elasticsearch
        verify(mockLangueSearchRepository, times(0)).save(langue);
    }

    @Test
    @Transactional
    public void deleteLangue() throws Exception {
        // Initialize the database
        langueRepository.saveAndFlush(langue);

        int databaseSizeBeforeDelete = langueRepository.findAll().size();

        // Delete the langue
        restLangueMockMvc.perform(delete("/api/langues/{id}", langue.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Langue> langueList = langueRepository.findAll();
        assertThat(langueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Langue in Elasticsearch
        verify(mockLangueSearchRepository, times(1)).deleteById(langue.getId());
    }

    @Test
    @Transactional
    public void searchLangue() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        langueRepository.saveAndFlush(langue);
        when(mockLangueSearchRepository.search(queryStringQuery("id:" + langue.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(langue), PageRequest.of(0, 1), 1));

        // Search the langue
        restLangueMockMvc.perform(get("/api/_search/langues?query=id:" + langue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(langue.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())));
    }
}
