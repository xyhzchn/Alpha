package bean;

import java.sql.Timestamp;
import java.util.List;

/**
 * 任务javabean
 * Created by sofronie on 2017/8/10.
 */
public class Task {
    //任务ID
    private int taskId;
    //任务拥有者ID
    private int userId;
    //任务名称
    private String taskName;
    //任务描述
    private String taskDesc;
    //创建者名字
    private String creatorName;
    //任务执行者ID
    private int executorId;
    //任务执行者名字
    private String executorName;
    //任务的当前状态 0:创建未发布 1:已发布 2:已接受 3:已拒绝 4:执行中 5:已完成
    private int status;
    //拒绝理由
    private String reason;
    //任务的删除状态 0：未删除  1：已删除
    private int isDelete;
    //任务创建时间
    private Timestamp createTime;
    //任务修改时间
    private Timestamp updateTime;
    //任务执行百分比
    private String precent;
    //包含的用例列表
    private List<Case> caselist;
    //查询用的任务id
    private int search_taskId;
    //查询用的任务名字
    private String search_name;
    //查询用的任务状态
    private int search_status;


    /**------------------------------getter and setter----------------------------*/
    public int getSearch_taskId() {
        return search_taskId;
    }

    public void setSearch_taskId(int search_taskId) {
        this.search_taskId = search_taskId;
    }

    public String getSearch_name() {
        return search_name;
    }

    public void setSearch_name(String search_name) {
        this.search_name = search_name;
    }

    public int getSearch_status() {
        return search_status;
    }

    public void setSearch_status(int search_status) {
        this.search_status = search_status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getExecutorId() {
        return executorId;
    }

    public void setExecutorId(int executorId) {
        this.executorId = executorId;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Case> getCaselist() {
        return caselist;
    }

    public void setCaselist(List<Case> caselist) {
        this.caselist = caselist;
    }

    public String getPrecent() {
        return precent;
    }

    public void setPrecent(String precent) {
        this.precent = precent;
    }
}
