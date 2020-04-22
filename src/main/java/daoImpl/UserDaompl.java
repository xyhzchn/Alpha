package daoImpl;

import bean.User;
import common.CommonParam;
import common.JDBCUtil;
import dao.UserDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * 用户相关操作实现类
 * Created by sofronie on 2017/7/19.
 */
public class UserDaompl extends HibernateDaoSupport implements UserDao {

    /**
     * 获取所有的用户列表
     * @return List<User>
     */
    public List<User> getAllUsers(){
        //定义接收对象
        List<User> users = null;
        //定义数据库相关对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //创建数据库连接
            conn = util.getConnerction();
            //定义查询语句
            String sql="select * from users inner join role on users.roleId = role.roleId and role.isDelete = ? ORDER BY users.updateTime DESC ";
            //连接
            ps = conn.prepareStatement(sql);
            ps.setInt(1,CommonParam.NOT_DELETE);
            //执行查询
            rs = ps.executeQuery();
            //实例化对象
            users = new ArrayList<User>();
            //迭代并赋值
            while(rs.next()){
                //定义实例
                User user=new User();
                user.setUserId(rs.getInt("userId"));              //用户id
                user.setRoleId(rs.getInt("roleId"));              //用户角色id
                user.setUsername(rs.getString("username")); //用户名称
                user.setRoleName(rs.getString("rolename")); //角色名称
                user.setPassword(rs.getString("password")); //用户密码
                user.setPhone(rs.getString("phone"));       //用户手机号码
                user.setIsDelete(rs.getInt("isDelete"));    //用户删除状态
                user.setCreateTime(rs.getDate("createTime"));   //用户创建时间
                user.setUpdateTime(rs.getDate("updateTime"));   //用户修改时间
                //将所有对象添加到LIST中
                users.add(user);
            }
        }catch (SQLException e){
            e.getStackTrace();
        }finally {
            try{
                //关闭数据库相关操作
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回list
        return users;
    }

    /**
     * 获取角色列表
     * @return Map<Integer, String>
     */
    public Map<Integer, String> getRoleList(){
        //定义接收对象
        Map<Integer,String> map = new HashMap<Integer, String>();
        //数据库相关定义
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //创建数据库连接
           conn =  util.getConnerction();
           //定义查询语句
           String sql = "select roleId,rolename from role where isDelete = ?";
           ps = conn.prepareStatement(sql);
           //赋值
           ps.setInt(1, CommonParam.NOT_DELETE);
           //执行查询
           rs = ps.executeQuery();

           while (rs.next()){
               //map赋值
               map.put(rs.getInt("roleId"),rs.getString("rolename"));
           }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回角色id和角色名称组成的键值对
        return map;
    }

    /**
     * 添加用户到数据库
     * @param user 添加用户对象
     * @return 执行结果
     */
    public int addUser(User user){
        //定义执行结果
        int num = 0;
        //数据库相关对象定义
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = util.getConnerction();
            //执行sql
            String sql = "insert into users(roleId,username,password,phone,isDelete,createTime,updateTime) values (?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setInt(1,user.getRoleId());         //角色id
            ps.setString(2,user.getUsername()); //用户名
            ps.setString(3,user.getPassword()); //密码
            ps.setString(4,user.getPhone());    //电话号码，可为空
            ps.setInt(5, CommonParam.NOT_DELETE);    //删除状态
            //创建日期和时间
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(6,timestamp);       //设置用户创建时间
            ps.setTimestamp(7,timestamp);       //设置用户修改时间

            //执行添加操作并返回影响记录条数
            num = ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭连接
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回执行结果
        return num;
    }

    /**
     * 根据uid禁用用户
     * @param uid 用户id
     * @return 执行结果
     */
    public int disableUser(int uid){
        //定义执行结果
        int num = 0;
        //定义数据库相关对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
         //获取连接
         conn = util.getConnerction();
         //设置是否删除为:已删除
         String sql = "update users set isDelete = ?,updateTime = ? where userId = ?";
         ps = conn.prepareStatement(sql);
         //赋值
         ps.setInt(1,CommonParam.DELETED);
         Date date = new Date();
         Timestamp timestamp = new Timestamp(date.getTime());
         ps.setTimestamp(2,timestamp);           //修改时间
         ps.setInt(3,uid);
         //执行查询操作
         num = ps.executeUpdate();

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

    /**
     * 根据uid启用用户
     * @param uid 用户id
     * @return 执行结果
     */
    public int enableUser(int uid){
        //定义执行结果
        int num = 0;
        //定义数据库操作相关对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获得数据库连接
            conn = util.getConnerction();
            //设置是否删除为已删除
            String sql = "update users set isDelete = ?,updateTime = ? where userId = ?";
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setInt(1,CommonParam.NOT_DELETE);    //删除状态
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(2,timestamp);           //修改时间
            ps.setInt(3,uid);                       //用户id
            //执行修改操作
            num = ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭相关连接
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return num;
    }
}