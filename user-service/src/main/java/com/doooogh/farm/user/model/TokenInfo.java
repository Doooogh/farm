package com.doooogh.farm.user.model;

import lombok.Data;

@Data
public class TokenInfo {
    private Long userId;
    private String username;
    private Long expiresIn;
    private String[] authorities;
} 