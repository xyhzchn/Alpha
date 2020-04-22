package serviceImpl;

import bean.Role;
import dao.RoleDao;
import daoImpl.RoleDaoImpl;
import service.RoleService;

import java.util.List;

/**
 * 角色相关service接口实现类
 * Created by sofronie on 2017/7/21.
 */
public class RoleServiceImpl  implements RoleService {

    //定义RoleDao 依赖注入
    private RoleDao roleDao = new RoleDaoImpl();

    /**
     * 获取所有的角色信息
     * @return List<Role>
     */
    public List<Role> queryAllRoles(){
        //调用dao对应方法
        return roleDao.queryAllRoles();
    }
    /**
     * 添加角色
     * @param role 角色实例
     * @return 执行状态
     */
    public int addRole(Role role){
        //调用dao对应方法
        return roleDao.addRole(role);
    }

    /**
     * 通过角色ID 查询角色信息
     * @param rid 角色id
     * @return 角色
     */
    public Role getRoleByID(int rid){
        //调用dao对应方法
        return roleDao.getRoleById(rid);
    }
    /**
     * 删除角色
     * @param rid 角色id
     * @return 执行状态
     */
    public int delRole(int rid){
        //调用dao对应方法
        return roleDao.delRole(rid);
    }

    /**
     * 通过角色的ID，修改角色信息
     * @param role 角色
     * @return 执行状态
     */
    public int updateRole(Role role){
        return roleDao.updateRole(role);
    }

    /**-----------------------getter  and setter-----------------------*/

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }
}
