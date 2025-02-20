package com.doooogh.farm.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

public class User {
    
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private List<String> roles;
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    // 其他字段和方法
} 