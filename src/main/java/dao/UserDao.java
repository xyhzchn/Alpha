package dao;

import bean.User;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by sofronie on 2017/7/19.
 */
public interface UserDao {
    /**
     * 获取用户列表。
     * @return List<User>
     */
    public List<User> getAllUsers();

    /**
     * 获取角色列表
     * @return Map<Integer, String>
     */
    public Map<Integer, String> getRoleList();

    /**
     * 添加用户
     * @param user 用户对象
     * @return 执行状态
     */
    public int addUser(User user);

    /**
     * 根据uid禁用用户
     * @param uid 用户id
     * @return int
     */
    public int disableUser(int uid);

    /**
     * 根据uid启用用户
     * @param uid 用户id
     * @return 执行结果
     */
    public int enableUser(int uid);
}
