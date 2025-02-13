package com.doooogh.farm.user.model;

import lombok.Data;

@Data
public class WechatUserInfo {
    private String openId;
    private String nickname;
    private String headImgUrl;
    private String unionId;
    private Integer sex;
    private String country;
    private String province;
    private String city;
} 