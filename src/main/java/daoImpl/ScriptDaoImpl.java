package daoImpl;

import bean.Script;
import common.CommonParam;
import common.JDBCUtil;
import dao.ScriptDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 *
 * Created by sofronie on 2017/12/11.
 */
public class ScriptDaoImpl extends HibernateDaoSupport implements ScriptDao {


    /**
     * 获取所有的测试脚本列表
     * @return 自动化测试脚本列表
     */
    public List<Script> getScriptList(int userRole,int userId,Script script){

        JDBCUtil util = new JDBCUtil();
        //定义相关对象
        List<Script> scriptList = new ArrayList<Script>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();

        try {
            conn = util.getConnerction();

            if(userRole == 1){

                sql.append("select script.*,users.username from script,users where script.creatorId = users.userId");
                //检索的脚本id不为空
                if (script.getSearchId() != 0){
                    sql.append(" and script.scriptId = "+script.getSearchId());
                }
                //检索的名字不为空
                if (script.getSearchName() != null && !script.getSearchName().equals("")){
                    sql.append(" and script.scriptName like '"+"%"+script.getSearchName()+"%\'");
                }
                //检索的创建者不为空
                if (script.getSearchCreator() != 0){
                    sql.append(" and script.creatorId = "+script.getSearchCreator());
                }

                sql.append(" and script.isDelete = ?  ORDER BY script.updateTime DESC ");

                ps = conn.prepareStatement(sql.toString());
                ps.setInt(1, CommonParam.NOT_DELETE);

            }else{
                sql.append("select * from script where 1=1");
                //检索的脚本id不为空
                if (script.getSearchId() != 0){
                    sql.append(" and script.scriptId = "+script.getSearchId());
                }
                //检索的名字不为空
                if (script.getSearchName() != null && !script.getSearchName().equals("")){
                    sql.append(" and script.scriptName like '"+"%"+script.getSearchName()+"%\'");
                }
                sql.append(" and isDelete = ? and creatorId = ? ORDER BY updateTime DESC");

                ps = conn.prepareStatement(sql.toString());
                ps.setInt(1,CommonParam.NOT_DELETE);
                ps.setInt(2,userId);
            }

            rs = ps.executeQuery();

            while (rs.next()){
                //定义自动化测试脚本对象
                Script scriptBean = new Script();
                //赋值
                scriptBean.setId(rs.getInt("scriptId"));  //id
                scriptBean.setName(rs.getString("scriptName"));   //名字
                scriptBean.setLocation(rs.getString("location"));   //路径
                scriptBean.setScriptDesc(rs.getString("scriptDesc"));   //描述
                scriptBean.setCreator(rs.getInt("creatorId"));    //创建者
                scriptBean.setIsDelete(rs.getInt("isDelete"));  //是否删除
                scriptBean.setCreateTime(rs.getTimestamp("createTime"));    //创建时间
                scriptBean.setUpdateTime(rs.getTimestamp("updateTime"));    //修改时间
                //角色权限是管理员
                if(userRole == 1) {
                    scriptBean.setCreatorName(rs.getString("username"));
                }
                scriptList.add(scriptBean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                ps.close();
                conn.close();
                util.closeConnerction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }
        return scriptList;
    }



    /**
     * 添加自动化测试脚本
     * @param script 自动化测试脚本对象
     * @return 执行状态
     */
    public int addScript(Script script){

        JDBCUtil util = new JDBCUtil();
        int result = 0;
        //定义数据库相关
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            //获取数据库连接
            conn = util.getConnerction();
            //定义sql
            String sql = "insert into script(scriptName,location,scriptDesc,creatorId,isDelete,createTime,updateTime) value(?,?,?,?,?,?,?)";
            //获取预定义对象
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setString(1,script.getName());       //脚本名称
            ps.setString(2,script.getLocation());   //脚本路径
            ps.setString(3,script.getScriptDesc());       //脚本描述
            ps.setInt(4,script.getCreator());       //脚本创建者
            ps.setInt(5,CommonParam.NOT_DELETE);    //是否删除
            //获取当前时间
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(6,timestamp);           //创建时间
            ps.setTimestamp(7,timestamp);           //修改时间
            //返回影响数据条数
            result = ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                ps.close();
                conn.close();
                util.closeConnerction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }

        return  result;
    }


    /**
     * 通过脚本id获取脚本详细信息
     * @param scriptId 脚本id
     * @return 指定脚本
     */
    public Script getSciptById(int scriptId){
        //定义返回值
        Script script = null;

        JDBCUtil util = new JDBCUtil();

        //定义数据库操作相关
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = util.getConnerction();
            String sql = "select script.*,users.username from script,users where script.creatorId = users.userId and script.scriptId = ? and script.isDelete = ?";
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setInt(1,scriptId);
            ps.setInt(2,CommonParam.NOT_DELETE);
            //执行查询
            rs = ps.executeQuery();

            while (rs.next()){
                script = new Script();
                //赋值
                script.setId(rs.getInt("scriptId"));  //id
                script.setName(rs.getString("scriptName"));   //名字
                script.setLocation(rs.getString("location"));   //路径
                script.setScriptDesc(rs.getString("scriptDesc"));   //描述
                script.setCreator(rs.getInt("creatorId"));    //创建者
                script.setIsDelete(rs.getInt("isDelete"));  //是否删除
                script.setCreateTime(rs.getTimestamp("createTime"));    //创建时间
                script.setUpdateTime(rs.getTimestamp("updateTime"));    //修改时间
                script.setCreatorName(rs.getString("username"));    //创建者名字
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                ps.close();
                conn.close();
                util.closeConnerction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }
        return script;
    }


    /**
     * 通过脚本id删除指定脚本
     * @param scriptId 脚本id
     * @return 影响记录条数
     */
    public int deleteScriptById(int scriptId){
        //定义返回值
        int num = 0;

        //定义数据库相关
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;


        try {
            //获取数据库连接
            conn = util.getConnerction();
            String sql = "update script set isDelete = ? where scriptId = ?";
            //预定义sql
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setInt(1,CommonParam.DELETED);   //删除
            ps.setInt(2,scriptId);              //脚本id

            //sql执行
            num = ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //关闭数据库相关连接
            try {
                ps.close();
                conn.close();
                util.closeConnerction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }
        return num;
    }



    /**
     * 获取脚本创建者列表
     * @return 创建者列表
     */
    public Map<Integer,String> getCreatorList(){
        //定义返回值
        Map<Integer,String> map = null;

        //获取数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //连接数据库
            conn = util.getConnerction();
            //获取所有不是管理员的用户信息
            String sql = "select userId,username from users where roleId <> 1 and isDelete = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,CommonParam.NOT_DELETE);
            //执行查询
            rs = ps.executeQuery();
            //实例化对象
            map = new HashMap<Integer, String>();
            while (rs.next()){
                map.put(rs.getInt("userId"),rs.getString("username"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭数据库
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回对象
        return map;
    }


    /**
     * 修改自动化测试脚本
     * @param script 自动化测试脚本对象
     * @return 影响数据条数
     */
    public int updateScript(Script script){
        //定义返回值
        int num = 0;
        //定义数据库相关对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            //获取数据库连接
            conn = util.getConnerction();
            //定义sql语句
            String sql = "update script set scriptName = ?,location = ?,scriptDesc = ?,updateTime = ? where scriptId = ? and isDelete = ?";
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setString(1,script.getName());       //脚本名称
            ps.setString(2,script.getLocation());   //脚本路径
            ps.setString(3,script.getScriptDesc()); //脚本描述
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(4,timestamp);           //修改时间
            ps.setInt(5,script.getId());            //脚本id
            ps.setInt(6,CommonParam.NOT_DELETE);    //未删除


            num = ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                ps.close();
                conn.close();
                util.closeConnerction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }
        return num;
    }

    /**
     * 通过用例ID查询对应的脚本信息
     * @param caseIds 用例id组
     * @return 脚本信息
     */
    public List<Script> getScriptByCaseId(String[] caseIds){
        //定义返回值对象
        List<Script> scriptList = new ArrayList<Script>();
        //获取数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            //根据参数列表的大小生成in串
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<caseIds.length;i++){
                sb.append("?,");
            }
            sb.deleteCharAt(sb.length()-1);
            //定义sql语句
            String sql = "select * from testcase,script where testcase.scriptId = script.scriptId and testcase.caseId in ( "+sb.toString()+" )and testcase.isDelete = 0";
            ps = conn.prepareStatement(sql);
            //根据参数列表设置sql参数
            for(int j=0;j<caseIds.length;j++){
                ps.setString(j+1,caseIds[j]);       //赋值
            }
            //执行查询
            rs = ps.executeQuery();

            while (rs.next()){
                Script scriptBean = new Script();
                //赋值
                scriptBean.setId(rs.getInt("scriptId"));  //id
                scriptBean.setName(rs.getString("scriptName"));   //名字
                scriptBean.setLocation(rs.getString("location"));   //路径
                scriptBean.setScriptDesc(rs.getString("scriptDesc"));   //描述
                scriptBean.setCreator(rs.getInt("creatorId"));    //创建者
                scriptBean.setIsDelete(rs.getInt("isDelete"));  //是否删除
                scriptBean.setCreateTime(rs.getTimestamp("createTime"));    //创建时间
                scriptBean.setUpdateTime(rs.getTimestamp("updateTime"));    //修改时间
                scriptBean.setCaseId(rs.getInt("caseId"));       //用例id

                scriptList.add(scriptBean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                ps.close();
                conn.close();
                util.closeConnerction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }
        return scriptList;
    }
}
