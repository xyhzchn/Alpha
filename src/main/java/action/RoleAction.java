package action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import service.RoleService;
import serviceImpl.RoleServiceImpl;
import bean.Role;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by sofronie on 2017/7/21.
 */
public class RoleAction extends ActionSupport {

    //定义rid
    private  int rid;
    //定义页面flag
    private  String flag;
    //定义Role及get set方法
    private Role role = new Role();
    //定义RoleService
    private RoleService roleService = new RoleServiceImpl();
    //定义接受所有角色的list
    private List<Role> roles = new ArrayList<Role>();

    /**
     * 获取所有的角色列表
     * @return String
     */
    public String getRoleList(){
        //查询所有的角色列表
        roles = roleService.queryAllRoles();

        if (roles.size() > 0){
            return SUCCESS;
        }else {
            return "failure";
        }
    }

    /**
     * 添加角色信息
     * @return String
     */
    public String addRole(){

        //添加角色
        int num = roleService.addRole(role);
        //添加数据执行状态
        if (num > 0){
            return SUCCESS;
        }else {
            return "failure";
        }
    }

    /**
     * 添加角色时，输入信息验证
     */
    public void validateAddRole(){
        //角色名称不可为空
        if(role.getRoleName() == null || role.getRoleName().equals("")){
            addFieldError("roleNameIsNull","*角色名称不可为空");
        }

    }

    /**
     * 角色详情页面
     * @return String
     */
    public String getRoleByID(){
        //设置flag，因为跳转的页面一致，需根据flag判断显示
        flag = "detail";
        //查询对应id下的角色
        role = roleService.getRoleByID(rid);
        //判断查询结果
        if (role != null){
            return SUCCESS;
        }else {
            return "failure";
        }

    }

    /**
     * 删除角色
     * @return String
     */
    public String delRole(){
        //根据角色id删除角色
        int num = roleService.delRole(rid);
        //执行状态>0
        if (num > 0){
            return SUCCESS;
        }else {
            return "failure";
        }
    }

    /**
     * 根据id修改角色信息
     * @return String
     */
    public String updateRole(){
        //定义response对象
        HttpServletResponse response = ServletActionContext.getResponse();
        //设置返回数据的格式
        response.setContentType("text/html;charset=utf-8");
        //定义输出流
        PrintWriter out = null;
        //修改角色
        int num  = roleService.updateRole(role);
        //执行状态
            try{
                out = response.getWriter();
                if(num > 0){
                    out.print("success");
                }else{
                    out.print("failure");
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                out.flush();
                out.close();
            }

            return null;
    }


    /**-----------------------------------getter  and  setter-----------------------------*/
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
