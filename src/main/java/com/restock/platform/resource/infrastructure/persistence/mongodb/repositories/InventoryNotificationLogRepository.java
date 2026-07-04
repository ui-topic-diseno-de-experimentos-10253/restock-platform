package com.restock.platform.resource.infrastructure.persistence.mongodb.repositories;

import com.restock.platform.resource.domain.model.entities.InventoryNotificationLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryNotificationLogRepository extends MongoRepository<InventoryNotificationLog, Long> {
}
