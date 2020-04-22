package daoImpl;

import bean.Run;
import common.CommonParam;
import common.JDBCUtil;
import dao.RunDao;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.*;
import java.util.Date;

/**
 * dao对应实现类
 * Created by sofronie on 2017/8/16.
 */
public class RunDaoImpl extends HibernateDaoSupport implements RunDao {

    /**
     * 保存用例运行结果
     *
     * @param run 执行log
     * @return 影响记录
     */
    public int saveCaseResult(Run run,int taskId){

        //定义影响记录条数
        int num = 0;
        //数据库相关操作定义
        JDBCUtil util = new JDBCUtil();
        Connection conn = null;
        //测试用例最后的结果值修改
        PreparedStatement caseUpdatePs = null;
        //获取run表中对应用例的最大log
        PreparedStatement runSelectPs = null;
        ResultSet runSelectRs = null;
        //run表中增加记录
        PreparedStatement runInsertPs = null;
        //关联表中对应的任务下的用例设置为已运行
        PreparedStatement linkUpdatePs = null;
        //任务表中对应任务的状态查询，若为已接受，改为运行中
        PreparedStatement taskSearchPs = null;
        ResultSet taskSearchRs = null;
        PreparedStatement taskUpdatePs = null;

        try {
            //修改用例表中的最后执行结果和用例描述信息
            conn = util.getConnerction();
            //开启事务
            util.beginTransaction(conn);
            /**----修改用例表中的最后执行结果，用例描述，修改时间-----*/
            //修改用例表中数据
            String sql = "update testcase set lastResult = ?,caseDesc = ?,updateTime = ? where caseId = ? and  isDelete = ?";
            //预编译
            caseUpdatePs = conn.prepareStatement(sql);
            //赋值
            caseUpdatePs.setInt(1,run.getResult());     //结果
            caseUpdatePs.setString(2,run.getRunDesc()); //描述
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            caseUpdatePs.setTimestamp(3,timestamp);     //修改时间
            caseUpdatePs.setInt(4,run.getCaseId());  //用例ID
            caseUpdatePs.setInt(5, CommonParam.NOT_DELETE); //是否删除

            num = caseUpdatePs.executeUpdate();     //执行修改操作


            /**----获取当前用例执行次数的最大值-----*/
            //获取当前用例执行的次数
            String timesSql = "select max(times) from caserun where caseId = ?";
            //预编译
            runSelectPs = conn.prepareStatement(timesSql);
            runSelectPs.setInt(1,run.getCaseId());  //用例id
            //执行查询
            runSelectRs = runSelectPs.executeQuery();
            //定义
            int nowTimes = 0;
            while(runSelectRs.next()){
                //赋值
                nowTimes = runSelectRs.getInt(1); //当前用例执行次数的最大值
            }

            /**---添加用例执行日志------*/
            //添加用例执行日志
            String insertSql = "insert into caserun(caseId,result,staffId,startTime,endTime,times,staffWay,runDesc,bugId,createTime) values(?,?,?,?,?,?,?,?,?,?)";
            //预编译
            runInsertPs = conn.prepareStatement(insertSql);
            //赋值
            runInsertPs.setInt(1,run.getCaseId());      //用例id
            runInsertPs.setInt(2,run.getResult());         //执行结果
            runInsertPs.setInt(3,run.getStaffId());        //执行者id
            runInsertPs.setTimestamp(4,run.getStartTime());    //执行开始时间
            runInsertPs.setTimestamp(5,run.getEndTime());      //执行结束时间
            runInsertPs.setInt(6,nowTimes+1);               //执行次数
            runInsertPs.setInt(7,run.getStaffWay());      //执行方式
            runInsertPs.setString(8,run.getRunDesc());         //执行描述信息
            runInsertPs.setInt(9,run.getBugId());              //bugid
            runInsertPs.setTimestamp(10,timestamp);            //创建时间
            //执行添加操作
            num = runInsertPs.executeUpdate();
            /**---设置任务中对应的用例已执行------*/
            String updateSql2 = "update task_case set isRun = ?,lastResult = ? where taskId = ? and caseId = ?";
            linkUpdatePs = conn.prepareStatement(updateSql2);
            linkUpdatePs.setInt(1,CommonParam.RAN);
            linkUpdatePs.setInt(2,run.getResult());
            linkUpdatePs.setInt(3,taskId);
            linkUpdatePs.setInt(4,run.getCaseId());

            num = linkUpdatePs.executeUpdate();
            /**---设置任务中对应的用例已执行------*/
            String searchSql1 = "select status from task where taskId = ?";
            taskSearchPs = conn.prepareStatement(searchSql1);
            taskSearchPs.setInt(1,taskId);
            taskSearchRs = taskSearchPs.executeQuery();
            while (taskSearchRs.next()){
                if(taskSearchRs.getInt("status") == CommonParam.STATUS_NOT_START){

                    String updateSql3 = "update task set status = ? where taskId = ? and isDelete = ?";
                    taskUpdatePs = conn.prepareStatement(updateSql3);
                    //设置任务的状态为已发布：1
                    taskUpdatePs.setInt(1,CommonParam.STATUS_RUNNING);
                    //设置任务ID
                    taskUpdatePs.setInt(2,taskId);
                    //设置删除状态
                    taskUpdatePs.setInt(3,CommonParam.NOT_DELETE);

                    num = taskUpdatePs.executeUpdate();
                }
            }
            //提交事务
            util.commitTransaction(conn);

        }catch (SQLException e){
            try {
                //如果报错，回滚事务
                util.rollbackTransaction(conn);
            }catch (SQLException e1){
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            //关闭数据库连接
            try {
                if(taskUpdatePs != null){
                    taskUpdatePs.close();
                }
                taskSearchRs.close();
                taskSearchPs.close();
                linkUpdatePs.close();
                runInsertPs.close();
                runSelectRs.close();
                runSelectPs.close();
                caseUpdatePs.close();
                util.closeConnerction(conn);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //返回影响数据条数
        return num;
    }
}
