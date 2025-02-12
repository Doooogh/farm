package com.doooogh.farm.user.model;

import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Integer status;
    private String[] roles;
} 