package daoImpl;

import bean.Case;
import bean.Page;
import common.CommonParam;
import common.JDBCUtil;
import dao.CaseDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 *
 * Created by sofronie on 2017/8/1.
 */
public class CaseDaoImpl extends HibernateDaoSupport implements CaseDao {

    /**
     * 获取分页信息
     * @param pageSize 每页记录数
     * @param page 当前页面
     * @return page对象
     */
    public Page getPage(int pageSize, int page,Case acase){
        //实例化对象
        Page pageBean = new Page();
        //总数据条数
        int allRows = getCaseCount(acase);
        //总页数
        int totalPages = pageBean.getTotalPages(pageSize,allRows);
        //当前页
        int currentpage = pageBean.getCurPage(page);
        //开始记录数
        int offset = pageBean.getCurrentPageOffset(pageSize,currentpage);

        //用例列表
        List<Case> list = getCaseList(acase,offset,pageSize);

        pageBean.setList(list);             //设置用例列表
        pageBean.setAllRows(allRows);       //设置所有用例数
        pageBean.setCurrentPage(currentpage);//设置当前页码
        pageBean.setTotalPage(totalPages);  //设置页码总数

        return pageBean;
    }
    /**
     * 通过目录ID，获取该目录下的所有用例数
     * @param acase 用例
     * @return 用例列表
     */
    public int getCaseCount(Case acase){

        int count = 0;

        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = util.getConnerction();

            StringBuffer sql = new StringBuffer();

            sql.append("SELECT count(*) from testcase LEFT JOIN script ON testcase.scriptId=script.scriptId where 1=1");

            if(acase.isSearch()){
                //如果查询的用例id不为空
                if(acase.getSearch_caseId() > 0){
                    sql.append(" and testcase.caseId like \'"+"%"+acase.getSearch_caseId()+"%\'");
                }
                //用例简述不为空
                if(acase.getSearch_caseTitle() != null && !acase.getSearch_caseTitle().equals("")){
                    sql.append(" and testcase.caseTitle like \'"+"%"+acase.getSearch_caseTitle()+"%\'");
                }
                //版本号不为空并且不等于“0”
                if(acase.getSearch_version()!= null && !acase.getSearch_version().equals("0")){
                    sql.append(" and testcase.caseVersion = \'"+acase.getSearch_version()+"\'");
                }
                //优先级不为空并且不等于“0”
                if(acase.getSearch_priority()!= null && !acase.getSearch_priority().equals("0")){
                    sql.append(" and testcase.priority = \'"+acase.getSearch_priority()+"\'");
                }
                //最后执行结果 不等于 0
                if(!(acase.getSearch_result() == 0)){
                    sql.append(" and testcase.lastResult = "+acase.getSearch_result());
                }
                //创建开始时间
                if(acase.getSearch_From() != null && acase.getSearch_To() != null){
                    //开始时间也结束时间都不为空时
                    sql.append(" and testcase.createTime between \'"+ acase.getSearch_From()+"\' and \'"+acase.getSearch_To()+"\'");
                }else if(acase.getSearch_From() != null && acase.getSearch_To() == null){
                    //开始时间不为空，结束时间为空时
                    sql.append(" and testcase.createTime > \'"+ acase.getSearch_From()+"\'");
                }else if(acase.getSearch_From() == null && acase.getSearch_To() != null){
                    //开始时间为空，结束时间不为空时
                    sql.append(" and testcase.createTime < \'"+ acase.getSearch_To()+"\'");
                }

                //设置节点id范围
                sql.append(" and testcase.catalogId in ("+acase.getSearch_ids()+") ");

            }else{
                sql.append(" and testCase.catalogId = "+acase.getCatalog_id());
            }

            sql.append(" and testcase.isDelete = "+CommonParam.NOT_DELETE);

            //查询用例表中对应节点下的用例数量。
            ps = conn.prepareStatement(sql.toString());

            //执行查询
            rs = ps.executeQuery();

            while (rs.next()){
                count = rs.getInt(1);
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
        return count;
    }

    /**
     * 查询用例列表
     * @param acase  用例
     * @param offset    开始index
     * @param pageSize 查询记录条数
     * @return 用例列表
     */
    public List<Case> getCaseList(Case acase,int offset,int pageSize){

        //定义
        List<Case> cases = null;
        //数据库操作相关定义
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();

            StringBuffer sql = new StringBuffer();

            sql.append("SELECT * from testcase LEFT JOIN script ON testcase.scriptId=script.scriptId where 1=1");

            if(acase.isSearch()){

                //如果查询的用例id不为空
                if(acase.getSearch_caseId() > 0){
                    sql.append(" and testcase.caseId like \'"+"%"+acase.getSearch_caseId()+"%\'");
                }
                //用例简述不为空
                if(acase.getSearch_caseTitle() != null && !acase.getSearch_caseTitle().equals("")){
                    sql.append(" and testcase.caseTitle like \'"+"%"+acase.getSearch_caseTitle()+"%\'");
                }
                //版本号不为空并且不等于“0”
                if(acase.getSearch_version()!= null && !acase.getSearch_version().equals("0")){
                    sql.append(" and testcase.caseVersion = \'"+acase.getSearch_version()+"\'");
                }
                //优先级不为空并且不等于“0”
                if(acase.getSearch_priority()!= null && !acase.getSearch_priority().equals("0")){
                    sql.append(" and testcase.priority = \'"+acase.getSearch_priority()+"\'");
                }
                //最后执行结果 不等于 0
                if(!(acase.getSearch_result() == 0)){
                    sql.append(" and testcase.lastResult = "+acase.getSearch_result());
                }
                //创建开始时间
                if(acase.getSearch_From() != null && acase.getSearch_To() != null){
                    //开始时间也结束时间都不为空时
                    sql.append(" and testcase.createTime between \'"+ acase.getSearch_From()+"\' and \'"+acase.getSearch_To()+"\'");
                }else if(acase.getSearch_From() != null && acase.getSearch_To() == null){
                    //开始时间不为空，结束时间为空时
                    sql.append(" and testcase.createTime > \'"+ acase.getSearch_From()+"\'");
                }else if(acase.getSearch_From() == null && acase.getSearch_To() != null){
                    //开始时间为空，结束时间不为空时
                    sql.append(" and testcase.createTime < \'"+ acase.getSearch_To()+"\'");
                }
                //设置节点id范围
                sql.append(" and testcase.catalogId in ("+acase.getSearch_ids()+") ");

            }else {
                //设置节点id
                sql.append(" and testCase.catalogId = "+acase.getCatalog_id());
            }

            sql.append(" and testcase.isDelete = "+CommonParam.NOT_DELETE+ " order by testcase.caseId asc  limit ?,?");

            ps = conn.prepareStatement(sql.toString());

            //开始记录数
            ps.setInt(1,offset);
            //每页显示记录数
            ps.setInt(2,pageSize);

            //查询结果
            rs = ps.executeQuery();

            cases = new ArrayList<Case>();
            while (rs.next()){
                //实例化对象
                Case caseBean = new Case();
                caseBean.setCase_id(rs.getInt("caseId"));              //用例id
                caseBean.setCatalog_id(rs.getInt("catalogId"));        //用例所属目录id
                caseBean.setScriptId(rs.getInt("scriptId"));           //自动化脚本id
                caseBean.setScriptName(rs.getString("scriptName"));    //自动化脚本名字
                caseBean.setCaseTitle(rs.getString("caseTitle"));      //用例名称、简述
                caseBean.setPrecondition(rs.getString("precondition"));//用例前置条件
                caseBean.setTestStep(rs.getString("testStep"));        //测试步骤
                caseBean.setExpectedRes(rs.getString("expectedRes"));  //预期结果
                caseBean.setCaseVersion(rs.getString("caseVersion"));  //用例版本号
                caseBean.setCaseDesc(rs.getString("caseDesc"));        //用例描述
                caseBean.setPriority(rs.getString("priority"));        //用例优先级
                caseBean.setLastResult(rs.getInt("lastResult"));       //用例最后一次的执行结果
                caseBean.setCreatorId(rs.getInt("creatorId"));         //用例创建者id
                caseBean.setCreatorName(rs.getString("creatorName"));   //用例创建者名字
                caseBean.setProject(rs.getString("project"));           //根节点的英文名
                caseBean.setIsDelete(rs.getInt("isDelete"));           //是否删除
                caseBean.setCreateTime(rs.getTimestamp("createTime")); //创建时间
                caseBean.setUpdateTime(rs.getTimestamp("updateTime")); //更新时间

                cases.add(caseBean);   //添加用例到list列表
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
        return cases;
    }

    /**
     * 通过用例ID，获取某一用例详情
     * @param caseId
     * @return
     */
    public Case getCaseById(int caseId) {
        //定义对象
        Case acase = null;
        //定义数据库连接相关对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            //sql语句
            String sql = "select * from testcase " +
                    "LEFT JOIN script " +
                    "on testcase.scriptId = script.scriptId " +
                    "where testcase.caseId = ? and testcase.isDelete = ?";
            //预编译
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setInt(1,caseId);                 //用例id
            ps.setInt(2, CommonParam.NOT_DELETE);   //是否删除
            //执行查询
            rs = ps.executeQuery();

            while (rs.next()){
                //实例化
                acase = new Case();
                //赋值
                acase.setCase_id(rs.getInt("caseId"));           //用例id
                acase.setCatalog_id(rs.getInt("catalogId"));        //用例所属目录id
                acase.setScriptId(rs.getInt("scriptId"));           //自动化脚本id
                acase.setCaseTitle(rs.getString("caseTitle"));      //用例名称、简述
                acase.setPrecondition(rs.getString("precondition"));//用例前置条件
                acase.setTestStep(rs.getString("testStep"));        //测试步骤
                acase.setExpectedRes(rs.getString("expectedRes"));  //预期结果
                acase.setCaseVersion(rs.getString("caseVersion"));  //用例版本号
                acase.setCaseDesc(rs.getString("caseDesc"));        //用例描述
                acase.setPriority(rs.getString("priority"));        //用例优先级
                acase.setLastResult(rs.getInt("lastResult"));       //用例最后一次的执行结果
                acase.setCreatorId(rs.getInt("creatorId"));         //用例创建者id
                acase.setCreatorName(rs.getString("creatorName"));  //用例创建者名称
                acase.setScriptName(rs.getString("scriptName"));    //自动化脚本名称
                acase.setProject(rs.getString("project"));          //根节点的英文名
                acase.setIsDelete(rs.getInt("isDelete"));           //是否删除
                acase.setCreateTime(rs.getTimestamp("createTime")); //创建时间
                acase.setUpdateTime(rs.getTimestamp("updateTime")); //更新时间
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
        return acase;
    }


    /**
     * 修改测试用例
     * @param acase 实例
     * @return 影响记录条数
     */
    public Case updateCase(Case acase){
        //定义返回对象
        Case caseBean = null;
        //定义影响记录条数
        int num = 0;
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            //开启事务
            util.beginTransaction(conn);
            //预编译sql
            String sql = "update testcase set scriptId = ?, caseTitle = ?,precondition = ?,testStep = ?,expectedRes = ?,caseVersion = ?,caseDesc = ?,priority = ?,updateTime = ? where caseId = ?";

            ps = conn.prepareStatement(sql);
            //传值
            if(acase.getScriptId() > 0){
                ps.setInt(1,acase.getScriptId());
            }else{
                ps.setNull(1,Types.INTEGER);
            }
            ps.setString(2,acase.getCaseTitle());   //用例简述
            ps.setString(3,acase.getPrecondition());//前置条件
            ps.setString(4,acase.getTestStep());    //测试步骤
            ps.setString(5,acase.getExpectedRes()); //预期结果
            ps.setString(6,acase.getCaseVersion()); //用例版本
            ps.setString(7,acase.getCaseDesc());    //用例描述
            ps.setString(8,acase.getPriority());    //用例优先级
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(9,timestamp);           //用例修改时间
            ps.setInt(10,acase.getCase_id());     //用例id
            //执行修改操作
            num = ps.executeUpdate();
            //若修改的记录条数大于0
            if(num > 0){
                //查询修改的用例信息
                String sql1 = "select * from testcase left JOIN  script on testcase.scriptId = script.scriptId where testcase.caseId = ? and testcase.isDelete = ?";
                //预编译
                ps1 = conn.prepareStatement(sql1);
                //赋值
                ps1.setInt(1,acase.getCase_id());
                ps1.setInt(2,CommonParam.NOT_DELETE);
                //查询
                rs = ps1.executeQuery();
                while (rs.next()){
                    caseBean = new Case();
                    //赋值
                    caseBean.setCase_id(rs.getInt("caseId"));           //用例id
                    caseBean.setCatalog_id(rs.getInt("catalogId"));        //用例所属目录id
                    caseBean.setScriptId(rs.getInt("scriptId"));            //自动化脚本id
                    caseBean.setCaseTitle(rs.getString("caseTitle"));      //用例名称、简述
                    caseBean.setPrecondition(rs.getString("precondition"));//用例前置条件
                    caseBean.setTestStep(rs.getString("testStep"));        //测试步骤
                    caseBean.setExpectedRes(rs.getString("expectedRes"));  //预期结果
                    caseBean.setCaseVersion(rs.getString("caseVersion"));  //用例版本号
                    caseBean.setCaseDesc(rs.getString("caseDesc"));        //用例描述
                    caseBean.setPriority(rs.getString("priority"));        //用例优先级
                    caseBean.setLastResult(rs.getInt("lastResult"));       //用例最后一次的执行结果
                    caseBean.setCreatorId(rs.getInt("creatorId"));         //用例创建者id
                    caseBean.setCreatorName(rs.getString("creatorName"));  //用例创建者名称
                    caseBean.setScriptName(rs.getString("scriptName"));    //自动化脚本名称
                    caseBean.setIsDelete(rs.getInt("isDelete"));           //是否删除
                    caseBean.setCreateTime(rs.getTimestamp("createTime")); //创建时间
                    caseBean.setUpdateTime(rs.getTimestamp("updateTime")); //更新时间
                }
            }
            //提交事务
            util.commitTransaction(conn);

        }catch (SQLException e){
            try {
                //回滚事务
                util.rollbackTransaction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            try {
                //关闭数据库连接
                if(rs != null){
                    rs.close();
                }
                if(ps1 != null){
                    ps1.close();
                }
                if(ps != null){
                    ps.close();
                }
                util.closeConnerction(conn);

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回实例
        return caseBean;
    }



    /**
     * 删除测试用例
     * @param caseId 用例id
     * @return 影响记录条数
     */
    public int deleteCase(int caseId){
        //定义
        int num = 0;
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取连接
            conn = util.getConnerction();
            //删除sql
            String sql = "update testcase set isDelete = ?,updateTime = ? where caseId = ?";
            //预编译
            ps = conn.prepareStatement(sql);
            //赋值
            ps.setInt(1,CommonParam.DELETED);       //删除状态
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(2,timestamp);           //修改时间
            ps.setInt(3,caseId);                 //用例id
            //执行删除操作
            num = ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //关闭连接
            try {
                ps.close();
                util.closeConnerction(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //返回影响记录条数
        return num;
    }


    /**
     * 通过导出的用例ID，获取要导出的用例列表
     * @return
     */
    public List<Case> getExportCaseList(Case acase){
        //定义接收对象
        List<Case> cases = new ArrayList<Case>();
        Case caseBean = null;
        //定义数据库相关操作
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();

            StringBuffer sql = new StringBuffer();

            sql.append("SELECT * from testcase LEFT JOIN script ON testcase.scriptId=script.scriptId where 1=1");

                //如果查询的用例id不为空
                if(acase.getSearch_caseId() > 0){
                    sql.append(" and testcase.caseId like \'"+"%"+acase.getSearch_caseId()+"%\'");
                }
                //用例简述不为空
                if(acase.getSearch_caseTitle() != null && !acase.getSearch_caseTitle().equals("")){
                    sql.append(" and testcase.caseTitle like \'"+"%"+acase.getSearch_caseTitle()+"%\'");
                }
                //版本号不为空并且不等于“0”
                if(acase.getSearch_version()!= null && !acase.getSearch_version().equals("0")){
                    sql.append(" and testcase.caseVersion = \'"+acase.getSearch_version()+"\'");
                }
                //优先级不为空并且不等于“0”
                if(acase.getSearch_priority()!= null && !acase.getSearch_priority().equals("0")){
                    sql.append(" and testcase.priority = \'"+acase.getSearch_priority()+"\'");
                }
                //最后执行结果 不等于 0
                if(!(acase.getSearch_result() == 0)){
                    sql.append(" and testcase.lastResult = "+acase.getSearch_result());
                }
                //创建开始时间
                if(acase.getSearch_From() != null && acase.getSearch_To() != null){
                    //开始时间也结束时间都不为空时
                    sql.append(" and testcase.createTime between \'"+ acase.getSearch_From()+"\' and \'"+acase.getSearch_To()+"\'");
                }else if(acase.getSearch_From() != null && acase.getSearch_To() == null){
                    //开始时间不为空，结束时间为空时
                    sql.append(" and testcase.createTime > \'"+ acase.getSearch_From()+"\'");
                }else if(acase.getSearch_From() == null && acase.getSearch_To() != null){
                    //开始时间为空，结束时间不为空时
                    sql.append(" and testcase.createTime < \'"+ acase.getSearch_To()+"\'");
                }
                //设置节点id范围
                sql.append(" and testcase.catalogId in ("+acase.getSearch_ids()+") ");
                //
                sql.append(" and testcase.isDelete = "+CommonParam.NOT_DELETE+ " order by testcase.caseId asc");

                ps = conn.prepareStatement(sql.toString());

                rs = ps.executeQuery();

                while (rs.next()){
                    caseBean = new Case();
                //赋值
                    caseBean.setCase_id(rs.getInt("caseId"));              //用例id
                    caseBean.setCatalog_id(rs.getInt("catalogId"));        //用例所属目录id
                    caseBean.setScriptId(rs.getInt("scriptId"));           //自动化脚本id
                    caseBean.setCaseTitle(rs.getString("caseTitle"));      //用例名称、简述
                    caseBean.setPrecondition(rs.getString("precondition"));//用例前置条件
                    caseBean.setTestStep(rs.getString("testStep"));        //测试步骤
                    caseBean.setExpectedRes(rs.getString("expectedRes"));  //预期结果
                    caseBean.setCaseVersion(rs.getString("caseVersion"));  //用例版本号
                    caseBean.setCaseDesc(rs.getString("caseDesc"));        //用例描述
                    caseBean.setPriority(rs.getString("priority"));        //用例优先级
                    caseBean.setLastResult(rs.getInt("lastResult"));       //用例最后一次的执行结果
                    caseBean.setCreatorId(rs.getInt("creatorID"));         //用例创建者id
                    caseBean.setCreatorName(rs.getString("creatorName"));  //用例创建者名称
                    caseBean.setIsDelete(rs.getInt("isDelete"));           //是否删除
                    caseBean.setCreateTime(rs.getTimestamp("createTime")); //创建时间
                    caseBean.setUpdateTime(rs.getTimestamp("updateTime")); //更新时间
                //添加到list列表
                cases.add(caseBean);
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
        //返回列表
        return cases;
    }


    /**
     * 添加用例到数据库
     * @param acase 用例列表
     * @return int
     */
    public int addCase(Case acase){
        //定义影响记录数
        int num = 0;
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //创建连接
            conn = util.getConnerction();
            //开启事务
            util.beginTransaction(conn);


                    //定义sql
                    String sql = "insert into testcase(catalogId,scriptId,caseTitle,precondition,testStep,expectedRes,caseVersion,caseDesc,priority,creatorId,creatorName,lastResult,project,isDelete,createTime,updateTime) " +
                            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    ps = conn.prepareStatement(sql);
                    //赋值
                    ps.setInt(1,acase.getCatalog_id());
                    ps.setNull(2,Types.INTEGER);
                    ps.setString(3,acase.getCaseTitle());
                    ps.setString(4,acase.getPrecondition());
                    ps.setString(5,acase.getTestStep());
                    ps.setString(6,acase.getExpectedRes());
                    ps.setString(7,acase.getCaseVersion());
                    ps.setString(8,acase.getCaseDesc());
                    ps.setString(9,acase.getPriority());
                    ps.setInt(10,acase.getCreatorId());
                    ps.setString(11,acase.getCreatorName());
                    ps.setInt(12,acase.getLastResult());
                    ps.setString(13,acase.getProject());
                    ps.setInt(14,acase.getIsDelete());
                    ps.setTimestamp(15,acase.getCreateTime());
                    ps.setTimestamp(16,acase.getUpdateTime());
                    //执行sql
                    num =  ps.executeUpdate();
            //提交事务
            util.commitTransaction(conn);
        }catch (SQLException e){
            try {
                //事务回滚
                util.rollbackTransaction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
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
     * 添加用例列表
     * @param cases 用例列表
     * @return 执行数量
     */
    public int addCaseList(List<Case> cases){
        int num = 0;
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //创建连接
            conn = util.getConnerction();
            //开启事务
            util.beginTransaction(conn);

            //定义sql
            String sql = "insert into testcase(catalogId,scriptId,caseTitle,precondition,testStep,expectedRes,caseVersion,caseDesc,priority,creatorId,creatorName,lastResult,project,isDelete,createTime,updateTime) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            for(Case acase:cases){
                //赋值
                ps.setInt(1,acase.getCatalog_id());     //节点id
                ps.setNull(2,Types.INTEGER);            //脚本id
                ps.setString(3,acase.getCaseTitle());   //用例简述
                ps.setString(4,acase.getPrecondition());//预置条件
                ps.setString(5,acase.getTestStep());    //测试步骤
                ps.setString(6,acase.getExpectedRes()); //预期结果
                ps.setString(7,acase.getCaseVersion()); //用例版本
                ps.setString(8,acase.getCaseDesc());    //用例描述
                ps.setString(9,acase.getPriority());    //用例优先级
                ps.setInt(10,acase.getCreatorId());     //用例创建者id
                ps.setString(11,acase.getCreatorName());//用例创建者名字
                ps.setInt(12,acase.getLastResult());    //最后执行结果
                ps.setString(13,acase.getProject());
                ps.setInt(14,acase.getIsDelete());      //是否删除
                ps.setTimestamp(15,acase.getCreateTime());//创建时间
                ps.setTimestamp(16,acase.getUpdateTime());//修改时间
                //执行sql
                num =  ps.executeUpdate();
            }
            //提交事务
            util.commitTransaction(conn);
        }catch (SQLException e){
            try {
                //事务回滚
                util.rollbackTransaction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }finally {
            try {
                if(ps!= null){
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
     * 获取脚本列表
     * @param userRole 用户角色
     * @param userId 用户id
     * @return 脚本列表
     */
    public Map<Integer,String> getScriptList(int userRole, int userId){
        //定义返回值
        Map<Integer,String> scriptList = new HashMap<Integer, String>();

        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();

        try {
            conn = util.getConnerction();

            if(userRole == 1){

                sql.append("select scriptId,scriptName from script where isDelete = ?");
                ps = conn.prepareStatement(sql.toString());
                ps.setInt(1, CommonParam.NOT_DELETE);

            }else{
                sql.append("select scriptId,scriptName from script where creatorId = ? and isDelete = ?");
                ps = conn.prepareStatement(sql.toString());
                ps.setInt(1,userId);
                ps.setInt(2,CommonParam.NOT_DELETE);
            }

            rs = ps.executeQuery();

            while (rs.next()){
                scriptList.put(rs.getInt("scriptId"),rs.getString("ScriptName"));
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
     * 查询所有用例包含的优先级列表
     * @return 优先级列表
     */
    public List<String> getCasePriority(){
        //定义返回对象
        List<String> priorityList = null;
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            String sql = "SELECT distinct priority FROM testcase where isDelete = ?";
            //预编译对象
            ps = conn.prepareStatement(sql);
            ps.setInt(1,CommonParam.NOT_DELETE);
            //执行查询并保存结果
            rs = ps.executeQuery();
            priorityList = new ArrayList<String>();

            while (rs.next()){
                //获取对象
                String priority = rs.getString("priority");
                if(priority.equals("") || priority == null){
                    continue;
                }
                //添加到list
                priorityList.add(priority);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            //关闭连接
            try {
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return priorityList;
    }



    /**
     * 获取用例包含的版本号列表
     * @return 版本号列表
     */
    public List<String> getVersionList(){
        //接收对象定义
        List<String> versionList = null;
        //数据库对象定义
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            //获取连接
            conn = util.getConnerction();
            //执行sql
            String sql = "SELECT distinct caseVersion FROM testcase where isDelete = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,CommonParam.NOT_DELETE);
            //执行
            rs = ps.executeQuery();
            //实例化
            versionList = new ArrayList<String>();
            while (rs.next()){
                //定义String
                String version = rs.getString("caseVersion");
                if(version.equals("") || version == null){
                    continue;
                }
                //添加到list
                versionList.add(version);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                //关闭连接
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回list
        return versionList;
    }







}
