package com.doooogh.farm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.doooogh.farm.auth.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    
    /**
     * 批量插入角色权限
     */
    @Insert("<script>" +
            "INSERT INTO role_permission (role_code, permission_code) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.roleCode}, #{item.permissionCode})" +
            "</foreach>" +
            "</script>")
    int insertBatch(@Param("list") List<RolePermission> rolePermissions);
} 