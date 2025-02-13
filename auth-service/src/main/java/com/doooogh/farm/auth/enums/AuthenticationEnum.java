package com.doooogh.farm.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Li m13283354149@163.com
 * @date 2025/02/13
 * @description 认证方式
 */
@Getter
@AllArgsConstructor
public enum AuthenticationEnum {
        //账号密码
        USERNAME_PASSWORD,
        //短信
        SMS,
        //二维码
        QR,
        ;
}
