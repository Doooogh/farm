package com.doooogh.farm.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

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
    

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 