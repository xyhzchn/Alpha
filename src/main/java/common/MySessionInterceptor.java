package common;

import action.LoginAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.Action;
import java.util.Map;

public class MySessionInterceptor extends AbstractInterceptor{

    public String intercept(ActionInvocation ai) throws Exception {
        //获取上下文
        ActionContext context = ai.getInvocationContext();
        //获取session
        Map session = context.getSession();
        //获取action
        Action action =(Action)ai.getAction();

        if(action instanceof LoginAction){
            return ai.invoke();
        }

        String username = (String)session.get("userName");

        if(username == null){
            return Action.LOGIN;
        }else {
            return  ai.invoke();
        }
    }
}
