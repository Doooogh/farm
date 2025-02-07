package com.doooogh.farm.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doooogh.farm.auth.entity.User;
import com.doooogh.farm.auth.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义用户详情服务
 * 实现Spring Security的UserDetailsService接口，用于加载用户信息
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    
    /**
     * 根据用户名加载用户信息
     * 从数据库中查询用户，并转换为UserDetails对象
     *
     * @param username 用户名
     * @return UserDetails对象，包含用户认证和授权信息
     * @throws UsernameNotFoundException 当用户不存在时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>()
            .eq("username", username)
            .eq("status", 1)  // 只查询状态正常的用户
            .eq("deleted", 0));  // 排除已删除的用户
            
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        // 查询用户角色
        List<UserRole> userRoles = userRoleMapper.selectList(
            new QueryWrapper<UserRole>().eq("user_id", user.getId())
        );
        
        // 转换为Spring Security的Authorities
        List<GrantedAuthority> authorities = userRoles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getRoleCode()))
            .collect(Collectors.toList());
        
        // 构建UserDetails对象
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(authorities)
            .build();
    }
} 