package action;

import bean.Run;
import bean.Script;
import bean.Task;
import com.offbytwo.jenkins.model.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import common.CommonParam;
import org.apache.struts2.ServletActionContext;
import service.RunService;
import service.ScriptService;
import service.TaskService;
import serviceImpl.RunServiceImpl;
import serviceImpl.ScriptServiceImpl;

import java.io.*;
import java.net.URI;
import java.sql.Timestamp;
import java.util.*;

import com.offbytwo.jenkins.JenkinsServer;
import serviceImpl.TaskServiceImpl;

/**
 * Created by sofronie on 2017/12/11.
 *
 */
public class ScriptAction extends ActionSupport{

    //定义service对象
    private ScriptService scriptService = new ScriptServiceImpl();
    //任务执行接口类
    private RunService runService = new RunServiceImpl();
    //定义脚本列表
    private List<Script> scripts = new ArrayList<Script>();
    //定义脚本类
    private Script script = new Script();
    //脚本ID
    private int scriptId;
    //标记
    private String flag;
    //用例创建者列表
    private Map<Integer,String> creatorList = null;
    //用例ID
    private String caseId;
    //任务ID
    private int taskId;
    //定义任务相关service接口
    private TaskService taskService = new TaskServiceImpl();
    //定义任务对象
    private Task task ;
    //任务包含的用例列表中，包含自动化测试脚本的用例数量
    private int scriptCount = 0;
    //执行用例组
    private String[] ids;

    /**
     * 获取自动化测试脚本列表
     * @return 处理结果
     */
    public String getScriptList(){

        //获取session中存放的登录用户的id和角色id
        Map<String,Object> session = ActionContext.getContext().getSession();
        //角色id
        int userRole = Integer.parseInt(session.get("userRole").toString());
        //用户id
        int userId = Integer.parseInt(session.get("userId").toString());

        scripts = scriptService.getScriptList(userRole,userId,script);
        //当用户角色是管理员时，查询创建者列表
        if (userRole == 1){
            //定义创建者列表
            creatorList = new HashMap<Integer, String>();
            //获取创建者列表
            creatorList = scriptService.getCreatorList();
        }


        return SUCCESS;
    }

    /**
     * 添加脚本
     * @return 处理结果
     */
    public String addScript(){

        //获取session中存放的登录用户的id和角色id
        Map<String,Object> session = ActionContext.getContext().getSession();
        //设置当前用户的UID
        script.setCreator(Integer.parseInt(session.get("userId").toString()));
        //添加脚本
        int result = scriptService.addScript(script);
        //判断影响数据条数
        if(result > 0){
            return SUCCESS;
        }else {
            return "failure";
        }

    }


    /**
     * 通过脚本ID，获取脚本的详细信息
     * @return 处理状态
     */
    public String getScriptById(){

        script = scriptService.getSciptById(scriptId);

        //查询出来的脚本对象不为空
        if (script != null){
            //设置标记为detail
            flag = "detail";

            return SUCCESS;
        }else {
            return "failure";
        }
    }


    /**
     * 删除自动化测试脚本通过ID
     * @return 执行结果
     */
    public String deleteScriptById(){

        //删除指定id的自动化测试脚本并返回影响记录条数
        int num = scriptService.deleteScriptById(scriptId);
        //影响记录条数大于0
        if (num > 0){
            return SUCCESS;
        }else {
            return "failure";
        }
    }

    /**
     * 编辑指定的脚本
     * @return 处理结果
     */
    public String updateScript(){

        //修改相关的脚本并返回影响记录条数
        int num = scriptService.updateScript(script);
        //判断
        if(num > 0){
            return SUCCESS;
        }else {
            return "failure";
        }

    }

