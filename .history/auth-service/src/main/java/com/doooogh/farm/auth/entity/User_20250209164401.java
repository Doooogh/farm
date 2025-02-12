package com.doooogh.farm.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.doooogh.farm.common.entity.BaseUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 认证服务用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseUser {
    
    /**
     * 微信OpenID
     */
    private String wechatOpenid;
    
    /**
     * MFA密钥
     */
    private String mfaSecretKey;
} 