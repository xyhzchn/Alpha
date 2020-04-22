package dao;

import bean.Role;
import java.util.List;

/**
 * 角色相关dao接口类
 * Created by sofronie on 2017/7/21.
 */
public interface RoleDao {
    /**
     * 获取所有的角色信息
     * @return 数据库中所有角色列表
     */
    public List<Role> queryAllRoles();

    /**
     * 添加角色
     * @param role 角色实例
     * @return 执行状态
     */
    public int addRole(Role role);
    /**
     * 通过角色ID 查询角色信息
     * @param rid 角色id
     * @return 角色
     */
    public Role getRoleById(int rid);

    /**
     * 删除角色
     * @param rid 角色id
     * @return 执行状态
     */
    public int delRole(int rid);

    /**
     * 通过角色ID 修改角色信息
     * @param role 角色
     * @return 执行状态
     */
    public int updateRole(Role role);

}
