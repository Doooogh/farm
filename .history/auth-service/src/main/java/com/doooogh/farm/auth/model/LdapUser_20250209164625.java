package com.doooogh.farm.auth.model;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.naming.Name;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entry(objectClasses = {"inetOrgPerson", "top"})
public class LdapUser {
    
    @Id
    private Name dn;
    
    @Attribute(name = "uid")
    private String username;
    
    @Attribute(name = "cn")
    private String commonName;
    
    @Attribute(name = "sn")
    private String surname;
    
    @Attribute(name = "mail")
    private String email;
    
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 这里可以根据LDAP中的角色属性来设置权限
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
} 