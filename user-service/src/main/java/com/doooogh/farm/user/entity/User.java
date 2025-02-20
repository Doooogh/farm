package com.doooogh.farm.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@TableName("sys_user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;

    //微信ID
    private String wechatOpenid;
    
    private String email;
    
    private String phone;

    //昵称
    private String nickname;

    //真实名称
    private String realName;

    //头像
    private String avatar;

    //生日
    private String birthday;

    //地址
    private String address;
    
    /**
     * 用户状态：0-禁用，1-启用
     */
    private Integer status;

    @TableField(exist = false)
    private List<String> roles;
    

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
} 