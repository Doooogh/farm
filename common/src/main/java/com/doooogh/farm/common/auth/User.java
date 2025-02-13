package com.doooogh.farm.common.auth;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.doooogh.farm.common.entity.BaseUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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

    /**
     * 用户角色列表
     */
    @TableField(exist = false)
    private List<UserRole> roles;

    //认证方式
    private String authenticationType;
} 