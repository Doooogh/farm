package com.doooogh.farm.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doooogh.farm.user.entity.User;
import com.doooogh.farm.user.entity.UserRole;
import com.doooogh.farm.user.exception.ServiceException;
import com.doooogh.farm.user.mapper.UserMapper;
import com.doooogh.farm.user.mapper.UserRoleMapper;
import com.doooogh.farm.user.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务
 */
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserEventService userEventService;
    
    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public UserDTO getUserInfo(Long userId) {
        // 查询用户基本信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        
        // 查询用户角色
        List<UserRole> userRoles = userRoleMapper.selectList(
            new QueryWrapper<UserRole>().eq("user_id", userId)
        );
        
        // 转换为DTO
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoles(userRoles.stream()
            .map(UserRole::getRoleCode)
            .collect(Collectors.toList()));
            
        return userDTO;
    }

    @Transactional
    public UserDTO createUser(User user) {
        userMapper.insert(user);
        userEventService.sendUserCreatedEvent(user);
        return getUserInfo(user.getId());
    }
    
    @Transactional
    public UserDTO updateUser(User user) {
        userMapper.updateById(user);
        userEventService.sendUserUpdatedEvent(user);
        return getUserInfo(user.getId());
    }
} 