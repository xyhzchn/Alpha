package dao;

import bean.Case;
import bean.Task;
import bean.Tree;

import java.util.List;
import java.util.Map;

/**
 * 任务相关持久化
 * Created by sofronie on 2017/8/10.
 */
public interface TaskDao {

    /**
     * 获取管理员未开始任务列表
     * @return 任务列表
     */
    public List<Task> getAdminPreTasks(int userId,Task task);
    /**
     * 获取管理员进行中任务列表
     * @return 任务列表
     */
    public List<Task> getAdminRunTasks(int userId,Task task);
    /**
     * 获取管理员已结束任务列表
     * @return 任务列表
     */
    public List<Task> getAdminFinishTasks(int userId,Task task);



    /**
     * 获取用例所属的创建者列表
     * @return Map<Integer,String>
     */
    public Map<Integer,String> getCreaterList();


    /**
     * 获取用例所属的目录列表
     * @return 目录列表
     */
    public List<Tree> getCatalogList();

    /**
     * 根据条件查询用例列表
     * @param acase 用例实例
     * @return 用例列表
     */
    public List<Case> searchCases(Case acase);
    /**
     * 获取执行者用户列表
     * @return 执行者对象列表
     */
    public Map<Integer, String> getExecutorList();

    /**
     * 创建任务，保存数据到数据库
     * @param task 任务信息
     * @param ids 用户选择的用例id列表
     * @return 执行状态
     */
    public int addTask(Task task, List<String> ids);

    /**
     * 通过任务id获取任务的详情信息
     * @param taskId 用户ID
     * @return 任务实例
     */
    public Task getTaskById(int taskId,boolean isContentLog);

    /**
     * 发布任务
     * @param taskId 任务id
     * @return 执行状态
     */
    public int releaseTask(int taskId);

    /**
     * 修改任务
     * @param task 任务信息
     * @param ids 任务包含的用例id
     * @return 执行状态
     */
    public int editTask(Task task,List<String> ids);

    /**
     * 获取测试人员未开始任务列表
     * @return 任务列表
     */
    public List<Task> getTesterPreTasks(int userId,Task task);

    /**
     * 获取测试人员进行中任务列表
     * @param userId 用户ID
     * @return 任务列表
     */
    public List<Task> getTesterRunTasks(int userId,Task task);
    /**
     * 获取测试人员已结束任务列表
     * @param userId 用户ID
     * @return 任务列表
     */
    public List<Task> getTesterFinishTasks(int userId,Task task);
    /**
     * 接受任务
     * @param taskId 任务id
     * @return 执行状态
     */
    public int acceptTask(int taskId);

    /**
     * 拒绝任务
     * @param task 任务
     * @return 执行状态
     */
    public int refuseTask(Task task);

}
