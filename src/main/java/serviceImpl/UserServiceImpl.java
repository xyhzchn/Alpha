package serviceImpl;

import bean.User;
import dao.UserDao;
import daoImpl.UserDaompl;
import service.UserService;

import java.util.List;
import java.util.Map;

/**
 * 用户相关service实现类
 * Created by sofronie on 2017/7/19.
 */
public class UserServiceImpl implements UserService {

    //userDao  依赖注入
    private  UserDao userDao = new UserDaompl();

    /**
     * 获取所有用户信息
     * @return List<User>
     */
    public List<User> getAllUsers(){
        //调用userDao对应方法
        return userDao.getAllUsers();
    }

    /**
     * 获取角色列表
     * @return Map<Integer, String>
     */
    public Map<Integer, String> getRoleList(){
        //调用userDao对应方法
        return userDao.getRoleList();
    }

    /**
     * 添加用户到数据库
     * @param user 用户对象
     * @return 执行状态
     */
    public int addUser(User user){
        //调用userDao对应方法
        return userDao.addUser(user);
    }

    /**
     * 根据uid禁用用户
     * @param uid 用户id
     * @return int
     */
    public int disableUser(int uid){
        //调用userDao对应方法
        return userDao.disableUser(uid);
    }

    /**
     * 根据uid启用用户
     * @param uid 用户id
     * @return 执行结果
     */
    public int enableUser(int uid){
        //调用userDao对应方法
        return userDao.enableUser(uid);
    }

    /**-----------------------------getter  and  setter---------------------*/
    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
