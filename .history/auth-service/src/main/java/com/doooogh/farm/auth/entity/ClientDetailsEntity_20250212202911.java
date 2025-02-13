package com.doooogh.farm.auth.entity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "oauth_client_details")
public class ClientDetailsEntity {
    @Id
    private String clientId;
    private String clientSecret;
    private String authorizedGrantTypes;
    private String scopes;
    private String redirectUris;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
} 