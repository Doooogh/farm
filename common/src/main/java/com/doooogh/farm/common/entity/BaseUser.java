package com.doooogh.farm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.doooogh.farm.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 基础用户实体
 * 定义用户的通用属性
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseUser extends BaseEntity {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 状态(0:禁用,1:正常)
     */
    private Integer status;
    

} 