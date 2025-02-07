package com.doooogh.farm.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doooogh.farm.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("oauth2_client")
public class OAuth2Client extends BaseEntity {
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