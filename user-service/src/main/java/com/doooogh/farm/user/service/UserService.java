/**
 * 用户服务
 */
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    
    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public UserDTO getUserInfo(Long userId) {
        // 查询用户基本信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        
        // 查询用户角色
        List<UserRole> userRoles = userRoleMapper.selectList(
            new QueryWrapper<UserRole>().eq("user_id", userId)
        );
        
        // 转换为DTO
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoles(userRoles.stream()
            .map(UserRole::getRoleCode)
            .collect(Collectors.toList()));
            
        return userDTO;
    }
} 