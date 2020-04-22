package serviceImpl;

import bean.Run;
import dao.RunDao;
import daoImpl.RunDaoImpl;
import service.RunService;

/**
 * 用例执行
 * Created by sofronie on 2017/8/16.
 */
public class RunServiceImpl implements RunService {

    private RunDao runDao = new RunDaoImpl();

    /**
     * 保存用例运行结果
     * @param run 执行log
     * @return 影响数据条数
     */
    public int saveCaseResult(Run run,int taskId){
        return runDao.saveCaseResult(run,taskId);
    }

    /**----------------------------getter and setter--------------------------------*/
    public RunDao getRunDao() {
        return runDao;
    }

    public void setRunDao(RunDao runDao) {
        this.runDao = runDao;
    }
}
