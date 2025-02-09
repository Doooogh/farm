package com.doooogh.farm.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doooogh.farm.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OAuth2客户端实体类
 * 存储OAuth2客户端的配置信息，包括客户端ID、密钥、授权范围等
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("oauth2_client")
@Entity
@Table(name = "oauth2_client")
public class OAuth2Client extends BaseEntity {
    
    @Id
    private Long id;
    
    /**
     * 客户端ID
     * OAuth2客户端的唯一标识
     */
    private String clientId;
    
    /**
     * 客户端密钥
     * 用于客户端认证的密钥，应该进行加密存储
     */
    private String clientSecret;
    
    /**
     * 授权范围
     * 客户端被允许请求的权限范围，多个值用逗号分隔
     */
    private String scope;
    
    /**
     * 授权类型
     * 支持的OAuth2授权类型，如password,authorization_code等
     */
    private String authorizedGrantTypes;
    
    /**
     * 重定向URI
     * 授权码模式下的回调地址
     */
    private String webServerRedirectUri;
    
    /**
     * 访问令牌有效期
     * 单位：秒
     */
    private Integer accessTokenValidity;
    
    /**
     * 刷新令牌有效期
     * 单位：秒
     */
    private Integer refreshTokenValidity;
    
    /**
     * 附加信息
     * 存储客户端的其他配置信息，JSON格式
     */
    private String additionalInformation;
    
    /**
     * 权限列表
     * 客户端被授予的权限，多个值用逗号分隔
     */
    private String authorities;
    
    /**
     * 资源ID列表
     * 客户端可以访问的资源服务器ID，多个值用逗号分隔
     */
    private String resourceIds;
} 