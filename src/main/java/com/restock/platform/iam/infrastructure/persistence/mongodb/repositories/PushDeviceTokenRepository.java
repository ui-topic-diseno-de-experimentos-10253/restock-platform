package com.restock.platform.iam.infrastructure.persistence.mongodb.repositories;

import com.restock.platform.iam.domain.model.entities.PushDeviceToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PushDeviceTokenRepository extends MongoRepository<PushDeviceToken, Long> {
    Optional<PushDeviceToken> findByUserIdAndProviderAndPlatform(Long userId, String provider, String platform);
    List<PushDeviceToken> findAllByUserIdAndProviderAndActiveTrue(Long userId, String provider);
}
