package com.restock.platform.resource.interfaces.rest;

import com.restock.platform.resource.domain.model.queries.GetAllSuppliesQuery;
import com.restock.platform.resource.domain.model.queries.GetAllSupplyCategoriesQuery;
import com.restock.platform.resource.domain.model.queries.GetSupplyByIdQuery;
import com.restock.platform.resource.domain.services.SupplyQueryService;
import com.restock.platform.resource.interfaces.rest.resources.SupplyCategoryResource;
import com.restock.platform.resource.interfaces.rest.resources.SupplyResource;
import com.restock.platform.resource.interfaces.rest.transform.SupplyCategoryResourceFromEntityAssembler;
import com.restock.platform.resource.interfaces.rest.transform.SupplyResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/supplies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Supplies", description = "Endpoints for viewing official platform supplies")
public class SupplyController {

    private final SupplyQueryService supplyQueryService;

    public SupplyController(SupplyQueryService supplyQueryService) {
        this.supplyQueryService = supplyQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all platform supplies")
    public ResponseEntity<List<SupplyResource>> getAllPlatformSupplies() {
        var supplies = supplyQueryService.handle(new GetAllSuppliesQuery());
        if (supplies.isEmpty()) return ResponseEntity.notFound().build();

        var resources = supplies.stream()
                .map(SupplyResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{supplyId}")
    @Operation(summary = "Get platform supply by ID")
    public ResponseEntity<SupplyResource> getPlatformSupplyById(@PathVariable Long supplyId) {
        var result = supplyQueryService.handle(new GetSupplyByIdQuery(supplyId));
        if (result.isEmpty()) return ResponseEntity.notFound().build();

        var resource = SupplyResourceFromEntityAssembler.toResourceFromEntity(result.get());
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all supply categories")
    public ResponseEntity<List<String>> getAllSupplyCategories() {
        var categories = supplyQueryService.handle(new GetAllSupplyCategoriesQuery());
        if (categories.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(categories);
    }

}
