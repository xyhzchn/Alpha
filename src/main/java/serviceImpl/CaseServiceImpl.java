package serviceImpl;

import bean.Case;
import dao.CaseDao;
import service.CaseService;
import bean.Page;
import java.util.List;
import java.util.Map;

/**
 * 用例相关service实现类
 * Created by sofronie on 2017/8/1.
 */
public class CaseServiceImpl implements CaseService {

    private CaseDao caseDao;

    /**
     * 通过目录ID，获取该目录下的所有用例列表
     * @param acase 用例实例
     * @return 用例列表
     */
//    public Page searchCase(Case acase){
//        return  caseDao.searchCase(acase);
//    }

    /**
     * 通过用例ID，获取某一用例详情
     * @param caseId 用例id
     * @return Case
     */
    public Case getCaseById(int caseId){
        return caseDao.getCaseById(caseId);
    }

    /**
     * 修改测试用例
     * @param acase 实例
     * @return 实例
     */
    public Case updateCase(Case acase){
        return  caseDao.updateCase(acase);
    }

    /**
     * 删除测试用例
     * @param caseId 用例id
     * @return 影响记录条数
     */
    public int deleteCase(int caseId){
        return caseDao.deleteCase(caseId);
    }


    /**
     * 通过导出的用例ID，获取要导出的用例列表
     * @return
     */
    public List<Case> getExportCaseList(Case acase){
        return caseDao.getExportCaseList(acase);
    }

    /**
     * 添加用例到数据库
     * @param acase
     * @return
     */
    public int addCase(Case acase){
        return caseDao.addCase(acase);
    }

    /**
     * 获取脚本列表
     * @param userRole 用户角色
     * @param userId 用户id
     * @return 脚本列表
     */
    public Map<Integer,String> getScriptList(int userRole, int userId){
        return caseDao.getScriptList(userRole,userId);
    }

    /**
     * 添加用例列表
     * @param cases 用例列表
     * @return 执行数量
     */
    public int addCaseList(List<Case> cases){
        return caseDao.addCaseList(cases);
    }

    /**
     * 查询所有用例包含的优先级列表
     * @return 优先级列表
     */
    public List<String> getCasePriority(){
        return caseDao.getCasePriority();
    }

    /**
     * 获取用例包含的版本号列表
     * @return 版本号列表
     */
    public List<String> getVersionList(){
        return caseDao.getVersionList();
    }

    /**
     * 获取分页信息
     * @param pageSize 每页记录数
     * @param page 当前页面
     * @return page对象
     */
    public Page getPage(int pageSize, int page,Case acase){
        return caseDao.getPage(pageSize,page,acase);
    }
    /**-------------------getter and setter---------------------------------*/

    public CaseDao getCaseDao() {
        return caseDao;
    }

    public void setCaseDao(CaseDao caseDao) {
        this.caseDao = caseDao;
    }

}
