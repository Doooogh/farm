package com.doooogh.farm.auth.model;

import lombok.Data;

@Data
public class AuthWechatUserInfo {
    private String openId;
    private String nickname;
    private String headImgUrl;
    private String unionId;
    private Integer sex;
    private String country;
    private String province;
    private String city;
} 