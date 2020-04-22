package action;

import bean.Case;
import bean.Run;
import bean.Task;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import common.CommonParam;
import org.apache.struts2.ServletActionContext;
import service.CaseService;
import service.RunService;
import service.TaskService;
import serviceImpl.CaseServiceImpl;
import serviceImpl.RunServiceImpl;
import serviceImpl.TaskServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 完成用例执行相关操作
 * Created by sofronie on 2017/8/16.
 */
public class RunAction extends ActionSupport {

    //任务执行接口类
    private RunService runService;
    //任务接口
    private TaskService taskService;
    //用例接口
    private CaseService caseService;
    //任务执行Javabean
    private Run run ;
    //任务下的用例列表
    private List<Case> allCases;
    //用例id
    private String caseId;
    //任务id
    private int taskId;
    //用例
    private Case acase;
    //json数据
    private String jsonData;
    //标记
    private String flag;
    //所有的自动化测试脚本列表
    private Map<Integer,String> scriptList = new HashMap<Integer, String>();
    /**
     * 获取所有用例，并显示在用例详情页面；
     * @return 获取所有用例
     */
    public String getRunCaseDetail(){
        //定义任务实例
        Task task = new Task();
        //因从上一个页面传递用例列表不方便，暂时再次根据任务id取得用例列表
        task = taskService.getTaskById(taskId,true);
        //如果返回的任务对象不为空
        if (task != null){
            //获取任务id
            taskId = task.getTaskId();
            //获取所有的用例列表
            allCases = task.getCaselist();
            //获取session中存放的登录用户的id和角色id
            Map<String,Object> session = ActionContext.getContext().getSession();
            //角色id
            int userRole = Integer.parseInt(session.get("userRole").toString());
            //用户id
            int userId = Integer.parseInt(session.get("userId").toString());
            //自动化测试脚本列表
            scriptList = caseService.getScriptList(userRole, userId);
            //定义flag
            flag = "run";
            //将传递过来的用例iD暂存到request作用域
            HttpServletRequest request = ServletActionContext.getRequest();
            //将点击的用例id放到request
            request.setAttribute("passCaseId",caseId);
            request.setAttribute("flag",flag);

            return SUCCESS;
        }else{
            return "failure";
        }
    }

    /**
     * 保存用例执行日志
     * @return 执行用例影响的用例条数
     */
    public String saveCaseResult(){
       //影响记录数
       int num = 0;
       //获取session
       ActionContext context = ActionContext.getContext();
       Map session = context.getSession();
       //设置执行者名称和id信息
        run.setStaffId(Integer.parseInt(session.get("userId").toString())); //执行者id
        run.setStaffName(session.get("userName").toString());               //执行者名字
        run.setStaffWay(CommonParam.BY_HAND);
        //保存执行log
        num = runService.saveCaseResult(run,taskId);

        //json格式数据
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("{caseId:"+run.getCaseId()+"}");
        sb.append("]");

        jsonData = sb.toString();

        if(num>0){
            return SUCCESS;
        }else{
            return "failure";
        }
    }

    /**----------------------getter  and  setter-------------------------------*/
    public List<Case> getAllCases() {
        return allCases;
    }

    public void setAllCases(List<Case> allCases) {
        this.allCases = allCases;
    }

    public RunService getRunService() {
        return runService;
    }

    public void setRunService(RunService runService) {
        this.runService = runService;
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
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

    public Case getAcase() {
        return acase;
    }

    public void setAcase(Case acase) {
        this.acase = acase;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Map<Integer, String> getScriptList() {
        return scriptList;
    }

    public void setScriptList(Map<Integer, String> scriptList) {
        this.scriptList = scriptList;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public CaseService getCaseService() {
        return caseService;
    }

    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
}
