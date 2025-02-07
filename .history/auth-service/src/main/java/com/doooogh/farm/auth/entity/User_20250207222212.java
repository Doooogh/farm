@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private Integer status; // 0:禁用 1:正常
} 