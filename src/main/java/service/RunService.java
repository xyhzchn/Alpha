package service;

import bean.Run;

/**
 * 执行用例service
 * Created by sofronie on 2017/8/16.
 */
public interface RunService {

    /**
     * 保存用例运行结果
     * @param run 执行日志
     * @return 影响数据条数
     */
    public int saveCaseResult(Run run,int taskId);

}
