package com.doooogh.farm.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.doooogh.farm.common.exception.BusinessException;
import com.doooogh.farm.user.entity.User;
import com.doooogh.farm.user.entity.UserRole;
import com.doooogh.farm.user.exception.ServiceException;
import com.doooogh.farm.user.mapper.UserMapper;
import com.doooogh.farm.user.mapper.UserRoleMapper;
import com.doooogh.farm.user.model.UserDTO;
import com.doooogh.farm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private final UserRepository userRepository;

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public UserDTO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
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
        // 参数校验
        if (StringUtils.isEmpty(user.getUsername())) {
            throw BusinessException.invalidParameter("用户名不能为空");
        }
        
        // 检查用户名是否存在
        User existingUser = userMapper.selectOne(
            new QueryWrapper<User>().eq("username", user.getUsername())
        );
        if (existingUser != null) {
            throw BusinessException.alreadyExists("用户名已存在");
        }
        
        try {
            userMapper.insert(user);
//            userEventService.sendUserCreatedEvent(user);
            return getUserInfo(user.getId());
        } catch (Exception e) {
            throw new ServiceException("创建用户失败");
        }
    }
    
    @Transactional
    public UserDTO updateUser(User user) {
        userMapper.updateById(user);
//        userEventService.sendUserUpdatedEvent(user);
        return getUserInfo(user.getId());
    }

    public User findUserByIdentifier(String identifier) {
        return userRepository.findByUsernameOrPhone(identifier, identifier);
    }
} 