package bean;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by sofronie on 2017/7/27.
 * 创建用例目录所用的实体类
 * 对应与数据库中catalog表
 */
public class Tree {
    //节点id
    private int catalogId;
    //节点中文名
    private String catalogName;
    //节点英文名
    private String ename;
    //节点等级
    private int level;
    //节点所在的父节点id
    private int parent_id;
    //节点是否已删除
    private int isDelete;
    //节点创建时间
    private Timestamp createTime;
    //节点修改时间
    private Timestamp updateTime;

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
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
}
