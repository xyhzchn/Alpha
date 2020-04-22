package service;

import bean.User;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by sofronie on 2017/7/19.
 */
public interface UserService {

    /**
     * 获取当前所有的用户信息
     * @return List<User>
     */
    public List<User> getAllUsers();

    /**
     * 获取添加用户界面的角色列表
     * @return Map<Integer, String>
     */
    public Map<Integer, String> getRoleList();

    /**
     * 添加用户
     * @param user 用户对象
     * @return int
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
