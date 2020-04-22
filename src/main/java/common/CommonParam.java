package common;

/**
 *
 * Created by sofronie on 2017/8/15.
 */
public interface CommonParam {

    /**------------任务发布状态定义--------------**/
    //1:创建未发布  2:已发布 3:已接受 4:已拒绝 5:执行中 6:已完成
    public final  int STATUS_NOT_RELEASE = 1;
    //已发布
    public final  int STATUS_RELEASED = 2;
    //已接受未开始
    public final  int STATUS_NOT_START = 3;
    //已拒绝
    public final  int STATUS_REFLUSED = 4;
    //执行中
    public final  int STATUS_RUNNING = 5;
    //已完成
    public final  int STATUS_FINISHED = 6;
    /**------------删除状态定义--------------**/
    //未删除
    public final  int NOT_DELETE = 0;
    //已发布
    public final  int DELETED = 1;

    /**------------测试用例执行结果定义--------------**/
    //通过
    public final  String RESULT_PASS = "PASS";
    //失败
    public final  String RESULT_FAIL = "FAIL";
    //暂不执行
    public final  String RESULT_BLOCK = "BLOCK";
    //删除
    public final  String RESULT_DELETE = "DELETE";
    /**------------测试用例执行状态定义--------------**/
    //已执行
    public final int RAN = 1;
    //未执行
    public final int NOT_RUN = 0;
    /**------------用户禁用状态定义-----------------**/
    //未禁用
    public final int USER_ENABLE = 0;
    //已禁用
    public final int USER_DISABLE = 1;
    /**------------目录节点是否最后一级定义-----------------**/
    //否
    public final int NOT_LAST = 0;
    //是
    public final int IS_LAST = 1;

    /**------------每页显示用例条数定义-----------------**/
    public final int PAGE_SIZE = 5;
    /**------------测试用例模板记录条数定义-----------------**/
    public final int RECORD_NUM = 5;

    /**------------定义用例执行方式----------------------**/
    //手工执行
    public final int BY_HAND = 1;
    //自动化测试脚本执行
    public final int BY_AUTO = 2;

    /**------------jenkins 访问网址和用户名密码定义--------**/
    //访问的用户名
    public final String URL = "http://localhost:9797/jenkins/";
    //jenkin用户名
    public final String USERNAME = "admin";
    //jenkins密码
    public final String PASSWORD = "admin";

}
