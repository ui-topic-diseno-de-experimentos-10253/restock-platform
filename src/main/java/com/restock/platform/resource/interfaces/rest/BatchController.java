package com.restock.platform.resource.interfaces.rest;

import com.restock.platform.resource.domain.model.commands.DeleteBatchCommand;
import com.restock.platform.resource.domain.model.queries.GetAllBatchesQuery;
import com.restock.platform.resource.domain.model.queries.GetBatchByIdQuery;
import com.restock.platform.resource.domain.model.queries.GetBatchesByUserIdQuery;
import com.restock.platform.resource.domain.services.BatchCommandService;
import com.restock.platform.resource.domain.services.BatchQueryService;
import com.restock.platform.resource.interfaces.rest.resources.BatchResource;
import com.restock.platform.resource.interfaces.rest.resources.CreateBatchResource;
import com.restock.platform.resource.interfaces.rest.transform.BatchResourceFromEntityAssembler;
import com.restock.platform.resource.interfaces.rest.transform.CreateBatchCommandFromResourceAssembler;
import com.restock.platform.resource.domain.model.commands.UpdateBatchCommand;
import com.restock.platform.resource.interfaces.rest.resources.UpdateBatchResource;
import com.restock.platform.resource.interfaces.rest.transform.UpdateBatchCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * BatchController
 * <p>
 *     All batch-related endpoints.
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Batches", description = "Endpoints for managing batches")
public class BatchController {

    private final BatchCommandService batchCommandService;
    private final BatchQueryService batchQueryService;

    /**
     * Constructor
     *
     * @param batchCommandService The {@link BatchCommandService} instance
     * @param batchQueryService   The {@link BatchQueryService} instance
     */
    public BatchController(BatchCommandService batchCommandService, BatchQueryService batchQueryService) {
        this.batchCommandService = batchCommandService;
        this.batchQueryService = batchQueryService;
    }

    /**
     * Create a new batch
     *
     * @param resource The {@link CreateBatchResource} instance
     * @return The created {@link BatchResource}
     */
    @PostMapping
    @Operation(summary = "Create a new batch", description = "Create a new batch and return its details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Batch created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<BatchResource> createBatch(@RequestBody CreateBatchResource resource) {
        var command = CreateBatchCommandFromResourceAssembler.toCommandFromResource(resource);
        var batchId = batchCommandService.handle(command);
        if (batchId == null || batchId == 0L) return ResponseEntity.badRequest().build();
        var result = batchQueryService.handle(new GetBatchByIdQuery(batchId));
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        var batch = result.get();
        var batchResource = BatchResourceFromEntityAssembler.toResourceFromEntity(batch);
        return new ResponseEntity<>(batchResource, HttpStatus.CREATED);
    }

    /**
     * Get all batches
     *
     * @return A list of {@link BatchResource}
     */
    @GetMapping
    @Operation(summary = "Get all batches", description = "Retrieve all available batches")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batches retrieved successfully")
    })
    public ResponseEntity<List<BatchResource>> getAllBatches() {
        var batches = batchQueryService.handle(new GetAllBatchesQuery());
        var resources = batches.stream()
                .map(BatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Get batch by ID
     *
     * @param id The ID of the batch
     * @return The {@link BatchResource} if found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get batch by ID", description = "Retrieve a batch by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch found"),
            @ApiResponse(responseCode = "404", description = "Batch not found")
    })
    public ResponseEntity<BatchResource> getBatchById(@PathVariable Long id) {
        return batchQueryService.handle(new GetBatchByIdQuery(id))
                .map(BatchResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get batches by user ID
     *
     * @param userId The ID of the user
     * @return A list of {@link BatchResource} belonging to the user
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get batches by user ID", description = "Retrieve all batches associated with a given user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batches found for user")
    })
    public ResponseEntity<List<BatchResource>> getBatchesByUserId(@PathVariable Long userId) {
        var batches = batchQueryService.handle(new GetBatchesByUserIdQuery(userId));
        var resources = batches.stream()
                .map(BatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
    /**
     * Update a batch by ID
     *
     * @param id The ID of the batch
     * @param resource The updated values
     * @return The updated {@link BatchResource}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a batch", description = "Update an existing batch by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Batch not found")
    })
    public ResponseEntity<BatchResource> updateBatch(
            @PathVariable Long id,
            @RequestBody UpdateBatchResource resource) {

        var command = UpdateBatchCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var updatedBatch = batchCommandService.handle(command);

        if (updatedBatch.isEmpty()) return ResponseEntity.notFound().build();

        var result = batchQueryService.handle(new GetBatchByIdQuery(id));
        if (result.isEmpty()) return ResponseEntity.notFound().build();

        var resourceResponse = BatchResourceFromEntityAssembler.toResourceFromEntity(result.get());
        return ResponseEntity.ok(resourceResponse);
    }
    /**
     * Delete a batch by ID
     *
     * @param id The ID of the batch to delete
     * @return 204 No Content if deleted successfully
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a batch", description = "Delete a batch by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Batch deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Batch not found")
    })
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        try {
            batchCommandService.handle(new DeleteBatchCommand(id));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
