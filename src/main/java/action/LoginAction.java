package action;

import bean.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import common.CommonParam;
import service.LoginService;
import serviceImpl.LoginServiceImpl;
import java.util.Map;

/**
 *
 * Created by sofronie on 2017/7/21.
 */
public class LoginAction extends ActionSupport{


    //定义User对象
    private User user;
    //定义loginService对象
    private LoginService loginService = new LoginServiceImpl();

    /**
     * 登录验证
     * @return String
     */
    public String userLogin(){
        String result = "";
        //定义用户列表
        User loginUser = new User();
        loginUser = loginService.getUserByUserName(user);
        if(loginUser != null) {
            if (user.getUsername().equals(loginUser.getUsername())) {
                if (user.getPassword().equals(loginUser.getPassword())) {   //如果用户名和密码一致
                    if (loginUser.getIsDelete() == CommonParam.USER_ENABLE) {
                        //获取session
                        ActionContext actionContext = ActionContext.getContext();
                        Map session = actionContext.getSession();
                        //保存信息到session
                        session.put("userId", loginUser.getUserId());
                        session.put("userName", loginUser.getUsername());
                        session.put("userRole", loginUser.getRoleId());

                        result = SUCCESS;
                        return result;
                    } else {
                        addFieldError("loginFail", "用户已禁用，请联系管理员");
                        result = "failure";
                        return result;
                    }
                } else {
                    addFieldError("loginFail", "用户名和密码不对应，请检查");
                    result = "failure";
                    return result;
                }
            }
        }else {
            addFieldError("loginFail", "该用户不存在，请联系管理员");
            result = "failure";
            return result;
        }

        return result;

    }

/**---------------------------getter and setter-------------------------*/
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }
}

