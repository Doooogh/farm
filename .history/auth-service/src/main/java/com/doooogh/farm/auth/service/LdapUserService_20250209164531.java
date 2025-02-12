package com.doooogh.farm.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LdapUserService {

    private final LdapTemplate ldapTemplate;

    /**
     * 验证LDAP用户
     */
    public boolean authenticate(String username, String password) {
        Filter filter = new EqualsFilter("uid", username);
        return ldapTemplate.authenticate("", filter.encode(), password);
    }

    /**
     * 查找LDAP用户
     */
    public LdapUser findUser(String username) {
        return ldapTemplate.findOne(
            LdapQueryBuilder.query()
                .where("uid")
                .is(username),
            LdapUser.class
        );
    }
} 