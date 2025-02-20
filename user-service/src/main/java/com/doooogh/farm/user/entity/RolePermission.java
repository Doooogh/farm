package com.doooogh.farm.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("sys_role_permission")
public class RolePermission {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 权限编码
     */
    private String permissionCode;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
} 