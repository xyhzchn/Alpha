package daoImpl;

import bean.Tree;
import common.CommonParam;
import common.JDBCUtil;
import dao.TreeDao;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by sofronie on 2017/7/27.
 */
public class TreeDaoImpl extends HibernateDaoSupport implements TreeDao {

    /**
     * 添加子节点
     * @return 添加的节点数量
     */
    public int setRootNote(Tree tree){
        //定义返回值
        int num = 0;
        //定义连接
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = util.getConnerction();

            //多个数据库操作，开启事务
            util.beginTransaction(conn);

            //如果父节点的ID不为空，则通过父节点的ID，查询父节点的level，设置字节的的level=父节点的level+1；
            if(tree.getParent_id() != 0){
                String parentLevelSql = "select level from catalog where catalogId = ?";

                ps = conn.prepareStatement(parentLevelSql);
                ps.setInt(1,tree.getParent_id());
                //查询对应的父节点
                rs = ps.executeQuery();
                while (rs.next()){
                    //设置子节点的level
                    tree.setLevel(rs.getInt("level")+1);
                }
            }

            //在数据库中插入子节点的相关数据
            String sql = "insert into catalog(catalogName,Ename,level,parent_id,isDelete,createTime,updateTime) values(?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            //id自增，name和ename在页面上获取
            ps.setString(1,tree.getCatalogName());
            ps.setString(2,tree.getEname());
            //设置子节点的level，0级目录默认显示为0
            ps.setInt(3,tree.getLevel());
            //设置父节点的ID。0级目录默认为0
            ps.setInt(4,tree.getParent_id());
            //设置删除状态为0：未删除
            ps.setInt(5,CommonParam.NOT_DELETE);
            //设置创建时间和修改时间，当前为同一时间
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            //设置创建时间和修改时间
            ps.setTimestamp(6,timestamp);
            ps.setTimestamp(7,timestamp);
            //执行sql
            num = ps.executeUpdate();

            //提交事务
            util.commitTransaction(conn);
        }catch (SQLException e){
            try{
                //如果事务中有错误。则回滚
                util.rollbackTransaction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }

        }finally {
            //最后关闭相关数据库连接信息
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                util.closeConnerction(conn);
            }catch (SQLException e){
                //打出堆栈信息
                e.printStackTrace();
            }
        }
        return num;
    }

    /**
     * 查询数据库中所有的节点信息
     * @return 所有节点列表
     */
    public List<Tree> selectAllNodes() {

        //定义返回值
        List<Tree> trees = new ArrayList<Tree>();

        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //创建数据库连接
            conn = util.getConnerction();
            //定义sql
            String sql = "select * from catalog where isDelete = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1,CommonParam.NOT_DELETE);
            //查询并返回结果
            rs = ps.executeQuery();
            //赋值
            while (rs.next()){
                //创建对象并赋值
                Tree tree = new Tree();
                tree.setCatalogId(rs.getInt("catalogId"));          //节点id
                tree.setCatalogName(rs.getString("catalogName"));   //节点中文名字
                tree.setEname(rs.getString("Ename"));               //节点英文名字
                tree.setLevel(rs.getInt("level"));                  //节点等级
                tree.setParent_id(rs.getInt("parent_id"));          //父节点
                tree.setIsDelete(rs.getInt("isDelete"));            //是否已删除
                tree.setCreateTime(rs.getTimestamp("createTime"));  //节点创建时间
                tree.setUpdateTime(rs.getTimestamp("updateTime"));  //节点修改时间

                //组成list
                trees.add(tree);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return trees;
    }

    /**
     * 删除节点
     * @param catalog_id 节点id
     * @return 删除节点数量
     */
    public int deleteNode(int catalog_id){

        //定义返回值
        int num = 0;
        //定义连接相关
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            //开启事务
            util.beginTransaction(conn);

            //查询节点下的用例列表，并修改为已删除
            String selectSql = "select * from testCase where catalogId = ?";
            //赋值
            ps1 = conn.prepareStatement(selectSql);
            ps1.setInt(1,catalog_id);
            //执行查询
            rs = ps1.executeQuery();
            //结果不为空
            while (rs.next()){
                //定义修改sql
                String updateSql = "update testCase set isDelete = ?,updateTime = ? where catalogId = ?";

                ps2 = conn.prepareStatement(updateSql);
                //删除
                ps2.setInt(1,CommonParam.DELETED);
                //修改时间
                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                ps2.setTimestamp(2,timestamp);
                //节点id
                ps2.setInt(3,catalog_id);
                //执行修改操作
                ps2.executeUpdate();
            }
            //删除语句
            String sql = "update catalog set isDelete = ?,updateTime = ? where catalogId = ?";
            ps3 = conn.prepareStatement(sql);
            //赋值
            ps3.setInt(1,CommonParam.DELETED);
            //修改时间
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps3.setTimestamp(2,timestamp);
            //节点id
            ps3.setInt(3,catalog_id);

            //执行
            num = ps3.executeUpdate();

            //提交事务
            util.commitTransaction(conn);

        }catch (SQLException e){
            e.printStackTrace();
            try{
                //如果出错。回滚事务
                util.rollbackTransaction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }

        }finally {
            try {
                if(ps1 != null){
                    ps1.close();
                }
                if(ps2 != null){
                    ps2.close();
                }
                if(ps3 != null){
                    ps3.close();
                }
                if(rs != null){
                    rs.close();
                }
                util.closeConnerction(conn);

            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        return num;
    }

    /**
     * 修改节点
     * @return 修改节点数量
     */
    public int updateNote(Tree tree){
        //定义返回值
        int num = 0;
        //定义数据库操作
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
             //获取数据库连接
             conn =util.getConnerction();
             //定义sql
             String sql = "update catalog set catalogName = ?,Ename = ?,updateTime = ? where catalogId = ? and isDelete = ?";

             ps = conn.prepareStatement(sql);
             //赋值
             ps.setString(1,tree.getCatalogName());     //节点id
             ps.setString(2,tree.getEname());           //节点英文名
             Date date = new Date();
             Timestamp timestamp = new Timestamp(date.getTime());
             ps.setTimestamp(3,timestamp);              //修改时间
             ps.setInt(4,tree.getCatalogId());          //节点id
             ps.setInt(5,CommonParam.NOT_DELETE);       //未删除
             //执行操作
             num = ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if(ps != null){
                    ps.close();
                }
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return num;
    }


    /**
     * 获取当前节点的所有父级节点列表
     * @param catalog_id 节点id
     * @return 节点列表
     */
    public List<Tree> getCaseCatalog(int catalog_id){
        //定义
        List<Tree> trees= null;
        //定义数据库相关连接
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = util.getConnerction();
            //查询当前节点的所有父级节点
            String sql =
                    "select t2.catalogId,t2.catalogName,t2.Ename from(\n" +
                            "select @r as _catalogId,(select @r:=parent_id from catalog where catalogId = _catalogId)AS\n" +
                            "parent_id,@l:=@l+1 as lvl from(select @r:=?,@l:=0)vars,catalog h where @r <> 0)t1\n" +
                            "join catalog t2\n" +
                            "on t1._catalogId = t2.catalogId\n" +
                            "order by t1.lvl desc";
            ps = conn.prepareStatement(sql);
            //设置当前节点的id
            ps.setInt(1,catalog_id);    //节点id
            //执行查询
            rs = ps.executeQuery();

            trees = new ArrayList<Tree>();
            while (rs.next()){
                //实例化
                Tree tree = new Tree();
                tree.setCatalogId(rs.getInt("catalogId"));  //节点id
                tree.setCatalogName(rs.getString("catalogName"));         //节点中文名称
                tree.setEname(rs.getString("Ename"));       //节点英文名称
                //list添加
                trees.add(tree);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭数据库连接
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回查询结果
        return trees;
    }


    /**
     * 根据节点Id获取到节点等级
     * @param catalogId 节点id
     * @return 节点等级
     */
    public int getCatalogLevelById(int catalogId){

        int level = 0;
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //创建连接
            conn = util.getConnerction();

            //定义sql
            String sql = "select level from catalog where catalogId = ? and isDelete = ?;";
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setInt(1,catalogId);
            ps.setInt(2,CommonParam.NOT_DELETE);
            //执行sql
            rs = ps.executeQuery();

            while (rs.next()){
                level = rs.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if(rs !=null){
                    rs.close();
                }
                if(ps!= null){
                    ps.close();
                }
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        return level;
    }


    /**
     * 根据level和name获取节点，若节点存在，则获取该节点的id。或节点不存在，则创建节点，
     * 返回该节点的id
     * @param level 节点等级
     * @param name  节点名字
     * @param parentId  父节点
     * @return  节点id
     */
    public int getCatalogId(int level,String name,int parentId){

        int catalogId = 0;

        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement insertPs = null;
        ResultSet rs = null;
        ResultSet insertRs = null;

        try {
            conn = util.getConnerction();
            String selectSql = "select catalogId from catalog where level=? and catalogName = ? and isDelete = ? and parent_id = ?";
            ps = conn.prepareStatement(selectSql);
            ps.setInt(1,level);
            ps.setString(2,name);
            ps.setInt(3,CommonParam.NOT_DELETE);
            ps.setInt(4,parentId);


            rs = ps.executeQuery();
            while (rs.next()){
                catalogId = rs.getInt("catalogId");
            }

            if(catalogId == 0){

                String insertSql = "insert into catalog(catalogName,Ename,level,parent_id,isDelete,createTime,updateTime) values(?,?,?,?,?,?,?)";
                insertPs = conn.prepareStatement(insertSql,PreparedStatement.RETURN_GENERATED_KEYS);
                //id自增，name和ename在页面上获取
                insertPs.setString(1,name);
                insertPs.setString(2,getHead(name));
                //设置子节点的level，0级目录默认显示为0
                insertPs.setInt(3,level);
                //设置父节点的ID。0级目录默认为0
                insertPs.setInt(4,parentId);
                //设置删除状态为0：未删除
                insertPs.setInt(5,CommonParam.NOT_DELETE);
                //设置创建时间和修改时间，当前为同一时间
                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                //设置创建时间和修改时间
                insertPs.setTimestamp(6,timestamp);
                insertPs.setTimestamp(7,timestamp);

                insertPs.executeUpdate();

                insertRs = insertPs.getGeneratedKeys();

                if(insertRs.next())
                {
                    catalogId = insertRs.getInt(1);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if(insertRs != null){
                    insertRs.close();
                }
                if(insertPs != null){
                    insertPs.close();
                }
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                util.closeConnerction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }

        return catalogId;

    }

    /**
     * 获取项目列表
     * @return 项目列表
     */
    public Map<Integer,String> getProjectList(){
        //定义返回值
        Map<Integer,String> projectMap = null;

        //定义数据库信息
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            conn = util.getConnerction();
            //查询所有等级为0的节点
            String sql = "SELECT catalogId,catalogName FROM catalog where level = 0 and isDelete = "+CommonParam.NOT_DELETE;
            //预定义
            ps = conn.prepareStatement(sql);
            //查询
            rs = ps.executeQuery();

            projectMap = new HashMap<Integer, String>();
            while (rs.next()){
                projectMap.put(rs.getInt("catalogId"),rs.getString("catalogName"));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }

        }

        return projectMap;
    }

    /**
     * 通过子节点的id获取到根节点的id
     * @return 根节点的id
     */
    public int getParentIdBySonId(int catalogId){
        //定义返回值
        int projectEname = 0;
        //定义数据库相关
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{

            conn = util.getConnerction();

            String sql =
                    "select t2.catalogId from(\n" +
                    "select @r as _catalogId,(select @r:=parent_id from catalog where catalogId = _catalogId)AS\n" +
                    "parent_id,@l:=@l+1 as lvl from(select @r:=?,@l:=0)vars,catalog h where @r <> 0)t1\n" +
                    "join catalog t2\n" +
                    "on t1._catalogId = t2.catalogId\n" +
                    "and t2.level = 0\n" +
                    "order by t1.lvl desc";

            ps = conn.prepareStatement(sql);
            //设置子节点id
            ps.setInt(1,catalogId);

            rs = ps.executeQuery();

            while (rs.next()){
                projectEname = rs.getInt("catalogId");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }

        }

        return projectEname;
    }

    /**
     * 根据一个节点查询所有的子节点
     * @param catalogId 节点id
     * @return 所有的子节点
     */
    public Set getSonsById(int catalogId,Set set){

        set.add(catalogId);

        //定义数据库相关
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = util.getConnerction();

            String sql = "select catalogId from catalog where parent_id = ? and isDelete = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1,catalogId);
            ps.setInt(2,CommonParam.NOT_DELETE);

            rs = ps.executeQuery();

            while (rs.next()){
                set.add(rs.getInt("catalogId"));
                getSonsById(rs.getInt("catalogId"),set);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                util.closeConnerction(conn);

            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        return set;
    }


    /**
     * 根据节点id获取节点的英文名
     * @param catalogId 节点id
     * @return 节点英文名
     */
    public String getEnameById(int catalogId){
        String ename = "";

        //定义数据库相关
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{

            conn = util.getConnerction();

            String sql = "select Ename from catalog where catalogId = ? and isDelete = ?";

            ps = conn.prepareStatement(sql);
            //设置子节点id
            ps.setInt(1,catalogId);
            ps.setInt(2,CommonParam.NOT_DELETE);

            rs = ps.executeQuery();

            while (rs.next()){
                ename = rs.getString("Ename");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                util.closeConnerction(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
            return  ename;

        }


    /**
     * 获取给定字符的拼音首字母
     * @param str 给定字符
     * @return 4位首字母
     */
    public String getHead(String str){

        String convert = "";
        for (int j = 0; j < str.length(); j++)
        {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null)
            {
                convert += pinyinArray[0].charAt(0);
            } else
            {
                convert += word;
            }
        }
        //判断字符串的长度
        int count = convert.length();
        //当长度>4时，截取前4位，否则，返回字符串并大写
        return count>4?convert.substring(0,4).toUpperCase():convert.toUpperCase();

    }



}
