package com.restock.platform.iam.domain.model.entities;

import com.restock.platform.shared.domain.model.entities.AuditableModel;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Document(collection = "push_device_tokens")
public class PushDeviceToken extends AuditableModel {
    private Long userId;
    private String platform;
    private String provider;
    private String pushToken;
    private boolean active;
    private Date lastSeenAt;

    protected PushDeviceToken() {
    }

    public PushDeviceToken(Long userId, String platform, String provider, String pushToken) {
        this.userId = userId;
        this.platform = platform;
        this.provider = provider;
        this.pushToken = pushToken;
        this.active = true;
        this.lastSeenAt = new Date();
    }

    public void refresh(String platform, String provider, String pushToken) {
        this.platform = platform;
        this.provider = provider;
        this.pushToken = pushToken;
        this.active = true;
        this.lastSeenAt = new Date();
    }
}
