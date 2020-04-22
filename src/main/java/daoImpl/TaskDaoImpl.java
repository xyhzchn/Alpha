package daoImpl;

import bean.Case;
import bean.Run;
import bean.Task;
import bean.Tree;
import common.CommonParam;
import common.JDBCUtil;
import dao.TaskDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.Date;

/**
 * 任务持久化接口实现类
 * Created by sofronie on 2017/8/10.
 */
public class TaskDaoImpl extends HibernateDaoSupport implements TaskDao {

    /**
     * 获取管理员未开始任务列表
     * @return List<Task>
     */
    public List<Task> getAdminPreTasks(int userId,Task task){
        //定义接收对象
        List<Task> taskList = new ArrayList<Task>();
        //定义数据库持久化对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            //查询数据库中任务状态为0：创建未发布  1：发布未接收或拒绝  的任务列表。
            StringBuffer sql = new StringBuffer();
            //默认查询
            sql.append("select task.* ,user1.username as creatorName,user2.username as executorName from " +
                    "task ,users user1,users user2 " +
                    "where task.userId = user1.userId " +
                    "and task.executorId = user2.userId ");
            //当查询的状态值>0
            if(task.getSearch_status() > 0 && ((task.getSearch_status() == CommonParam.STATUS_NOT_RELEASE) || (task.getSearch_status() == CommonParam.STATUS_RELEASED))){
                sql.append(" and task.status = "+task.getSearch_status());
            }else {
                //否则查询未发布、已发布
                sql.append(" and task.status in ("+CommonParam.STATUS_NOT_RELEASE+","+CommonParam.STATUS_RELEASED+")");
            }
            //当查询的任务id不等于0
            if(task.getSearch_taskId() != 0){
                sql.append(" and task.taskId = "+task.getSearch_taskId());
            }
            //当查询的任务名称不等于0
            if(!(task.getSearch_name() == null) && !(task.getSearch_name().equals(""))){
                sql.append(" and task.name like \'%"+task.getSearch_name()+"%\'");
            }
            //最后增加用户id，删除状态和排列限制
            sql.append(" and  task.userId= "+userId+" and task.isDelete = "+CommonParam.NOT_DELETE+" order by task.updateTime desc");
            //预编译
            ps = conn.prepareStatement(sql.toString());
            //保存查询结果
            rs = ps.executeQuery();
            while (rs.next()){
                //实例化
                Task taskBean = new Task();

                taskBean.setTaskId(rs.getInt("taskId"));            //任务id
                taskBean.setUserId(rs.getInt("userId"));               //用户id
                taskBean.setTaskName(rs.getString("taskName"));         //任务名称
                taskBean.setTaskDesc(rs.getString("taskDesc"));     //任务描述
                taskBean.setCreatorName(rs.getString("creatorName"));//创建者名字
                taskBean.setExecutorId(rs.getInt("executorId"));        //执行者id
                taskBean.setExecutorName(rs.getString("executorName")); //执行者名字
                taskBean.setStatus(rs.getInt("status"));                //任务执行状态
                taskBean.setReason(rs.getString("reason"));             //拒绝理由
                taskBean.setIsDelete(rs.getInt("isDelete"));            //是否删除
                taskBean.setCreateTime(rs.getTimestamp("createTime"));  //创建时间
                taskBean.setUpdateTime(rs.getTimestamp("updateTime"));  //修改时间
                //添加到任务列表
                taskList.add(taskBean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return taskList;
    }

    /**
     * 获取管理员进行中任务列表
     * @return
     */
    public List<Task> getAdminRunTasks(int userId,Task task){
        //任务列表
        List<Task> taskList = new ArrayList<Task>();
        //数据库对象定义
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            //查询数据库中任务状态为0：创建未发布  1：发布未接收或拒绝  的任务列表。
            StringBuffer sql = new StringBuffer();
            //默认查询
            sql.append(
                    "select task.* ,user1.username as creatorName,user2.username as executorName from " +
                            "task ,users user1,users user2 " +
                            "where task.userId = user1.userId " +
                            "and task.executorId = user2.userId "
            );
            //当查询的状态值>0
            if(task.getSearch_status() > 0 && ((task.getSearch_status() == CommonParam.STATUS_NOT_START) || (task.getSearch_status() == CommonParam.STATUS_RUNNING))){
                sql.append(" and task.status = "+task.getSearch_status());
            }else {
                //否则查询未发布、已发布
                sql.append(" and task.status in ("+CommonParam.STATUS_NOT_START+","+CommonParam.STATUS_RUNNING+")");
            }
            //当查询的任务id不等于0
            if(task.getSearch_taskId() != 0){
                sql.append(" and task.taskId = "+task.getSearch_taskId());
            }
            //当查询的任务名称不等于0
            if(!(task.getSearch_name() == null) && !(task.getSearch_name().equals(""))){
                sql.append(" and task.name like \'%"+task.getSearch_name()+"%\'");
            }
            //最后增加用户id，删除状态和排列限制
            sql.append(" and task.userId = "+userId+" and task.isDelete = "+CommonParam.NOT_DELETE+" order by task.updateTime desc");

            ps = conn.prepareStatement(sql.toString());
            //执行数据库查询
            rs = ps.executeQuery();
            while (rs.next()){
                //实例化
                Task taskBean = new Task();
                taskBean.setTaskId(rs.getInt("taskId"));            //任务id
                taskBean.setUserId(rs.getInt("userId"));               //用户id
                taskBean.setTaskName(rs.getString("taskName"));         //任务名称
                taskBean.setTaskDesc(rs.getString("taskDesc"));     //任务描述
                taskBean.setCreatorName(rs.getString("creatorName"));//创建者名字
                taskBean.setExecutorId(rs.getInt("executorId"));        //执行者id
                taskBean.setExecutorName(rs.getString("executorName")); //执行者名字
                taskBean.setStatus(rs.getInt("status"));                //任务执行状态
                taskBean.setReason(rs.getString("reason"));             //拒绝理由
                taskBean.setIsDelete(rs.getInt("isDelete"));            //是否删除
                taskBean.setCreateTime(rs.getTimestamp("createTime"));  //创建时间
                taskBean.setUpdateTime(rs.getTimestamp("updateTime"));  //修改时间
                taskBean.setPrecent(getRunPrecent(taskBean.getTaskId()));
                //添加到list
                taskList.add(taskBean);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭数据库连接
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return taskList;
    }
    /**
     * 获取管理员已结束任务列表
     * @return
     */
    public List<Task> getAdminFinishTasks(int userId,Task task) {
        List<Task> taskList = new ArrayList<Task>();
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            //获取数据库连接
            conn = util.getConnerction();
            //查询数据库中任务状态为0：创建未发布  1：发布未接收或拒绝  的任务列表。
            StringBuffer sql = new StringBuffer();
            //默认查询
            sql.append(
                    "select task.* ,user1.username as creatorName,user2.username as executorName from " +
                            "task ,users user1,users user2 " +
                            "where task.userId = user1.userId " +
                            "and task.executorId = user2.userId "
            );
            //当查询的状态值>0
            if(task.getSearch_status() > 0 && ((task.getSearch_status() == CommonParam.STATUS_FINISHED) || (task.getSearch_status() == CommonParam.STATUS_REFLUSED))){
                sql.append(" and task.status = "+task.getSearch_status());
            }else {
                //否则查询未发布、已发布
                sql.append(" and task.status in ("+CommonParam.STATUS_FINISHED+","+CommonParam.STATUS_REFLUSED+")");
            }
            //当查询的任务id不等于0
            if(task.getSearch_taskId() != 0){
                sql.append(" and task.taskId = "+task.getSearch_taskId());
            }
            //当查询的任务名称不等于0
            if(!(task.getSearch_name() == null) && !(task.getSearch_name().equals(""))){
                sql.append(" and task.name like \'%"+task.getSearch_name()+"%\'");
            }
            //最后增加用户id，删除状态和排列限制
            sql.append(" and task.userId = "+userId+" and task.isDelete = "+CommonParam.NOT_DELETE+" order by task.updateTime desc");

            ps = conn.prepareStatement(sql.toString());
            //执行查询
            rs = ps.executeQuery();
            while (rs.next()) {
                //实例化
                Task taskBean = new Task();
                taskBean.setTaskId(rs.getInt("taskId"));            //任务id
                taskBean.setUserId(rs.getInt("userId"));               //用户id
                taskBean.setTaskName(rs.getString("taskName"));         //任务名称
                taskBean.setTaskDesc(rs.getString("taskDesc"));     //任务描述
                taskBean.setCreatorName(rs.getString("creatorName"));//创建者名字
                taskBean.setExecutorId(rs.getInt("executorId"));        //执行者id
                taskBean.setExecutorName(rs.getString("executorName")); //执行者名字
                taskBean.setStatus(rs.getInt("status"));                //任务执行状态
                taskBean.setReason(rs.getString("reason"));             //拒绝理由
                taskBean.setIsDelete(rs.getInt("isDelete"));            //是否删除
                taskBean.setCreateTime(rs.getTimestamp("createTime"));  //创建时间
                taskBean.setUpdateTime(rs.getTimestamp("updateTime"));  //修改时间
                //添加到list
                taskList.add(taskBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭数据库连接
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return taskList;
    }




    /**
     * 获取用例所属的创建者列表
     * @return Map<Integer,String>
     */
    public Map<Integer,String> getCreaterList(){
        //接收对象定义
        Map<Integer,String> createrMap = null;
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = util.getConnerction();
            String sql = "SELECT distinct creatorId,creatorName FROM testcase";
            ps = conn.prepareStatement(sql);
            //执行查询
            rs = ps.executeQuery();
            //实例化对象
            createrMap = new HashMap<Integer, String>();
            while (rs.next()){
                createrMap.put(rs.getInt("creatorId"),rs.getString("creatorName"));
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
        //返回结果
        return createrMap;
    }



    /**
     * 获取用例所属的目录列表
     * @return 目录列表
     */
    public List<Tree> getCatalogList(){
        //接收对象定义
        List<Tree> treeList = null;
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = util.getConnerction();
            String sql = "select * from catalog where isDelete = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,CommonParam.NOT_DELETE);
            //执行查询
            rs = ps.executeQuery();
            //实例化对象
            treeList = new ArrayList<Tree>();
            while (rs.next()){
                //实例化对象
                Tree tree = new Tree();
                tree.setCatalogId(rs.getInt("catalogId"));          //节点id
                tree.setCatalogName(rs.getString("catalogName"));   //节点名字
                tree.setParent_id(rs.getInt("parent_id"));          //父节点
                tree.setLevel(rs.getInt("level"));              //节点等级

                treeList.add(tree);

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
        //返回结果
        return treeList;
    }

    /**
     * 根据条件查询用例列表
     * @param acase 用例实例
     * @return 用例列表
     */
    public List<Case> searchCases(Case acase){
        //定义接收对象
        List<Case> cases = null;
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取连接
            conn = util.getConnerction();

            //拼接sql查询语句
            StringBuffer sb = new StringBuffer();
            //初始化查询语句
            sb.append("select * from testcase left join script on testcase.scriptId=script.scriptId where 1=1");

            //如果查询的用例id不为空
            if(acase.getSearch_caseId() > 0){
                sb.append(" and testcase.caseId like \'"+"%"+acase.getSearch_caseId()+"%\'");
            }
            //目录id不等于0
            if(acase.getSearch_catalogId() != 0){
                sb.append(" and testcase.catalogId in ("+acase.getSearch_ids()+") ");
            }
            //用例简述不为空
            if(acase.getSearch_caseTitle() != null && !acase.getSearch_caseTitle().equals("")){
                sb.append(" and testcase.caseTitle like \'"+"%"+acase.getSearch_caseTitle()+"%\'");
            }
            //版本号不为空并且不等于“0”
            if(acase.getSearch_version()!= null && !acase.getSearch_version().equals("0")){
                sb.append(" and testcase.caseVersion = \'"+acase.getSearch_version()+"\'");
            }
            //优先级不为空并且不等于“0”
            if(acase.getSearch_priority()!= null && !acase.getSearch_priority().equals("0")){
                sb.append(" and testcase.priority = \'"+acase.getSearch_priority()+"\'");
            }
            //最后执行结果 不等于 0
            if(!(acase.getSearch_result() == 0)){
                sb.append(" and testcase.lastResult = "+acase.getSearch_result());
            }
            //用例创建者id 不等于 0
            if(!(acase.getSearch_creator() == 0)){
                sb.append(" and testcase.creatorId = "+acase.getSearch_creator());
            }
            //创建开始时间
            if(acase.getSearch_From() != null && acase.getSearch_To() != null){
                //开始时间也结束时间都不为空时
                sb.append(" and testcase.createTime between \'"+ acase.getSearch_From()+"\' and \'"+acase.getSearch_To()+"\'");
            }else if(acase.getSearch_From() != null && acase.getSearch_To() == null){
                //开始时间不为空，结束时间为空时
                sb.append(" and testcase.createTime > \'"+ acase.getSearch_From()+"\'");
            }else if(acase.getSearch_From() == null && acase.getSearch_To() != null){
                //开始时间为空，结束时间不为空时
                sb.append(" and testcase.createTime < \'"+ acase.getSearch_To()+"\'");
            }


            sb.append(" and testcase.isDelete = "+CommonParam.NOT_DELETE);

            ps = conn.prepareStatement(sb.toString());
            //执行查询
            rs = ps.executeQuery();
            //实例化对象
            cases = new ArrayList<Case>();
            while (rs.next()){
                //实例化用例对象
                Case caseBean = new Case();
                //赋值
                caseBean.setCase_id(rs.getInt("caseId"));              //用例id
                caseBean.setCatalog_id(rs.getInt("catalogId"));        //用例所属目录id
                caseBean.setScriptId(rs.getInt("scriptId"));           //自动化脚本id
                caseBean.setScriptName(rs.getString("scriptName"));    //自动化脚本名字
                caseBean.setCaseTitle(rs.getString("caseTitle"));      //用例简述
                caseBean.setPrecondition(rs.getString("precondition"));//预置条件
                caseBean.setTestStep(rs.getString("testStep"));        //测试步骤
                caseBean.setExpectedRes(rs.getString("expectedRes"));  //预期结果
                caseBean.setCaseVersion(rs.getString("caseVersion"));  //版本号
                caseBean.setCaseDesc(rs.getString("caseDesc"));        //用例描述
                caseBean.setPriority(rs.getString("priority"));        //优先级
                caseBean.setLastResult(rs.getInt("lastResult"));       //最后的执行结果
                caseBean.setCreatorId(rs.getInt("creatorId"));         //拥有者id
                caseBean.setCreatorName(rs.getString("creatorName"));  //拥有者名字
                caseBean.setProject(rs.getString("project"));          //根节点英文名
                caseBean.setIsDelete(rs.getInt("isDelete"));           //是否删除
                caseBean.setCreateTime(rs.getTimestamp("createTime")); //创建时间
                caseBean.setUpdateTime(rs.getTimestamp("updateTime")); //修改时间

                //添加到用例列表
                cases.add(caseBean);
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
        //返回结果
        return cases;
    }


    /**
     * 获取执行者用户列表
     * @return 执行者用户列表
     */
    public Map<Integer, String> getExecutorList(){
        //定义对象
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
     * 创建任务，保存数据到数据库
     * @param task 任务信息
     * @param ids 用户选择的用例id列表
     * @return 执行状态
     */
    public int addTask(Task task, List<String> ids){
        //定义执行状态
        int num = 0;
        //定义数据库连接相关对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        //添加到任务表中的预编译对象
        PreparedStatement ps = null;
        //添加到任务用例关联表中的预编译对象
        PreparedStatement ps1 = null;
        //查询刚刚添加到任务表中的任务id.
        ResultSet rs = null;

        try {
            //创建数据库连接
            conn =  util.getConnerction();
            //开启事务
            util.beginTransaction(conn);
            //添加到任务表中一条记录的sql
            String sql = "insert into task(userId,taskName,taskDesc,executorId,status,reason,isDelete,createTime,updateTime) values(?,?,?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            //赋值
            ps.setInt(1,task.getUserId());              //用户id
            ps.setString(2,task.getTaskName());         //任务名称
            ps.setString(3,task.getTaskDesc());         //任务描述
            ps.setInt(4,task.getExecutorId());          //任务执行者id
            ps.setInt(5,CommonParam.STATUS_NOT_RELEASE);//任务状态：未发布
            ps.setString(6,task.getReason());           //任务拒绝原因：暂无
            ps.setInt(7,CommonParam.NOT_DELETE);       //任务是否删除
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(8,timestamp);              //获取当前服务器时间
            ps.setTimestamp(9,timestamp);              //设置任务修改时间

            //执行持久化操作
            num = ps.executeUpdate();

            //获取刚刚添加的任务
            rs = ps.getGeneratedKeys();

            int task_id = 0;
            while (rs.next()){
                //任务id赋值
                task_id = rs.getInt(1);
                //设置添加到关联表的sql
                String linkSql  = "insert into task_case(taskId,caseId,isRun,lastResult,createTime,updateTime)value(?,?,?,?,?,?)";
                ps1 = conn.prepareStatement(linkSql);
                //循环赋值
                for(int i=0;i<ids.size();i++){
                    ps1.setInt(1,task_id);              //相同的任务id
                    ps1.setInt(2,Integer.parseInt(ids.get(i)));        //不同的用例id
                    ps1.setInt(3,CommonParam.NOT_RUN);  //用例是否已运行
                    ps1.setInt(4,0);                 //任务中的执行结果默认为0
                    ps1.setTimestamp(5,timestamp);      //添加创建时间
                    ps1.setTimestamp(6,timestamp);      //添加修改时间
                    //持久化操作
                    num = ps1.executeUpdate();
                }
            }
            //提交事务
            util.commitTransaction(conn);
        }catch (SQLException e){
            try {
                //如果执行失败，则回滚事务
                util.rollbackTransaction(conn);
            }catch ( SQLException e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            try {
                if(ps1 != null){
                    ps1.close();
                }
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
        //返回执行结果
        return num;
    }

    /**
     * 通过任务id获取任务的详情信息
     * @param taskId 任务id
     * @return 任务实例
     */
    public Task getTaskById(int taskId,boolean isContentLog){
        //定义任务实例
        Task task = new Task();
        //定义任务包含的用例列表
        List<Case> cases = new ArrayList<Case>();
        //数据库相关对象定义
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        //任务相关
        PreparedStatement ps = null;
        ResultSet rs = null;
        //用例log相关
        PreparedStatement logPs = null;
        ResultSet logRs = null;

        try{
            //获取连接
            conn = util.getConnerction();
            //查询任务-用例数据
            String sql =
                    "select *,u1.username as taskCreatorName,u2.username as executorName from task_case as b \n" +
                            "inner join task as a on b.taskId = a.taskId\n" +
                            "inner join testcase as c on b.caseId = c.caseId\n" +
                            "INNER JOIN users as u1 on a.userId = u1.userId\n" +
                            "INNER JOIN users as u2 on a.executorId = u2.userId\n" +
                            "LEFT JOIN script as s on c.scriptId = s.scriptId\n"+
                            "where a.taskId = ? and a.isDelete = ?";

            //查询任务信息
            ps = conn.prepareStatement(sql);
            ps.setInt(1,taskId);                //任务id
            ps.setInt(2,CommonParam.NOT_DELETE);//任务是否删除
            //执行任务查询
            rs = ps.executeQuery();

            while (rs.next()) {
                //设置任务信息
                task.setTaskId(rs.getInt("taskId"));                //任务id
                task.setUserId(rs.getInt("userId"));                   //任务所属用户id
                task.setTaskName(rs.getString("taskName"));             //任务名称
                task.setTaskDesc(rs.getString("taskDesc"));         //任务描述信息
                task.setCreatorName(rs.getString("taskCreatorName"));   //任务创建者名字
                task.setExecutorId(rs.getInt("executorId"));        //任务执行者id
                task.setExecutorName(rs.getString("executorName")); //任务执行者名字
                task.setStatus(rs.getInt("status"));                //任务当前状态
                task.setReason(rs.getString("reason"));             //任务拒绝理由
                task.setIsDelete(rs.getInt("isDelete"));            //任务是否删除
                task.setCreateTime(rs.getTimestamp("createTime"));  //任务创建时间
                task.setUpdateTime(rs.getTimestamp("updateTime"));  //任务修改时间
                //定义用例对象
                Case acase = new Case();
                //给用例列表赋值
                acase.setCase_id(rs.getInt("caseId"));              //用例id
                acase.setCatalog_id(rs.getInt("catalogId"));        //用例所属目录id
                acase.setScriptId(rs.getInt("scriptId"));           //自动化脚本id
                acase.setScriptName(rs.getString("scriptName"));    //自动化脚本名字
                acase.setCaseTitle(rs.getString("caseTitle"));      //用例简述
                acase.setPrecondition(rs.getString("precondition"));//预置条件
                acase.setTestStep(rs.getString("testStep"));        //测试步骤
                acase.setExpectedRes(rs.getString("expectedRes"));  //预期结果
                acase.setCaseVersion(rs.getString("caseVersion"));  //用例版本
                acase.setCaseDesc(rs.getString("caseDesc"));        //用例描述
                acase.setPriority(rs.getString("priority"));        //用例优先级
                acase.setLastResult(rs.getInt("lastResult"));       //用例最新的执行结果
                acase.setCreatorId(rs.getInt("creatorId"));         //用例所有者id
                acase.setCreatorName(rs.getString("creatorName"));  //用例所有者名字
                acase.setProject(rs.getString("project"));          //根节点英文名
                acase.setIsDelete(rs.getInt("isDelete"));           //用例是否删除
                acase.setCreateTime(rs.getTimestamp("createTime")); //用例创建时间
                acase.setUpdateTime(rs.getTimestamp("updateTime")); //用例修改时间

                //如果需要查询每条用例的执行log
                if(isContentLog){
                    String logSql = "select * from caserun,users where caserun.staffId = users.userId and caserun.caseId = ? ORDER by caserun.createTime desc";

                    //获取某一条用例的log列表
                    logPs = conn.prepareStatement(logSql);
                    //根据用例id查询每条用例的执行log
                    logPs.setInt(1,acase.getCase_id());
                    //执行查询并保存结果
                    logRs = logPs.executeQuery();
                    //定义接收list
                    List<String> logs = new ArrayList<String>();

                    while (logRs.next()){
                        //定义log对象
                        Run run = new Run();
                        run.setStaffName(logRs.getString("username"));     //执行用例者的名字
                        run.setCreateTime(logRs.getTimestamp("createTime"));//log执行时间
                        run.setResult(logRs.getInt("result"));              //执行结果
                        run.setStaffWay(logRs.getInt("staffWay"));
                        String resultStr = "";
                        switch (run.getResult()){
                            case 1:resultStr = "SUCCESS"; break;
                            case 2:resultStr = "FAILURE"; break;
                            case 3:resultStr = "BLOCK"; break;
                        }
                        //拼接log信息
                        StringBuffer sb = new StringBuffer();
                        sb.append(run.getStaffName());
                        sb.append("在");
                        sb.append(run.getCreateTime());
                        sb.append("执行了该测试用例,执行结果是[ ");
                        sb.append(resultStr+" ]");
                        String staffWayStr = "";
                        if (run.getStaffWay() == CommonParam.BY_HAND){
                            staffWayStr = "手工执行";
                        }else{
                            staffWayStr = "自动化测试脚本执行";
                        }
                        sb.append("执行方式为："+staffWayStr);
                        String log = sb.toString();
                        //每一条用例的loglist
                        logs.add(log);
                    }
                    //设置执行的用例log信息
                    acase.setRunLogs(logs);
                    //添加用例列表
                    cases.add(acase);
                }else{
                    //添加用例列表
                    cases.add(acase);
                }
            }
            //任务添加用例列表
            task.setCaselist(cases);

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                //关闭相关连接
                if(isContentLog){
                    logPs.close();
                    if(!(logRs.wasNull())){
                        logRs.close();
                    }
                }
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回任务信息
        return task;
    }

    /**
     * 发布任务
     * @param taskId 任务id
     * @return 执行状态
     */
    public int releaseTask(int taskId){
        //影响的数据条数
        int num = 0;
        //数据库连接相关对象定义
        JDBCUtil util = new JDBCUtil();

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取连接
            conn = util.getConnerction();
            //定义sql
            String sql = "update task set status = ?,updateTime = ? where taskID = ? and isDelete = ?";
            ps = conn.prepareStatement(sql);
            //设置任务的状态为已发布：1
            ps.setInt(1,CommonParam.STATUS_RELEASED);
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            //设置修改任务状态时间
            ps.setTimestamp(2,timestamp);
            //设置任务ID
            ps.setInt(3,taskId);
            //设置删除状态
            ps.setInt(4,CommonParam.NOT_DELETE);
            //执行sql
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
        //返回影响数据条数
        return num;
    }


    /**
     * 修改任务
     * @param task 任务信息
     * @param ids 任务包含的用例id
     * @return 执行状态
     */
    public int editTask(Task task,List<String> ids){
        int num = 0;
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement selectSql = null;
        PreparedStatement updateSql_1 = null;
        PreparedStatement deleteSql = null;
        PreparedStatement updateSql_2 = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            //开启事务
            util.beginTransaction(conn);

            //查询数据库中对应的任务不为空
            String searchSql = "select count(*) from task as a inner join task_case as b on a.taskId = b.taskId where a.taskId = ? and a.isDelete = ?";
            selectSql = conn.prepareStatement(searchSql);
            //赋值
            selectSql.setInt(1,task.getTaskId());      //任务id
            selectSql.setInt(2,CommonParam.NOT_DELETE);//任务未被手动删除
            rs = selectSql.executeQuery();
            while (rs.next()){
                //获取记录条数
                int count = rs.getInt(1);
                //数据库中有对应的数据
                if(count >0){
                    //第一步， 修改任务表中的相关字段
                    String sql1 = "update task set taskName= ?,taskDesc = ?,executorId = ?,updateTime = ? where taskId = ?";
                    updateSql_1 = conn.prepareStatement(sql1);
                    //赋值
                    updateSql_1.setString(1,task.getTaskName());    //任务名称
                    updateSql_1.setString(2,task.getTaskDesc());    //任务描述
                    updateSql_1.setInt(3,task.getExecutorId());     //任务执行者id
                    Date date =  new Date();
                    Timestamp timestamp = new Timestamp(date.getTime());
                    updateSql_1.setTimestamp(4,timestamp);          //任务修改时间
                    updateSql_1.setInt(5,task.getTaskId());         //任务id
                    //执行针对任务表的修改
                    num = updateSql_1.executeUpdate();
                    //第二步， 删除关联表中任务id对应的所有用例
                    String deleteStr = "delete from task_case where taskId = ?";
                    deleteSql = conn.prepareStatement(deleteStr);
                    deleteSql.setInt(1,task.getTaskId());   //任务id
                    //执行删除操作
                    num = deleteSql.executeUpdate();

                    //第三步，重新添加任务对应的用例列表
                    //设置添加到关联表的sql
                    String linkSql  = "insert into task_case(taskId,caseId,isRun,lastResult,createTime,updateTime) values(?,?,?,?,?,?)";
                    updateSql_2 = conn.prepareStatement(linkSql);
                    //循环赋值
                    for(int i=0;i<ids.size();i++){
                        updateSql_2.setInt(1,task.getTaskId());     //任务id
                        updateSql_2.setString(2,ids.get(i));        //不同的用例id
                        updateSql_2.setInt(3,CommonParam.NOT_RUN);  //用例是否已运行
                        updateSql_2.setInt(4,0);                 //设置任务默认
                        updateSql_2.setTimestamp(5,timestamp);      //添加创建时间
                        updateSql_2.setTimestamp(6,timestamp);      //添加修改时间
                        //持久化操作
                        num = updateSql_2.executeUpdate();
                    }
                }
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
            e.printStackTrace();
        }finally {
            try {
                //关闭数据库连接
                updateSql_2.close();
                deleteSql.close();
                updateSql_1.close();
                rs.close();
                selectSql.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
        return num;
    }
    /**
     * 获取测试人员未开始任务列表
     * @return 未开始任务列表
     */
    public List<Task> getTesterPreTasks(int userId,Task task){
        //定义接收对象
        List<Task> taskList = new ArrayList<Task>();
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            //获取数据库连接
            conn = util.getConnerction();
            StringBuffer sb = new StringBuffer();
            sb.append(
                    "select task.* ,user1.username as creatorName,user2.username as executorName from " +
                            "task ,users user1,users user2 " +
                            "where task.userId = user1.userId " +
                            "and task.executorId = user2.userId");

            //当查询的任务id不等于0
            if(task.getSearch_taskId() != 0){
                sb.append(" and task.taskId = "+task.getSearch_taskId());
            }
            //当查询的任务名称不等于0
            if(!(task.getSearch_name() == null) && !(task.getSearch_name().equals(""))){
                sb.append(" and task.taskName like \'%"+task.getSearch_name()+"%\'");
            }

            sb.append(" and task.executorId = "+userId);                     //执行者id
            sb.append(" and task.status = "+CommonParam.STATUS_RELEASED);    //任务状态
            sb.append(" and task.isDelete = "+CommonParam.NOT_DELETE);       //删除状态
            sb.append(" order by task.updateTime desc");                     //排序
            //预编译
            ps = conn.prepareStatement(sb.toString());
            //执行查询
            rs = ps.executeQuery();

            while (rs.next()){
                //实例化对象
                Task taskBean = new Task();

                taskBean.setTaskId(rs.getInt("taskId"));                //任务id
                taskBean.setUserId(rs.getInt("userId"));                   //所属用户id
                taskBean.setTaskName(rs.getString("taskName"));             //任务名称
                taskBean.setTaskDesc(rs.getString("taskDesc"));         //任务描述
                taskBean.setCreatorName(rs.getString("creatorName"));   //创建者名字
                taskBean.setExecutorId(rs.getInt("executorId"));        //执行者id
                taskBean.setExecutorName(rs.getString("executorName")); //执行者名字
                taskBean.setStatus(rs.getInt("status"));                //任务的状态
                taskBean.setReason(rs.getString("reason"));             //任务拒绝理由
                taskBean.setIsDelete(rs.getInt("isDelete"));            //任务删除状态
                taskBean.setCreateTime(rs.getTimestamp("createTime"));  //创建时间
                taskBean.setUpdateTime(rs.getTimestamp("updateTime"));  //修改时间
                //添加到list
                taskList.add(taskBean);

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
        //返回列表
        return taskList;
    }

    /**
     * 获取测试人员进行中任务列表
     * @param userId 用户id
     * @return  List<Task>
     */
    public List<Task> getTesterRunTasks(int userId,Task task){
        List<Task> taskList = new ArrayList<Task>();
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = util.getConnerction();
            //定义执行sql
            StringBuffer sql = new StringBuffer();
            //默认查询
            sql.append(
                    "select task.* ,user1.username as creatorName,user2.username as executorName from " +
                            "task ,users user1,users user2 " +
                            "where task.userId = user1.userId " +
                            "and task.executorId = user2.userId"
            );

            //当查询的任务id不等于0
            if(task.getSearch_taskId() != 0){
                sql.append(" and task.taskId = "+task.getSearch_taskId());
            }
            //当查询的任务名称不等于空
            if(!(task.getSearch_name() == null) && !(task.getSearch_name().equals(""))){
                sql.append(" and task.taskName like \'%"+task.getSearch_name()+"%\'");
            }

            //当查询的状态值>0
            if(task.getSearch_status() > 0 && ((task.getSearch_status() == CommonParam.STATUS_NOT_START) || (task.getSearch_status() == CommonParam.STATUS_RUNNING))){
                sql.append(" and task.status = "+task.getSearch_status());
            }else {
                //否则查询未发布、已发布
                sql.append(" and task.status in ("+CommonParam.STATUS_NOT_START+","+CommonParam.STATUS_RUNNING+")");
            }

            sql.append(" and task.executorId = "+userId);                     //执行者id
            sql.append(" and task.isDelete = "+CommonParam.NOT_DELETE);       //删除状态
            sql.append(" order by task.updateTime desc");                     //排序
            //预编译
            ps = conn.prepareStatement(sql.toString());
            //执行查询
            rs = ps.executeQuery();

            while (rs.next()){
                //实例化对象
                Task taskBean = new Task();
                taskBean.setTaskId(rs.getInt("taskId"));                //任务id
                taskBean.setUserId(rs.getInt("userId"));                   //所属用户id
                taskBean.setTaskName(rs.getString("taskName"));             //任务名称
                taskBean.setTaskDesc(rs.getString("taskDesc"));         //任务描述
                taskBean.setCreatorName(rs.getString("creatorName"));   //创建者名字
                taskBean.setExecutorId(rs.getInt("executorId"));        //执行者id
                taskBean.setExecutorName(rs.getString("executorName")); //执行者名字
                taskBean.setStatus(rs.getInt("status"));                //任务的状态
                taskBean.setReason(rs.getString("reason"));             //任务拒绝理由
                taskBean.setIsDelete(rs.getInt("isDelete"));            //任务删除状态
                taskBean.setCreateTime(rs.getTimestamp("createTime"));  //创建时间
                taskBean.setUpdateTime(rs.getTimestamp("updateTime"));  //修改时间
                taskBean.setPrecent(getRunPrecent(taskBean.getTaskId()));               //执行百分比
                //添加到list
                taskList.add(taskBean);
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
        //返回任务列表
        return taskList;
    }

    /**
     * 获取测试人员已结束任务列表
     * @param userId 用户id
     * @return List<Task>
     */
    public List<Task> getTesterFinishTasks(int userId,Task task){
        //定义接收对象
        List<Task> taskList = new ArrayList<Task>();
        //定义数据库连接对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            //获取连接
            conn = util.getConnerction();

            //定义执行sql
            StringBuffer sql = new StringBuffer();
            //默认查询
            sql.append(
                    "select task.* ,user1.username as creatorName,user2.username as executorName from " +
                            "task ,users user1,users user2 " +
                            "where task.userId = user1.userId " +
                            "and task.executorId = user2.userId");

            //当查询的任务id不等于0
            if(task.getSearch_taskId() != 0){
                sql.append(" and task.taskId = "+task.getSearch_taskId());
            }
            //当查询的任务名称不等于空
            if(!(task.getSearch_name() == null) && !(task.getSearch_name().equals(""))){
                sql.append(" and task.taskName like \'%"+task.getSearch_name()+"%\'");
            }

            //当查询的状态值>0
            if(task.getSearch_status() > 0 && ((task.getSearch_status() == CommonParam.STATUS_FINISHED) || (task.getSearch_status() == CommonParam.STATUS_REFLUSED))){
                sql.append(" and task.status = "+task.getSearch_status());
            }else {
                //否则查询未发布、已发布
                sql.append(" and task.status in ("+CommonParam.STATUS_FINISHED+","+CommonParam.STATUS_REFLUSED+")");
            }

            sql.append(" and task.executorId = "+userId);                     //执行者id
            sql.append(" and task.isDelete = "+CommonParam.NOT_DELETE);       //删除状态
            sql.append(" order by task.updateTime desc");                     //排序

            //预编译
            ps = conn.prepareStatement(sql.toString());
            //执行查询
            rs = ps.executeQuery();

            while (rs.next()){
                //实例化对象
                Task taskBean = new Task();
                taskBean.setTaskId(rs.getInt("taskId"));                //任务id
                taskBean.setUserId(rs.getInt("userId"));                   //所属用户id
                taskBean.setTaskName(rs.getString("taskName"));             //任务名称
                taskBean.setTaskDesc(rs.getString("taskDesc"));         //任务描述
                taskBean.setCreatorName(rs.getString("creatorName"));   //创建者名字
                taskBean.setExecutorId(rs.getInt("executorId"));        //执行者id
                taskBean.setExecutorName(rs.getString("executorName")); //执行者名字
                taskBean.setStatus(rs.getInt("status"));                //任务的状态
                taskBean.setReason(rs.getString("reason"));             //任务拒绝理由
                taskBean.setIsDelete(rs.getInt("isDelete"));            //任务删除状态
                taskBean.setCreateTime(rs.getTimestamp("createTime"));  //创建时间
                taskBean.setUpdateTime(rs.getTimestamp("updateTime"));  //修改时间
                //添加到list
                taskList.add(taskBean);
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
        //返回list
        return taskList;
    }

    /**
     * 接受任务
     * @param taskId 任务id
     * @return 执行状态
     */
    public int acceptTask(int taskId){
        //定义执行状态
        int num = 0;
        //定义数据库相关操作对象
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            //定义sql
            String sql = "update task set status = ?,updateTime = ? where taskId = ? and isDelete = ?";
            ps = conn.prepareStatement(sql);
            //设置任务的状态为已发布：1
            ps.setInt(1,CommonParam.STATUS_NOT_START);
            //设置修改时间
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(2,timestamp);
            //设置任务ID
            ps.setInt(3,taskId);
            //设置删除状态
            ps.setInt(4,CommonParam.NOT_DELETE);
            //执行查询并返回影响数据条数
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
        //返回操作影响条数
        return num;
    }

    /**
     * 获取用例执行百分比
     * @param taskId 任务id
     * @return 执行百分比
     */
    public String getRunPrecent(int taskId){
        //返回结果字段定义
        String precent = "";
        //数据库连接字段定义
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps1= null;
        ResultSet rs1 = null;
        PreparedStatement updatePs = null;
        //定义该任务中所有用例数量
        int totalCount = 0;
        //定义该任务中已执行用例数量
        int ranCount = 0;
        try {
            conn = util.getConnerction();
            //开启事务
            util.beginTransaction(conn);
            //查询所有的用例
            String sql = "select count(*) from task_case where taskId  = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,taskId);    //任务id
            rs = ps.executeQuery();
            while (rs.next()){
                //当前任务包含的用例总数
                totalCount = rs.getInt(1);
            }
            //查询已执行的用例
            String sql1 = "select count(*) from task_case where taskId  = ? and isRun = ?";
            ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1,taskId);           //任务id
            ps1.setInt(2,CommonParam.RAN);  //状态为已经运行
            rs1 = ps1.executeQuery();
            while (rs1.next()){
                //已经运行的用例数量
                ranCount = rs1.getInt(1);
            }

            Double p3 = 0.0;
            if(totalCount == 0){
                p3 = 0.0;
            }else{
                p3 = ranCount*1.0/totalCount;
            }
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMinimumFractionDigits(2);//控制保留小数点后几位，2：表示保留2位小数点
            precent = nf.format(p3);

            //当百分比为100%时，设置任务为已完成
            if(p3 == 1.0){
                String sql2 = "update task set status = ?,updateTime = ? where taskId = ? and isDelete = ?";

                updatePs = conn.prepareStatement(sql2);
                //设置任务的状态为已发布：1
                updatePs.setInt(1,CommonParam.STATUS_FINISHED);
                //设置修改时间
                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                updatePs.setTimestamp(2,timestamp);
                //设置任务ID
                updatePs.setInt(3,taskId);
                //设置删除状态
                updatePs.setInt(4,CommonParam.NOT_DELETE);

                int num = updatePs.executeUpdate();
            }
            //提交事务 因当完成度为100%时，需要执行数据库修改操作
            util.commitTransaction(conn);
        }catch (SQLException e){
            try {
                util.rollbackTransaction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            try {
                //关闭数据库连接
                if(updatePs != null){
                    updatePs.close();
                }
                rs1.close();
                ps1.close();
                rs.close();
                ps.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回百分比
        return precent;
    }
    /**
     * 拒绝任务
     * @param task 任务
     * @return 执行状态
     */
    public int refuseTask(Task task){
        //影响记录数
        int num = 0;
        //数据库相关定义
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            conn = util.getConnerction();
            String sql = "update task set status = ?, reason = ? ,updateTime = ? where taskId = ? and isDelete = ?";
            ps = conn.prepareStatement(sql);
            //设置任务的状态为已发布：1
            ps.setInt(1,CommonParam.STATUS_REFLUSED);
            //设置拒绝理由
            ps.setString(2,task.getReason());
            //设置修改时间
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            ps.setTimestamp(3,timestamp);
            //设置任务ID
            ps.setInt(4,task.getTaskId());
            //设置删除状态
            ps.setInt(5,CommonParam.NOT_DELETE);
            //执行修改操作
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
        //返回影响记录数
        return num;
    }
}
