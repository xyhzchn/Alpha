package bean;

import java.sql.Timestamp;

/**
 * 脚本javabean
 * Created by sofronie on 2017/12/11.
 */
public class Script {

    //脚本id
    private int id;
    //脚本对应类名
    private String name;
    //脚本路径
    private String location;
    //脚本描述
    private String scriptDesc;
    //脚本创建者id
    private int creator;
    //是否删除
    private int isDelete;
    //创建日期
    private Timestamp createTime;
    //修改日期
    private Timestamp updateTime;
    //创建者名字
    private String creatorName;
    //检索脚本id
    private int searchId;
    //检索脚本名称
    private String searchName;
    //检索创建者
    private int searchCreator;
    //脚本对应的用例ID
    private int caseId;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getScriptDesc() {
        return scriptDesc;
    }

    public void setScriptDesc(String scriptDesc) {
        this.scriptDesc = scriptDesc;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public int getSearchCreator() {
        return searchCreator;
    }

    public void setSearchCreator(int searchCreator) {
        this.searchCreator = searchCreator;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }
}
