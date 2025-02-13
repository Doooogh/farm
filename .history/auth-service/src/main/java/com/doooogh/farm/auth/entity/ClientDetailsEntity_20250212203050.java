package com.doooogh.farm.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("oauth_client_details")
public class ClientDetailsEntity {
    @TableId
    private String clientId;
    private String clientSecret;
    private String authorizedGrantTypes;
    private String scopes;
    private String redirectUris;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
} 