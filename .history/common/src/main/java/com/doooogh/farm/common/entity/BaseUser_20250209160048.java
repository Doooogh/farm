/**
 * 基础用户实体
 * 定义用户的通用属性
 */
package com.doooogh.farm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseUser extends BaseEntity {
    
    /**
     * 用户名
     */
    @TableField
    private String username;
    
    /**
     * 密码
     */
    @TableField
    private String password;
    
    /**
     * 昵称
     */
    @TableField
    private String nickname;
    
    /**
     * 邮箱
     */
    @TableField
    private String email;
    
    /**
     * 手机号
     */
    @TableField
    private String mobile;
    
    /**
     * 头像
     */
    @TableField
    private String avatar;
    
    /**
     * 状态(0:禁用,1:正常)
     */
    @TableField
    private Boolean enabled = true;
    
    /**
     * 账户未过期
     */
    @TableField
    private Boolean accountNonExpired = true;
    
    /**
     * 凭证未过期
     */
    @TableField
    private Boolean credentialsNonExpired = true;
    
    /**
     * 账户未锁定
     */
    @TableField
    private Boolean accountNonLocked = true;
} 