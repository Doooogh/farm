/**
 * 权限实体
 */
@Data
@TableName("sys_permission")
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity {
    
    /**
     * 权限编码
     */
    private String code;
    
    /**
     * 权限名称
     */
    private String name;
    
    /**
     * 权限类型（menu:菜单,button:按钮）
     */
    private String type;
    
    /**
     * 父权限ID
     */
    private Long parentId;
    
    /**
     * 权限路径
     */
    private String path;
    
    /**
     * 组件路径
     */
    private String component;
    
    /**
     * 权限图标
     */
    private String icon;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态(0:禁用,1:正常)
     */
    private Integer status;
} 