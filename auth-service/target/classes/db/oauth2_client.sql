CREATE TABLE oauth2_client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id VARCHAR(255) NOT NULL,
    client_secret VARCHAR(255) NOT NULL,
    scope VARCHAR(255),
    authorized_grant_types VARCHAR(255),
    web_server_redirect_uri VARCHAR(255),
    access_token_validity INTEGER,
    refresh_token_validity INTEGER,
    additional_information VARCHAR(4096),
    authorities VARCHAR(255),
    resource_ids VARCHAR(255),
    UNIQUE KEY uk_client_id (client_id)
);

-- 插入测试客户端
INSERT INTO oauth2_client (
    client_id, 
    client_secret,
    scope,
    authorized_grant_types,
    web_server_redirect_uri,
    access_token_validity,
    refresh_token_validity
) VALUES (
    'test-client',
    '$2a$10$8jJ.1C8nzIDXdXkIbIAO4.wqEtQLjHzJxQ5K.Hs0JtDQj9YnmxDDO', -- 密码: test-secret
    'read,write',
    'password,refresh_token,authorization_code',
    'http://localhost:8080/login/oauth2/code/custom',
    3600,
    7200
); 