package com.restock.platform.resource.application.internal.eventhandlers;

import com.restock.platform.resource.domain.model.commands.SeedSuppliesCommand;
import com.restock.platform.resource.domain.services.SupplyCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ResourceApplicationReadyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceApplicationReadyEventHandler.class);
    private final SupplyCommandService supplyCommandService;

    public ResourceApplicationReadyEventHandler(SupplyCommandService supplyCommandService) {
        this.supplyCommandService = supplyCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var appName = event.getApplicationContext().getId();
        LOGGER.info("Seeding supplies for {} at {}", appName, timestamp());
        supplyCommandService.handle(new SeedSuppliesCommand());
        LOGGER.info("Seeding finished for {} at {}", appName, timestamp());
    }

    private Timestamp timestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
