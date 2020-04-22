package action;

import bean.Case;
import bean.Task;
import bean.Tree;
import bean.Page;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import common.CommonParam;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import service.CaseService;
import service.TaskService;
import service.TreeService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 *
 * Created by sofronie on 2017/8/10.
 */
public class TaskAction extends ActionSupport {

    //定义任务接口
    private TaskService taskService;
    //定义用例接口
    private CaseService caseService;
    //定义节点接口
    private TreeService treeService;
    //定义任务对象
    private Task task ;
    //定义用例对象
    private Case acase;
    //分页相关定义
    private Page pageBean;
    //未开始任务列表
    private List<Task> tasks = new ArrayList<Task>();
    //进行中任务列表
    private List<Task> runtasks = new ArrayList<Task>();
    //已结束任务列表
    private List<Task> overtasks = new ArrayList<Task>();
    //任务id
    private int taskId;
    //标记
    private String flag;
    //创建任务选择的用例组
    private String[] ids;
    //当前页面
    private int page;
    //用例列表
    private List<Case> cases = new ArrayList<Case>();
    //所有的用例包含的优先级列表
    private List<String> priorityList = new ArrayList<String>();
    //所有的用例包含的创建者列表
    private Map<Integer,String> createrMap = new HashMap<Integer, String>();
    //所有的用例包含的版本号列表
    private List<String> versionList = new ArrayList<String>();
    //所有的用例包含的目录列表
    private List<Tree> catalogList = new ArrayList<Tree>();
    //定义任务创建页面执行者列表
    private Map<Integer,String> map = null;
    //任务包含的用例列表中，包含自动化测试脚本的用例数量
    private int scriptCount = 0;
    //点击的tabID
    private int tabId;
    //是否包含子节点
    private boolean containSons;


    /**
     * 获取管理员权限下任务所有界面列表
     * @return String
     */
    public String adminTaskList(){
        //获取session
        ActionContext actionContext = ActionContext.getContext();
        Map session = actionContext.getSession();
        //获取当前登录用户id
        int userId = Integer.parseInt(session.get("userId").toString());

        if(task == null){
           //实例化任务对象
           task = new Task();
        }
        //未开始任务列表
        tasks = taskService.getAdminPreTasks(userId,task);
        //进行中任务列表
        runtasks = taskService.getAdminRunTasks(userId,task);
        //已完成任务列表
        overtasks = taskService.getAdminFinishTasks(userId,task);

        return SUCCESS;
    }


    /**
     * 跳转到创建任务界面，包括
     * 获取用例列表
     * 获取用例优先级列表
     * 获取用例执行结果列表
     * 获取用例版本号列表
     * @return
     */
    public String goCreateTask(){

        //查询所有的用例包含的优先级列表
        priorityList = caseService.getCasePriority();
        //查询所有的用例包含的版本号列表
        versionList = caseService.getVersionList();
        //查询所有的用例包含的创建者列表
        createrMap = taskService.getCreaterList();
        //查询所有的用例所属的目录列表
        catalogList = treeService.selectAllNodes();

        //将List<Tree>转换成json格式
        JSONArray json = new JSONArray();
        //循环
        for(Tree tree:catalogList){
            JSONObject jo = new JSONObject();

            jo.put("level",tree.getLevel());
            jo.put("parentId",tree.getParent_id());
            jo.put("catalogName",tree.getCatalogName());
            jo.put("id",tree.getCatalogId());

            json.put(jo);
        }
        //防止在修改任务时，查询后导致任务id丢失
        if(!(taskId == 0)){
            flag = "edit";
        }
        //获取request对象
        HttpServletRequest request = ServletActionContext.getRequest();
        //request设置flag
        request.setAttribute("flag",flag);
        request.setAttribute("trees",json);
        if(acase == null){
            return SUCCESS;
        }else{
            //将选择的创建日期开始时间转换为timestamp类型
            if(acase.getSearch_timeFrom() != null && !(acase.getSearch_timeFrom().equals(""))){
                String timeFromStr = acase.getSearch_timeFrom()+" 00:00:00";
                acase.setSearch_From(Timestamp.valueOf(timeFromStr));
            }
            //将选择的创建日期截止时间转换为timestamp类型
            if(acase.getSearch_timeTo() != null && !(acase.getSearch_timeTo().equals(""))){
                String timeToStr = acase.getSearch_timeTo()+" 00:00:00";
                acase.setSearch_To(Timestamp.valueOf(timeToStr));
            }

            if(containSons){
                //选择查询项目时，设置列表
                StringBuffer sb = new StringBuffer();
                //获取节点等级
                Set catalogids = new HashSet();
                //获取该节点下的所有子节点
                catalogids =treeService.getSonsById(acase.getSearch_catalogId(),new HashSet());

                if(catalogids.size() > 0){
                    Iterator it = catalogids.iterator();
                    while (it.hasNext()){
                        sb.append(it.next());
                        sb.append(",");
                    }
                }
                acase.setSearch_ids(sb.toString().substring(0,sb.lastIndexOf(",")));
            }else {
                acase.setSearch_ids(String.valueOf(acase.getSearch_catalogId()));
            }

            //查询所有的用例列表
            cases = taskService.searchCases(acase);

            return SUCCESS;
        }

    }

