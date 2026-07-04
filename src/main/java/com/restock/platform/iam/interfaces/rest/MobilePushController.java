package com.restock.platform.iam.interfaces.rest;

import com.restock.platform.iam.domain.model.entities.PushDeviceToken;
import com.restock.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.restock.platform.iam.domain.services.UserQueryService;
import com.restock.platform.iam.infrastructure.persistence.mongodb.repositories.PushDeviceTokenRepository;
import com.restock.platform.iam.interfaces.rest.resources.RegisterPushDeviceTokenResource;
import com.restock.platform.shared.infrastructure.persistence.mongodb.SequenceGeneratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/mobile", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Mobile Push Tokens", description = "Endpoints for mobile Firebase Cloud Messaging token registration")
public class MobilePushController {

    private final UserQueryService userQueryService;
    private final PushDeviceTokenRepository pushDeviceTokenRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Value("${notifications.push.provider:firebase}")
    private String pushProvider;

    public MobilePushController(UserQueryService userQueryService,
                                PushDeviceTokenRepository pushDeviceTokenRepository,
                                SequenceGeneratorService sequenceGeneratorService) {
        this.userQueryService = userQueryService;
        this.pushDeviceTokenRepository = pushDeviceTokenRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @PutMapping("/push-token")
    @Operation(summary = "Register or update mobile FCM token", description = "Stores the Firebase Cloud Messaging registration token for a mobile user and refreshes lastSeenAt.")
    public ResponseEntity<Void> registerOrUpdatePushToken(@RequestBody RegisterPushDeviceTokenResource resource) {
        var user = userQueryService.handle(new GetUserByIdQuery(resource.userId()));
        if (user.isEmpty()) return ResponseEntity.notFound().build();

        var pushToken = pushDeviceTokenRepository
                .findByUserIdAndProviderAndPlatform(resource.userId(), pushProvider, resource.platform())
                .map(existing -> {
                    existing.refresh(resource.platform(), pushProvider, resource.pushToken());
                    return existing;
                })
                .orElseGet(() -> {
                    var token = new PushDeviceToken(resource.userId(), resource.platform(), pushProvider, resource.pushToken());
                    token.setId(sequenceGeneratorService.generateSequence("push_device_tokens_sequence"));
                    return token;
                });

        pushDeviceTokenRepository.save(pushToken);
        return ResponseEntity.ok().build();
    }
}
