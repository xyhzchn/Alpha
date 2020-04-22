package serviceImpl;

import bean.Script;
import dao.ScriptDao;
import daoImpl.ScriptDaoImpl;
import service.ScriptService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by sofronie on 2017/12/11.
 */
public class ScriptServiceImpl implements ScriptService{

    //依赖注入
    private ScriptDao scriptDao = new ScriptDaoImpl();

    public ScriptDao getScriptDao() {
        return scriptDao;
    }

    public void setScriptDao(ScriptDao scriptDao) {
        this.scriptDao = scriptDao;
    }


    /**
     * 获取所有的测试脚本列表
     * @return 自动化测试脚本列表
     */
    public List<Script> getScriptList(int userRole,int userId,Script script){
        return scriptDao.getScriptList(userRole,userId,script);
    }

    /**
     * 添加自动化测试脚本
     * @param script 自动化测试脚本对象
     * @return 执行状态
     */
    public int addScript(Script script){
        return scriptDao.addScript(script);
    }

    /**
     * 通过脚本id获取脚本详细信息
     * @param scriptId 脚本id
     * @return 指定脚本
     */
    public Script getSciptById(int scriptId){
        return scriptDao.getSciptById(scriptId);
    }

    /**
     * 通过脚本id删除指定脚本
     * @param scriptId 脚本id
     * @return 影响记录条数
     */
    public int deleteScriptById(int scriptId){
        return scriptDao.deleteScriptById(scriptId);
    }

    /**
     * 获取脚本创建者列表
     * @return 创建者列表
     */
    public Map<Integer,String> getCreatorList(){
        return scriptDao.getCreatorList();
    }

    /**
     * 修改自动化测试脚本
     * @param script 自动化测试脚本对象
     * @return 影响数据条数
     */
    public int updateScript(Script script){
        return scriptDao.updateScript(script);
    }

    /**
     * 通过用例ID查询对应的脚本信息
     * @param caseId 用例id
     * @return 脚本信息
     */
    public List<Script> getScriptByCaseId(String[] caseId){
        return scriptDao.getScriptByCaseId(caseId);
    }
}
