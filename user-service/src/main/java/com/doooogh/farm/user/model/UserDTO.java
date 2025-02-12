package com.doooogh.farm.user.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String mobile;
    private String avatar;
    private Integer status;
    private List<String> roles;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 