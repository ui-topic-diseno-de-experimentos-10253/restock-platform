package com.restock.platform.monitoring.acl;

import com.restock.platform.monitoring.application.internal.outboundedservices.acl.ExternalPlanningService;
import com.restock.platform.planning.domain.model.commands.CreateRecipeCommand;
import com.restock.platform.planning.domain.services.RecipeCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Cross-Context ACL Integration Tests: Monitoring -> Planning")
class ExternalPlanningServiceIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ExternalPlanningServiceIntegrationTest.class);

    @Autowired
    private ExternalPlanningService externalPlanningService;

    @Autowired
    private RecipeCommandService recipeCommandService;

    @Test
    @DisplayName("fetchRecipeByRecipeId with non-existent ID returns Optional.empty()")
    void fetchRecipeByRecipeId_nonExistentId_returnsEmpty() {
        var result = externalPlanningService.fetchRecipeByRecipeId(99999L);

        assertThat(result).isEmpty();
        log.info("[PASS] fetchRecipeByRecipeId_nonExistent → result={}", result);
    }

    @Test
    @DisplayName("fetchRecipeByRecipeId with existing recipe returns correct RecipeId")
    void fetchRecipeByRecipeId_existingRecipe_returnsRecipeId() {
        var recipeId = recipeCommandService.handle(
                new CreateRecipeCommand("Pasta Carbonara", "Classic Italian pasta",
                        "https://img.example.com/pasta.jpg", 12.5, 1));

        var result = externalPlanningService.fetchRecipeByRecipeId(recipeId);

        assertThat(result).isPresent();
        assertThat(result.get().recipeId()).isEqualTo(recipeId);
        log.info("[PASS] fetchRecipeByRecipeId_existing → recipeId={} result={}", recipeId, result.get().recipeId());
    }

    @Test
    @DisplayName("fetchRecipePriceByRecipeId with existing recipe returns exact price")
    void fetchRecipePriceByRecipeId_existingRecipe_returnsCorrectPrice() {
        double expectedPrice = 22.75;
        var recipeId = recipeCommandService.handle(
                new CreateRecipeCommand("Margherita Pizza", "Classic pizza",
                        "https://img.example.com/pizza.jpg", expectedPrice, 1));

        var price = externalPlanningService.fetchRecipePriceByRecipeId(recipeId);

        assertThat(price).isEqualTo(expectedPrice);
        log.info("[PASS] fetchRecipePriceByRecipeId_existing → recipeId={} price={}", recipeId, price);
    }

    @Test
    @DisplayName("fetchRecipePriceByRecipeId with non-existent ID returns 0.0")
    void fetchRecipePriceByRecipeId_nonExistentId_returnsZero() {
        var price = externalPlanningService.fetchRecipePriceByRecipeId(99998L);

        assertThat(price).isEqualTo(0.0);
        log.info("[PASS] fetchRecipePriceByRecipeId_nonExistent → price={}", price);
    }
}
