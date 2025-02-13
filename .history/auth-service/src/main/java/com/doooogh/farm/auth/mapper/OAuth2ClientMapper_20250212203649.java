package com.doooogh.farm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doooogh.farm.auth.entity.OAuth2Client;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * OAuth2 客户端映射器接口，用于数据库操作。
 */
@Mapper
public interface OAuth2ClientMapper extends BaseMapper<OAuth2Client> {
    
    /**
     * 根据客户端ID查询OAuth2客户端。
     *
     * @param clientId 客户端ID
     * @return OAuth2客户端实体
     */
    OAuth2Client selectByClientId(String clientId);
} 