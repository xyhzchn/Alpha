package dao;

import bean.Case;
import bean.Page;
import java.util.List;
import java.util.Map;

/**
 * 用例操作dao接口
 * Created by sofronie on 2017/8/1.
 */
public interface CaseDao {

    /**
     * 通过目录ID，获取该目录下的所有用例列表
     * @param acase 用例实例
     * @return List<Case>
     */
    //public List<Case> getCaseList(int catalog_id,int offset,int pageSize);
//    public Page searchCase(Case acase);

    /**
     * 通过用例ID，获取某一用例详情
     * @param caseId 用例id
     * @return Case
     */
    public Case getCaseById(int caseId);

    /**
     * 修改测试用例
     * @param acase 用例实例
     * @return 用例实例
     */
    public Case updateCase(Case acase);

    /**
     * 删除测试用例
     * @param caseId 用例id
     * @return 影响记录条数
     */
    public int deleteCase(int caseId);

    /**
     * 通过导出的用例ID，获取要导出的用例列表
     * @return
     */
    public List<Case> getExportCaseList(Case acase);
    /**
     * 添加用例到数据库
     * @param acase
     * @return
     */
    public int addCase(Case acase);

    /**
     * 获取脚本列表
     * @param userRole 用户角色
     * @param userId 用户id
     * @return 脚本列表
     */
    public Map<Integer,String> getScriptList(int userRole, int userId);

    /**
     * 查询所有用例包含的优先级列表
     * @return 优先级列表
     */
    public List<String> getCasePriority();

    /**
     * 获取用例包含的版本号列表
     * @return 版本号列表
     */
    public List<String> getVersionList();

    /**
     * 添加用例列表
     * @param cases 用例列表
     * @return 执行数量
     */
    public int addCaseList(List<Case> cases);

    /**
     * 获取分页信息
     * @param pageSize 每页记录数
     * @param page 当前页面
     * @return page对象
     */
    public Page getPage(int pageSize, int page,Case acase);




}
