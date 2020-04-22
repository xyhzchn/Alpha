package serviceImpl;

import bean.User;
import dao.LoginDao;
import daoImpl.LoginDaoImpl;
import service.LoginService;

import java.util.List;

/**
 *
 * Created by sofronie on 2017/8/23.
 */
public class LoginServiceImpl implements LoginService {

    //定义注入
    private LoginDao loginDao = new LoginDaoImpl();

    /**
     * 获取登录用户信息
     * @param user 登录用户
     * @return 登录用户信息
     */
    public User getUserByUserName(User user){
       return loginDao.getUserByUserName(user);
    }

    /**------------------------------------getter and setter-------------------------*/
    public LoginDao getLoginDao() {
        return loginDao;
    }

    public void setLoginDao(LoginDao loginDao) {
        this.loginDao = loginDao;
    }
}
