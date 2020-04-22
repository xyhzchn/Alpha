package service;

import bean.Script;

import java.util.List;
import java.util.Map;

/**
 * service接口
 * Created by sofronie on 2017/12/11.
 */
public interface ScriptService {

    /**
     * 获取所有的测试脚本列表
     * @return 自动化测试脚本列表
     */
    public List<Script> getScriptList(int userRole,int userId,Script script);

    /**
     * 添加自动化测试脚本
     * @param script 自动化测试脚本对象
     * @return 执行状态
     */
    public int addScript(Script script);

    /**
     * 通过脚本id获取脚本详细信息
     * @param scriptId 脚本id
     * @return 指定脚本
     */
    public Script getSciptById(int scriptId);


    /**
     * 通过脚本id删除指定脚本
     * @param scriptId 脚本id
     * @return 影响记录条数
     */
    public int deleteScriptById(int scriptId);

    /**
     * 获取脚本创建者列表
     * @return 创建者列表
     */
    public Map<Integer,String> getCreatorList();

    /**
     * 修改自动化测试脚本
     * @param script 自动化测试脚本对象
     * @return 影响数据条数
     */
    public int updateScript(Script script);

    /**
     * 通过用例ID查询对应的脚本信息
     * @param caseId 用例id
     * @return 脚本信息
     */
    public List<Script> getScriptByCaseId(String[] caseId);

}