    /**
     * 跳转到创建任务下一页面。同时获取执行者列表信息
     * @return String
     */
    public String goCreateTaskSec(){

        //实例化map
        map = new HashMap<Integer, String>();
        //获取执行者列表
        map = taskService.getExecutorList();
        //获取request对象
        HttpServletRequest request = ServletActionContext.getRequest();
        //request设置属性
        //request.setAttribute("map",map);
        request.setAttribute("ids",ids);
        //如果传递过来的任务id不为空
        if(!(taskId == 0)){
            //获取该任务详情信息
            task = taskService.getTaskById(taskId,false);
            //任务不为空
            if(task != null){
                request.setAttribute("flag","edit");
                return SUCCESS;
            }else{//任务获取失败
                return "failure";
            }
        }else{
            return SUCCESS;
        }
    }

    /**
     * 创建任务并保存数据到数据库
     * @return String
     */
    public String createTask(){
        //操作结果
        int num = 0;
        //获取session中登录用户的Id和用户名。作为任务的创建者id和创建者名称
        ActionContext actionContext = ActionContext.getContext();
        Map session = actionContext.getSession();
        //设置任务管理的用户id
        task.setUserId(Integer.parseInt(session.get("userId").toString()));
        //设置任务的创建者名字
        task.setCreatorName(session.get("userName").toString());

        //选择的用例id列表格式化
        List<String>  ids_format= new ArrayList<String>();

            String[] id = ids[0].split(",");
            for(int i=0;i<id.length;i++){
                ids_format.add(id[i]);
            }

        num = taskService.addTask(task,ids_format);

        if(num>0){
            return SUCCESS;
        }else{
            return "failure";
        }
    }


    /**
     * 获取单个任务的任务详情信息
     * @return String
     */
    public String getTaskDetail(){
        //实例化任务对象
        task = new Task();

        if(!(flag == null) && flag.equals("run")){
            //通过任务id获取任务详情
            task = taskService.getTaskById(taskId,true);
        }else{
            //通过任务id获取任务详情
            task = taskService.getTaskById(taskId,false);
        }
        //获取任务包含的用例列表中，包含自动化测试脚本的用例数量
        for (int i=0;i<task.getCaselist().size();i++){
            if(task.getCaselist().get(i).getScriptId() > 0){
                scriptCount+= 1;
            }
        }
        //任务获取成功
        if(task != null){
            return SUCCESS;
        }else {
            //任务获取失败
            return "failure";
        }
    }

