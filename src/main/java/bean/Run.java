package bean;

import java.sql.Timestamp;

/**
 * 执行日志
 * Created by sofronie on 2017/8/16.
 */
public class Run {
    //执行用例的logId
    private int runId;
    //用例id
    private int caseId;
    //执行结果：0：删除 1：成功  2、失败  3、暂不执行
    private int result;
    //执行者id
    private int staffId;
    //执行者名字
    private String staffName;
    //执行开始时间
    private Timestamp startTime;
    //执行结束时间
    private Timestamp endTime;
    //执行次数
    private int times;
    //执行方式
    private int staffWay;
    //执行时添加的描述信息
    private String runDesc;
    //出现bug对应的bug_id
    private int bugId;
    //bug名称
    private String bugName;
    //log创建时间
    private Timestamp createTime;

    /**------------------------getter   and setter---------------*/
    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getRunDesc() {
        return runDesc;
    }

    public void setRunDesc(String runDesc) {
        this.runDesc = runDesc;
    }

    public int getBugId() {
        return bugId;
    }

    public void setBugId(int bugId) {
        this.bugId = bugId;
    }

    public String getBugName() {
        return bugName;
    }

    public void setBugName(String bugName) {
        this.bugName = bugName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getStaffWay() {
        return staffWay;
    }

    public void setStaffWay(int staffWay) {
        this.staffWay = staffWay;
    }
}
