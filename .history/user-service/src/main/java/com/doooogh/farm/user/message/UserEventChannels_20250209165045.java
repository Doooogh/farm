package com.doooogh.farm.user.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 用户事件消息通道
 * 定义消息的发送和接收通道
 */
public interface UserEventChannels {
    
    String USER_CREATED_OUTPUT = "userCreatedOutput";
    String USER_UPDATED_OUTPUT = "userUpdatedOutput";
    
    /**
     * 用户创建事件发送通道
     */
    @Output(USER_CREATED_OUTPUT)
    MessageChannel userCreatedOutput();
    
    /**
     * 用户更新事件发送通道
     */
    @Output(USER_UPDATED_OUTPUT)
    MessageChannel userUpdatedOutput();
} 