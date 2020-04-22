package bean;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * Created by sofronie on 2017/8/1.
 */
public class Case {

    //用例ID
    private int case_id;
    //用例名字
    private String caseIdStr;
    //目录ID
    private int catalog_id;
    //自动化脚本id
    private int scriptId;
    //用例标题
    private String caseTitle;
    //前置条件
    private String precondition;
    //测试步骤
    private String testStep;
    //预期结果
    private String expectedRes;
    //用例版本
    private String caseVersion;
    //用例描述
    private String caseDesc;
    //用例优先级
    private String priority;
    //用例最后的执行结果
    private int lastResult;
    //用例拥有者ID
    private int creatorId;
    //用例拥有者名字
    private String creatorName;
    //自动化脚本名称
    private String scriptName;
    //根节点的英文名
    private String project;
    //是否已删除0：否  1：是
    private int isDelete;
    //创建时间
    private Timestamp createTime;
    //修改时间
    private Timestamp updateTime;
    //执行日志
    private List<String> runLogs;
    //根节点的英文名
    private String parentEname;
    //查询用例ID
    private int search_caseId;
    //查询用例名称
    private String search_caseTitle;
    //查询创建者id
    private int search_creator;
    //查询用例优先级
    private String search_priority;
    //查询执行结果
    private int search_result;
    //查询用例所属分类id
    private int search_catalogId;
    //查询用例的版本号
    private String search_version;
    //查询用例创建的开始时间
    private String search_timeFrom;
    //查询用例创建的结束时间
    private String search_timeTo;
    //查询用例创建的开始时间
    private Timestamp search_From;
    //查询用例创建的结束时间
    private Timestamp search_To;
    //是否是查询
    private boolean isSearch;
    //项目下的所有节点
    private String search_ids;


    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCaseIdStr() {
        return caseIdStr;
    }

    public void setCaseIdStr(String caseIdStr) {
        this.caseIdStr = caseIdStr;
    }

    public String getParentEname() {
        return parentEname;
    }

    public void setParentEname(String parentEname) {
        this.parentEname = parentEname;
    }

    public String getSearch_ids() {
        return search_ids;
    }

    public void setSearch_ids(String search_ids) {
        this.search_ids = search_ids;
    }

    public int getCase_id() {
        return case_id;
    }

    public void setCase_id(int case_id) {
        this.case_id = case_id;
    }

    public int getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(int catalog_id) {
        this.catalog_id = catalog_id;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getTestStep() {
        return testStep;
    }

    public void setTestStep(String testStep) {
        this.testStep = testStep;
    }

    public String getExpectedRes() {
        return expectedRes;
    }

    public void setExpectedRes(String expectedRes) {
        this.expectedRes = expectedRes;
    }

    public String getCaseVersion() {
        return caseVersion;
    }

    public void setCaseVersion(String caseVersion) {
        this.caseVersion = caseVersion;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
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

    public int getLastResult() {
        return lastResult;
    }

    public void setLastResult(int lastResult) {
        this.lastResult = lastResult;
    }

    public List<String> getRunLogs() {
        return runLogs;
    }

    public void setRunLogs(List<String> runLogs) {
        this.runLogs = runLogs;
    }

    public int getSearch_caseId() {
        return search_caseId;
    }

    public void setSearch_caseId(int search_caseId) {
        this.search_caseId = search_caseId;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }

    public String getSearch_caseTitle() {
        return search_caseTitle;
    }

    public void setSearch_caseTitle(String search_caseTitle) {
        this.search_caseTitle = search_caseTitle;
    }

    public int getSearch_creator() {
        return search_creator;
    }

    public void setSearch_creator(int search_creator) {
        this.search_creator = search_creator;
    }

    public String getSearch_priority() {
        return search_priority;
    }

    public void setSearch_priority(String search_priority) {
        this.search_priority = search_priority;
    }

    public int getSearch_result() {
        return search_result;
    }

    public void setSearch_result(int search_result) {
        this.search_result = search_result;
    }

    public int getSearch_catalogId() {
        return search_catalogId;
    }

    public void setSearch_catalogId(int search_catalogId) {
        this.search_catalogId = search_catalogId;
    }

    public String getSearch_version() {
        return search_version;
    }

    public void setSearch_version(String search_version) {
        this.search_version = search_version;
    }

    public String getSearch_timeFrom() {
        return search_timeFrom;
    }

    public void setSearch_timeFrom(String search_timeFrom) {
        this.search_timeFrom = search_timeFrom;
    }

    public String getSearch_timeTo() {
        return search_timeTo;
    }

    public void setSearch_timeTo(String search_timeTo) {
        this.search_timeTo = search_timeTo;
    }

    public Timestamp getSearch_From() {
        return search_From;
    }

    public void setSearch_From(Timestamp search_From) {
        this.search_From = search_From;
    }

    public Timestamp getSearch_To() {
        return search_To;
    }

    public void setSearch_To(Timestamp search_To) {
        this.search_To = search_To;
    }

    public int getScriptId() {
        return scriptId;
    }

    public void setScriptId(int scriptId) {
        this.scriptId = scriptId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

}
