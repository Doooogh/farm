package com.doooogh.farm.user.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {

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


    /**
     * 用户状态：0-禁用，1-启用
     */
    private Integer status;

    private List<String> roles;
} 