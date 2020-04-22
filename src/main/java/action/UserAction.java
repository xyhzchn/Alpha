package action;

import bean.User;
import com.opensymphony.xwork2.ActionSupport;
import service.UserService;
import serviceImpl.UserServiceImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理相关处理
 * Created by sofronie on 2017/7/25.
 */
public class UserAction extends ActionSupport {
    //定义User对象
    private User user;
    //定义uid
    private int uid;
    //定义service对象
    private UserService service = new UserServiceImpl();
    //定义接受所有角色的list并获取get set方法
    private List<User> users = new ArrayList<User>();
    //定义用户添加界面的角色下拉列表
    private Map<Integer, String> roleset = new HashMap<Integer, String>();



    /**
     * 获取用户列表
     * @return String
     */
    public String getUserList(){

        users = service.getAllUsers();

        if (users.size() > 0){
            return SUCCESS;
        }else {
            return "failure";
        }
    }

    /**
     * 获取用户添加页面的角色列表
     * @return String
     */
    public String getRoleList(){
        //获取角色列表
        roleset = service.getRoleList();
        //判断角色列表
        if (roleset.size() > 0){
            return SUCCESS;
        }else{
            return "failure";
        }
    }

    /**
     * 添加用户
     * @return String
     */
    public String addUser(){
        //执行添加用户操作
        int num = service.addUser(user);
        //判断修改数据记录条数
        if (num > 0){
            return SUCCESS;
        }else {
            return "failure";
        }
    }


    /**
     * 禁用用户
     * @return String
     */
    public String disableUser(){
        //影响记录条数
        int num = service.disableUser(uid);

        if (num>0){
            return SUCCESS;
        }else {
            return  "failure";
        }

    }

    /**
     * 启用用户
     * @return String
     */
    public String enableUser(){

        //影响记录条数
        int num = service.enableUser(uid);

        if (num>0){
            return SUCCESS;
        }else {
            return  "failure";
        }
    }

    /**------------------------------------getter  and  setter---------------------------------*/
    //user对象的get set方法
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //service对象的get set方法
    public UserService getService() {
        return service;
    }

    public void setService(UserService service) {
        this.service = service;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Map<Integer, String> getRoleset() {
        return roleset;
    }

    public void setRoleset(Map<Integer, String> roleset) {
        this.roleset = roleset;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
