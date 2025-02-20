package com.doooogh.farm.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 实体基类
 * 包含所有实体共有的基础字段
 */
@Data
public class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     * 使用雪花算法生成
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 创建时间
     * 由MyBatis-Plus自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    
    /**
     * 更新时间
     * 由MyBatis-Plus自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    
    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    
    /**
     * 逻辑删除标志
     * true表示已删除
     */
    @TableField
    private Boolean deleted = false;
} 