package dao;

import bean.Run;

/**
 * 执行用例相关dao
 * Created by sofronie on 2017/8/16.
 */
public interface RunDao {

    /**
     * 保存用例运行结果
     * @param run 执行的log
     * @return 影响数据条数
     */
    public int saveCaseResult(Run run,int taskId);
}
