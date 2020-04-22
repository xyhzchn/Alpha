package common;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * Created by sofronie on 2017/7/19.
 */
public class JDBCUtil {


    private static DataSource ds = new ClassPathXmlApplicationContext("applicationContext.xml").getBean("dataSource",DataSource.class);

    //private static ThreadLocal<Connection> t1 = new ThreadLocal<Connection>();

    public static DataSource getDataSource(){
        return ds;
    }

    /**
     * 获取数据库连接
     * @return conn
     * @throws SQLException e
     */
    public Connection getConnerction()throws SQLException{

        Connection conn = ds.getConnection();
        return conn;
    }

    /**
     * 开启事务
     * @throws SQLException
     */
    public void beginTransaction(Connection conn)throws SQLException{
        //关闭sql语句自动提交
        conn.setAutoCommit(false);
    }

    /**
     * 提交事务
     * @throws SQLException
     */
    public void commitTransaction(Connection conn)throws SQLException{
        //手动提交事务
        conn.commit();
    }

    /**
     * 回滚事务
     * @throws SQLException
     */
    public void rollbackTransaction(Connection conn)throws SQLException{
        if(conn == null) throw new SQLException("没有事务不能回滚");
        conn.rollback();
    }
    /**
     * 关闭数据库连接
     * @throws SQLException e
     */
    public void closeConnerction(Connection conn)throws SQLException{
//        if(conn != conn){//如果参数连接与当前事务连接不同，说明这个连接不是当前事务，可以关闭
//            conn.close();
//        }
        conn.close();
    }
}
