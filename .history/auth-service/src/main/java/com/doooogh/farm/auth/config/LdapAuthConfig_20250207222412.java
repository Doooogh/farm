@Configuration
@EnableLdapAuthentication
public class LdapAuthConfig {
    
    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource source = new LdapContextSource();
        source.setUrl("ldap://localhost:389");
        source.setBase("dc=example,dc=com");
        source.setUserDn("cn=admin,dc=example,dc=com");
        source.setPassword("admin");
        return source;
    }
    
    @Bean
    public LdapAuthenticationProvider ldapAuthProvider() {
        return new LdapAuthenticationProvider(
            new BindAuthenticator(contextSource()),
            new DefaultLdapAuthoritiesPopulator(contextSource(), "ou=groups")
        );
    }
} 