    /**
     * 发布任务
     * @return String
     */
    public String releaseTask(){

        //执行结果
        int num = taskService.releaseTask(taskId);
        //当影响数据条数>0
        if (num > 0){
            return SUCCESS;
        }else{
            return "failure";
        }
    }

    /**
     * 跳转到修改任务，跳转到一级页面
     * @return 执行结果
     */
    public String editTask(){
        //操作结果
        int num = 0;

        //选择的用例id列表格式化
        List<String>  ids_format= new ArrayList<String>();

        String[] id = ids[0].split(",");
        for(int i=0;i<id.length;i++){
            ids_format.add(id[i]);
        }

        num = taskService.editTask(task,ids_format);

        //当影响数据条数>0
        if (num > 0){
            return SUCCESS;
        }else{
            return "failure";
        }
    }



    /**
     * 获取测试人员权限下未开始用例列表
     * @return 所有相关的标签下的列表
     */
    public String testerTaskList(){
        //获取session
        ActionContext actionContext = ActionContext.getContext();
        Map session = actionContext.getSession();
        //获取当前登录的用户id
        int userId = Integer.parseInt(session.get("userId").toString());
        //当任务实例为空
        if(task == null){
            //实例化任务
            task = new Task();
        }

        //查询未开始任务列表
        tasks = taskService.getTesterPreTasks(userId,task);
        //运行中任务列表
        runtasks = taskService.getTesterRunTasks(userId,task);
        //已完成任务列表
        overtasks = taskService.getTesterFinishTasks(userId,task);

        return SUCCESS;
    }

    /**
     * 接受任务
     * @return String
     */
    public String acceptTask(){
        //执行状态
        int num = 0;
        //接受任务
        num = taskService.acceptTask(taskId);

        if (num>0){
            return SUCCESS;
        }else {
            return "failure";
        }
    }

    /**
     * 拒绝任务
     * @return String
     */
    public String refuseTask(){
        //影响记录条数
        int num = taskService.refuseTask(task);

        if(num>0){
            return SUCCESS;
        }else{
            return "failure";
        }
    }

    /**-------------------------------getter and setter----------------------------*/
    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<Tree> getCatalogList() {
        return catalogList;
    }

    public void setCatalogList(List<Tree> catalogList) {
        this.catalogList = catalogList;
    }

    public List<String> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<String> versionList) {
        this.versionList = versionList;
    }

    public Map<Integer, String> getCreaterMap() {
        return createrMap;
    }

    public void setCreaterMap(Map<Integer, String> createrMap) {
        this.createrMap = createrMap;
    }

    public List<String> getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(List<String> priorityList) {
        this.priorityList = priorityList;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
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

    public Map<Integer, String> getMap() {
        return map;
    }

    public void setMap(Map<Integer, String> map) {
        this.map = map;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public List<Task> getRuntasks() {
        return runtasks;
    }

    public void setRuntasks(List<Task> runtasks) {
        this.runtasks = runtasks;
    }

    public List<Task> getOvertasks() {
        return overtasks;
    }

    public void setOvertasks(List<Task> overtasks) {
        this.overtasks = overtasks;
    }

    public Case getAcase() {
        return acase;
    }

    public void setAcase(Case acase) {
        this.acase = acase;
    }

    public int getScriptCount() {
        return scriptCount;
    }

    public void setScriptCount(int scriptCount) {
        this.scriptCount = scriptCount;
    }

    public int getTabId() {
        return tabId;
    }

    public void setTabId(int tabId) {
        this.tabId = tabId;
    }

    public CaseService getCaseService() {
        return caseService;
    }

    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    public Page getPageBean() {
        return pageBean;
    }

    public void setPageBean(Page pageBean) {
        this.pageBean = pageBean;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public TreeService getTreeService() {
        return treeService;
    }

    public void setTreeService(TreeService treeService) {
        this.treeService = treeService;
    }

    public boolean isContainSons() {
        return containSons;
    }

    public void setContainSons(boolean containSons) {
        this.containSons = containSons;
    }
}
