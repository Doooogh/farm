/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Tag(name = "权限管理", description = "权限相关接口")
public class PermissionController {
    
    private final PermissionService permissionService;
    
    @PostMapping
    @Operation(summary = "创建权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Permission> createPermission(@RequestBody @Valid Permission permission) {
        return Result.success(permissionService.createPermission(permission));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updatePermission(@PathVariable Long id, @RequestBody @Valid Permission permission) {
        permission.setId(id);
        permissionService.updatePermission(permission);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return Result.success();
    }
    
    @GetMapping("/tree")
    @Operation(summary = "获取权限树")
    public Result<List<PermissionTree>> getPermissionTree() {
        return Result.success(permissionService.getPermissionTree());
    }
} 