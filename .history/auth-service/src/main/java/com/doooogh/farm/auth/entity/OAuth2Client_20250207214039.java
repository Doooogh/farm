package com.doooogh.farm.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "oauth2_client")
public class OAuth2Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String clientId;
    
    private String clientSecret;
    
    private String scope;
    
    private String authorizedGrantTypes;
    
    private String webServerRedirectUri;
    
    private Integer accessTokenValidity;
    
    private Integer refreshTokenValidity;
    
    private String additionalInformation;
    
    private String authorities;
    
    private String resourceIds;
} 