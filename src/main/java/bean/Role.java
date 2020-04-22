package bean;

import java.util.Date;

/**
 * Created by sofronie on 2017/7/21.
 */
public class Role {
    //RID
    private int roleId;
    //角色名称
    private String roleName;
    //角色描述
    private String roleDesc;
    //是否已删除
    private int isDelete;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;



    public String getRoleName() {
        return roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public int getIsDelete() {
        return isDelete;
    }



    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
