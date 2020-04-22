package daoImpl;

import bean.User;
import common.JDBCUtil;
import dao.LoginDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by sofronie on 2017/8/23.
 */
public class LoginDaoImpl extends HibernateDaoSupport implements LoginDao{

    /**
     * 获取登录用户信息
     * @param user 登录用户
     * @return 登录用户信息
     */
    public User getUserByUserName(User user){
        //实例化
        User userBean = null;
        //定义数据库相关操作
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            String sql = "select * from users where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,user.getUsername());
            //查询用户
            rs = ps.executeQuery();
            while (rs.next()){
                //实例化
                userBean = new User();
                userBean.setUserId(rs.getInt("userId"));                      //用户id
                userBean.setRoleId(rs.getInt("roleId"));                      //角色id
                userBean.setUsername(rs.getString("username"));         //用户名
                userBean.setPassword(rs.getString("password"));         //用户密码
                userBean.setPhone(rs.getString("phone"));               //用户手机号
                userBean.setIsDelete(rs.getInt("isDelete"));            //是否删除
                userBean.setCreateTime(rs.getTimestamp("createTime"));  //创建时间
                userBean.setUpdateTime(rs.getTimestamp("updateTime"));  //修改时间
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭数据库连接
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return userBean;
    }
}
