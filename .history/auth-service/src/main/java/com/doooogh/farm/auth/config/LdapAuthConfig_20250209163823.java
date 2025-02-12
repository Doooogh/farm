package com.doooogh.farm.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.beans.factory.annotation.Value;

/**
 * LDAP认证配置
 * 配置LDAP连接和认证相关的Bean
 */
@Configuration
public class LdapAuthConfig {

    @Value("${ldap.urls}")
    private String ldapUrls;

    @Value("${ldap.base.dn}")
    private String ldapBaseDn;

    @Value("${ldap.username}")
    private String ldapSecurityPrincipal;

    @Value("${ldap.password}")
    private String ldapPrincipalPassword;

    @Value("${ldap.user.dn.pattern}")
    private String ldapUserDnPattern;

    @Value("${ldap.user.search.filter}")
    private String ldapUserSearchFilter;

    /**
     * 配置LDAP数据源
     */
    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapUrls);
        contextSource.setBase(ldapBaseDn);
        contextSource.setUserDn(ldapSecurityPrincipal);
        contextSource.setPassword(ldapPrincipalPassword);
        return contextSource;
    }

    /**
     * 配置LDAP模板
     */
    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

    /**
     * 配置LDAP认证提供者
     */
    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {
        FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch(
            ldapBaseDn,
            ldapUserSearchFilter,
            contextSource()
        );

        BindAuthenticator authenticator = new BindAuthenticator(contextSource());
        authenticator.setUserSearch(userSearch);
        authenticator.setUserDnPatterns(new String[]{ldapUserDnPattern});

        LdapAuthenticationProvider provider = new LdapAuthenticationProvider(authenticator);
        provider.setUserDetailsContextMapper(new LdapUserDetailsMapper());
        
        return provider;
    }
} 