package bean;

import java.util.Date;

/**
 * 用户相关javabean
 * Created by sofronie on 2017/7/19.
 */
public class
User {
    //用户id
    private int userId;
    //角色id
    private int roleId;
    //用户名
    private String username;
    //用户密码
    private String password;
    //用户联系方式
    private String phone;
    //用户是否禁用 0：未禁用   1：已禁用
    private int isDelete;
    //用户创建时间
    private Date createTime;
    //结束时间
    private Date updateTime;
    //用户对应角色名称
    private String roleName;

    /**------------------------------getter and  setter ---------------------------*/

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
