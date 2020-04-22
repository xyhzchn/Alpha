package service;

import bean.User;

import java.util.List;

/**
 *
 * Created by sofronie on 2017/8/23.
 */
public interface LoginService {

    /**
     * 获取登录用户信息
     * @param user 登录用户
     * @return 登录用户信息
     */
    public User getUserByUserName(User user);
}
