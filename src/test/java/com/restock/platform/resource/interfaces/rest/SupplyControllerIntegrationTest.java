package com.restock.platform.resource.interfaces.rest;

import com.restock.platform.resource.interfaces.rest.resources.SupplyResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Supply REST Integration Tests")
class SupplyControllerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(SupplyControllerIntegrationTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("GET /supplies with seeded database returns 200 and non-empty list")
    void getAllSupplies_seededDatabase_returns200WithList() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/v1/supplies", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        log.info("[PASS] getAllSupplies → status={} count={}", response.getStatusCode(), response.getBody().size());
    }

    @Test
    @DisplayName("GET /supplies/{id} with valid ID returns 200 and supply data")
    void getSupplyById_existingId_returns200WithSupply() {
        ResponseEntity<SupplyResource> response = restTemplate.getForEntity("/api/v1/supplies/1", SupplyResource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        log.info("[PASS] getSupplyById_existing → status={} supply={}", response.getStatusCode(), response.getBody());
    }

    @Test
    @DisplayName("GET /supplies/{id} with non-existent ID returns 404")
    void getSupplyById_nonExistentId_returns404() {
        ResponseEntity<SupplyResource> response = restTemplate.getForEntity("/api/v1/supplies/99999", SupplyResource.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        log.info("[PASS] getSupplyById_nonExistent → status={}", response.getStatusCode());
    }

    @Test
    @DisplayName("GET /supplies/categories with seeded database returns 200 and category list")
    void getAllSupplyCategories_seededDatabase_returns200WithCategories() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/v1/supplies/categories", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        log.info("[PASS] getAllSupplyCategories → status={} categories={}", response.getStatusCode(), response.getBody());
    }
}
