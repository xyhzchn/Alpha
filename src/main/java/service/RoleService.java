package service;

import bean.Role;
import java.util.List;

/**
 * 角色相关service类
 * Created by sofronie on 2017/7/21.
 */
public interface RoleService {
    /**
     * 获取所有的角色信息
     * @return 返回数据库中角色列表
     */
    public List<Role> queryAllRoles();

    /**
     * 添加角色信息
     * @param role 角色实例
     */
    public int addRole(Role role);

    /**
     * 通过角色ID查询出某一角色，查看该角色详情
     * @param rid 角色id
     * @return 角色
     */
    public Role getRoleByID(int rid);

    /**
     * 删除角色信息
     * @param rid 角色id
     * @return 执行状态
     */
    public int delRole(int rid);

    /**
     * 通过角色ID修改某一角色
     * @param role 角色
     * @return 执行状态
     */
    public int updateRole(Role role);
}
