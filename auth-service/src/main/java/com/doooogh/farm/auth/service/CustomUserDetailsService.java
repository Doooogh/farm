package com.doooogh.farm.auth.service;

import com.doooogh.farm.auth.client.UserServiceClient;
import com.doooogh.farm.common.auth.User;
import com.doooogh.farm.auth.exception.AuthException;
import com.doooogh.farm.common.auth.CustomUserDetails;
import com.doooogh.farm.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义用户详情服务
 * 实现Spring Security的UserDetailsService接口，用于加载用户信息
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    /**
     * 根据用户名加载用户信息
     * 从数据库中查询用户，并转换为UserDetails对象
     *
     * @param identifier 唯一标识
     * @return UserDetails对象，包含用户认证和授权信息
     * @throws UsernameNotFoundException 当用户不存在时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        String newIdentifier = StringUtil.getSecondString(identifier, "_");
        String authenticationType = StringUtil.getFirstString(identifier, "_");
        // 使用 Feign 客户端调用用户服务
        User user = userServiceClient.findUserByIdentifier(newIdentifier).getData();
        if (user == null) {
            throw new AuthException(401, "用户不存在");
        }
        user.setAuthenticationType(authenticationType);

        // 构建UserDetails对象
        return new CustomUserDetails(user);
    }
} 