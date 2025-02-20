package com.doooogh.farm.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("sys_permission")
public class Permission {
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限类型（menu:菜单,button:按钮）
     */
    private String type;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 权限路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 权限图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Integer status;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
} 