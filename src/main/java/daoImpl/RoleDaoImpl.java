package daoImpl;

import bean.Role;
import common.CommonParam;
import common.JDBCUtil;
import dao.RoleDao;
import org.hibernate.cfg.annotations.ResultsetMappingSecondPass;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色roleDao接口实现类
 * Created by sofronie on 2017/7/21.
 */
public class RoleDaoImpl extends HibernateDaoSupport implements RoleDao {


    /**
     * 查询所有的角色信息
     * @return
     */
    public List<Role> queryAllRoles(){

        JDBCUtil util = new JDBCUtil();
        //定义相关对象
        List<Role> roles = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //创建连接并查询
            conn = util.getConnerction();
            //创建sql语句，查询数据库中没有被删除的角色列表
            String sql = "select * from role where isDelete = 0 ORDER by updateTime DESC ";
            ps = conn.prepareStatement(sql);
            //执行查询并赋值
            rs = ps.executeQuery();

            roles = new ArrayList<Role>();
            //迭代查询结果并赋值
            while(rs.next()){
                //定义实例化对象
                Role role = new Role();
                //对象赋值
                role.setRoleId(rs.getInt("roleId"));                //角色id
                role.setRoleName(rs.getString("rolename"));         //角色名称
                role.setRoleDesc(rs.getString("roleDesc"));         //角色描述
                role.setIsDelete(rs.getInt("isDelete"));            //是否删除
                role.setCreateTime(rs.getDate("createTime"));  //创建时间
                role.setUpdateTime(rs.getDate("updateTime"));  //修改时间
                //将实例添加到role list中
                roles.add(role);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭数据库连接
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //返回查询结果
        return roles;
    }

    /**
     * 添加角色
     * @param role 角色实例
     * @return 执行结果
     */
    public int addRole(Role role){

        //定义执行sql影响行数。
        int num = 0;
        //定义连接
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //创建数据库连接
            conn = util.getConnerction();
            //编写sql语句
            String sql = "insert into role(rolename,roleDesc,isDelete,createTime,updateTime) value(?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setString(1,role.getRoleName()); //角色名称
            ps.setString(2,role.getRoleDesc()); //角色描述
            ps.setInt(3, CommonParam.NOT_DELETE); //删除状态，默认未删除
            //定义角色创建时间
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(4,timestamp);       //角色创建时间
            ps.setTimestamp(5,timestamp);       //角色修改时间

            //语句执行
            num = ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭数据库连接
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return num;
    }

    /**
     * 根据角色ID，查询角色信息
     * @param rid 角色id
     * @return 角色
     */
    public Role getRoleById(int rid){
        //定义
        Role role = null;
        //定义数据库连接信息
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取连接
            conn = util.getConnerction();
            //查询指定角色
            String sql = "select * from role where roleId = ?";
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setInt(1,rid);
            //执行查询并保存结果
            rs = ps.executeQuery();
            //获取角色信息
            while (rs.next()){
                //定义实例
                role = new Role();
                role.setRoleId(rs.getInt("roleId"));                      //角色id
                role.setRoleName(rs.getString("rolename"));         //角色名称
                role.setRoleDesc(rs.getString("roleDesc"));         //角色描述
                role.setIsDelete(rs.getInt("isDelete"));            //角色是否删除
                role.setCreateTime(rs.getDate("createTime"));  //角色创建时间
                role.setUpdateTime(rs.getDate("updateTime"));  //角色修改时间
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭连接
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return role;
    }

    /**
     * 删除角色信息，将角色的isDelete改为1
     * @param rid 角色id
     * @return 执行状态
     */
    public int delRole(int rid){
        //定义执行状态默认值
        int num = 0;

        int count = 0;
        //连接数据库
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            //创建连接
            conn = util.getConnerction();
            //创建事务
            util.beginTransaction(conn);

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            //查询当前角色下的用户数量
            String searchUserCount = "select count(*) from users where roleId = ? and isDelete = ?";
            ps2 = conn.prepareStatement(searchUserCount);
            ps2.setInt(1,rid);
            ps2.setInt(2,CommonParam.NOT_DELETE);

            rs = ps2.executeQuery();
            if (rs.next()){
                count = rs.getInt(1);
            }
            if(count > 0){
                //逻辑删除角色下的所有用户
                String deleteUserSql = "update users set isDelete = ? ,updateTime = ? where roleId = ?";
                ps1 = conn.prepareStatement(deleteUserSql);
                //赋值
                ps1.setInt(1,CommonParam.DELETED);
                ps1.setTimestamp(2,timestamp);
                ps1.setInt(3,rid);

                num = ps1.executeUpdate();
            }

            //逻辑删除角色
            String sql = "update role set isDelete = ? ,updateTime = ? where roleId = ?";
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setInt(1,CommonParam.DELETED);       //删除状态：已删除

            ps.setTimestamp(2,timestamp);
            ps.setInt(3,rid);                       //角色id
            //执行数据库操作
            num = ps.executeUpdate();

            //提交事务
            util.commitTransaction(conn);
        }catch (SQLException e){
            try{
                util.rollbackTransaction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            try {
                if (count > 0){
                    ps1.close();
                }
                rs.close();
                ps2.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回执行状态
        return num;
    }

    /**
     * 修改角色信息
     * @param role 角色
     * @return 执行状态
     */
    public int updateRole(Role role){
        //定义执行状态
        int num = 0;
        //定义连接
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            if (role != null){
                //创建连接
                conn = util.getConnerction();
                //定义执行语句
                String sql = "update role set rolename = ?,roleDesc = ?,updateTime = ? where roleId = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1,role.getRoleName());  //角色名称
                ps.setString(2,role.getRoleDesc());  //角色描述
                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                ps.setTimestamp(3,timestamp);        //修改角色时间
                ps.setInt(4,role.getRoleId());          //角色id
                //执行修改操作
                num = ps.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return num;
    }
}
