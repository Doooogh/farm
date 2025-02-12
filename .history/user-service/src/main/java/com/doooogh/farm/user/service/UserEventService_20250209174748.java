package com.doooogh.farm.user.service;

import com.doooogh.farm.user.entity.User;
import com.doooogh.farm.user.message.UserEventChannels;
import com.doooogh.farm.user.message.event.UserCreatedEvent;
import com.doooogh.farm.user.message.event.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * 用户事件服务
 * 处理用户相关的事件发送
 */
@Service
@RequiredArgsConstructor
public class UserEventService {
    
    private final UserEventChannels channels;
    
    /**
     * 发送用户创建事件
     *
     * @param user 创建的用户信息
     */
    public void sendUserCreatedEvent(User user) {
        UserCreatedEvent event = new UserCreatedEvent(user.getId(), user.getUsername());
        Message<UserCreatedEvent> message = MessageBuilder.withPayload(event)
            .setHeader("eventType", "USER_CREATED")
            .build();
        channels.userCreatedOutput().send(message);
    }
    
    /**
     * 发送用户更新事件
     *
     * @param user 更新的用户信息
     */
    public void sendUserUpdatedEvent(User user) {
        UserUpdatedEvent event = new UserUpdatedEvent(user.getId(), user.getUsername());
        Message<UserUpdatedEvent> message = MessageBuilder.withPayload(event)
            .setHeader("eventType", "USER_UPDATED")
            .build();
        channels.userUpdatedOutput().send(message);
    }
} 