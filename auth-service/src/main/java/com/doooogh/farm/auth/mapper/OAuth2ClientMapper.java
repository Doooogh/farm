package com.doooogh.farm.auth.mapper;

import com.doooogh.farm.auth.entity.OAuth2Client;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * OAuth2客户端数据访问接口
 * 继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作
 * 可以根据需要扩展自定义的查询方法
 */
@Mapper
public interface OAuth2ClientMapper extends BaseMapper<OAuth2Client> {
    
    /**
     * 根据客户端ID查询客户端信息
     * 
     * @param clientId 客户端ID
     * @return 客户端信息，不存在时返回null
     */
    default OAuth2Client selectByClientId(String clientId) {
        return selectOne(new QueryWrapper<OAuth2Client>()
            .eq("client_id", clientId)
            .eq("deleted", false));
    }
} 