    /**
     * 执行自动化测试脚本
     * @return
     */
    public String runScript(){
        //修改执行结果
        int num = 0;
        //多个用例执行
        String[] caseIds = null;
        if(ids != null){
            caseIds = ids[0].split(",");
        }else{
            caseIds = new String[1];
            caseIds[0] = caseId;
        }

        //根据用例名称，查询脚本Id
        scripts = scriptService.getScriptByCaseId(caseIds);
        if (scripts.size() > 0){
            for (Script scriptBean:scripts) {

                //定义脚本执行结果
                String result = "";
                //设置job名字,格式：Test_脚本名称_当前时间戳
                String jobName = "Test_"+scriptBean.getName()+"_"+System.currentTimeMillis();
                //定义run对象
                Run run = new Run();

                if(!(scriptBean.getName().equals(""))&& scriptBean.getName() != null){
                    try{
                        //启动jenkins服务
                        JenkinsServer server = new JenkinsServer(new URI(CommonParam.URL),CommonParam.USERNAME,CommonParam.PASSWORD);
                        //获取job配置信息
                        String jobxml = getJobConfig();

                        //创建一个job
                        server.createJob(jobName,jobxml,true);
                        //获取所有的job
                        Map<String ,Job> jobs = server.getJobs();
                        //获取job
                        Job job = jobs.get(jobName);
                        //设置job构建参数
                        Map<String,String> param = new HashMap<String, String>();
                        param.put("testCase",scriptBean.getName());
                        //job执行
                        job.build(param,true);

                        while(1==1){
                            //获取job详情
                            JobWithDetails jobDetail = job.details();
                            //获取job执行number
                            int job_num = jobDetail.getNextBuildNumber();
                            //判断，当构建数量>0且构建结果不为空时
                            if (jobDetail.getBuilds().size() > 0 && !(jobDetail.getLastBuild().details().getResult() == null)){
                                //构建结果赋值
                                result = jobDetail.getLastBuild().details().getResult().toString();
                                if (result.equals("SUCCESS")){
                                    run.setResult(1);
                                }else if (result.equals("FAILURE")){
                                    run.setResult(2);
                                }else {
                                    run.setResult(3);
                                }
                                break;
                            }else {
                                //否则，线程休息5秒
                                Thread.sleep(3000);
                                continue;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                //对象赋值
                //获取session
                ActionContext context = ActionContext.getContext();
                Map session = context.getSession();
                //设置执行者名称和id信息
                run.setStaffId(Integer.parseInt(session.get("userId").toString())); //执行者id
                run.setStaffName(session.get("userName").toString());               //执行者名字

                run.setCaseId(scriptBean.getCaseId());
                run.setStaffWay(CommonParam.BY_AUTO);

                //保存执行log
                num = runService.saveCaseResult(run,taskId);

                //通过任务id获取任务详情
                task = taskService.getTaskById(taskId,true);
                //获取任务包含的用例列表中，包含自动化测试脚本的用例数量
                for (int i=0;i<task.getCaselist().size();i++){
                    if(task.getCaselist().get(i).getScriptId() > 0){
                        scriptCount+= 1;
                    }
                }
            }
        }
        //任务获取成功
        if(num > 0 && task != null){
            return SUCCESS;
        }else {
            //任务获取失败
            return "failure";
        }
    }
    /**
     * 通过配置文件读取job 配置
     * @return 配置文件内容
     */
    public String getJobConfig(){
        //定义返回值
        String jobconfig = "";

        int len = 0;
        //定义StringBuffer
        StringBuffer sb = new StringBuffer("");

        String fileName = ServletActionContext.getServletContext().getRealPath("/xml/")+"jobConfig.xml";
        try{
            //定义输入流
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            //定义
            String line = null;
            //一行一行读取
            while ((line = in.readLine())!= null){
                if(len != 0){
                    //处理换行符
                    sb.append("\r\n"+line);
                }else {
                    sb.append(line);
                }
                len++;
            }
            //关闭输入流
            in.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        //转换成String格式
        jobconfig = sb.toString();

        return jobconfig;
    }



    public ScriptService getScriptService() {
        return scriptService;
    }

    public void setScriptService(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    public List<Script> getScripts() {
        return scripts;
    }

    public void setScripts(List<Script> scripts) {
        this.scripts = scripts;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public int getScriptId() {
        return scriptId;
    }

    public void setScriptId(int scriptId) {
        this.scriptId = scriptId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Map<Integer, String> getCreatorList() {
        return creatorList;
    }

    public void setCreatorList(Map<Integer, String> creatorList) {
        this.creatorList = creatorList;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public RunService getRunService() {
        return runService;
    }

    public void setRunService(RunService runService) {
        this.runService = runService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getScriptCount() {
        return scriptCount;
    }

    public void setScriptCount(int scriptCount) {
        this.scriptCount = scriptCount;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
