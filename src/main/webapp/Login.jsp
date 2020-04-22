<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>用户登录</title>
  <link rel="stylesheet" type="text/css" href="css/common.css">
  <script src="js/jquery-3.2.1.js"></script>
  <script src="js/jquery.validate.js"></script>
  <style type="text/css">
    #loginDiv{
      border: 1px solid #eee;
      width: 500px;
      height: 400px;
      margin: 200px auto;
      text-align: center;
      background-color: #FCFCFC;
      border-radius: 5px;
    }
    .loginTextfield{
      width: 200px;
      height: 30px;
      font-size: 13px;
      padding-left: 5px
    }

    #forgetPswd{
      text-align: right;
      margin: 5px 145px auto;
      font-size: xx-small;
    }

    .loginBtnStyle{
      width: 200px;
      height: 30px;
      border: 1px;
      border-color: #003d79;
      border-radius: 3px;
      background-color: #005AB5;
      color: #FFFFFF;
    }

    .error{
      color: #F00;
      padding-left: 16px;
      font-size: 12px;
    }
  </style>
  <script type="text/javascript">
      if (window != top){
          top.location.href = location.href;
      }
  </script>
</head>
<body style="background-color:#003d79">
    <s:form id="loginFrom" theme="simple" method="post" action="login">
      <div id="loginDiv">
        <div>
          <img src="<%=request.getContextPath()%>/img/logo.png" style="padding-top: 20px;">
        </div>
        <div style="padding-top: 10px;">
          <label style="text-align: center;color:#6C6C6C;">测试用例管理系统</label>
        </div>
        <div style="padding-top: 30px;">
          <s:textfield id="username" name ="user.username" placeholder="请输入用户名" class="loginTextfield"/>
        </div>
        <div style="padding-top: 30px;">
          <s:password id="password" name="user.password" placeholder="请输入密码" class="loginTextfield"/>
        </div>
        <div id="forgetPswd">
          <a href="#" style="text-align:right;color: #468ae3;" onclick="alert('修改密码，请联系管理员')">忘记密码？</a>
        </div>
        <s:fielderror style="color:red;font-size:12px;" name="loginFail"/>
        <div style="padding-top: 20px;">
          <s:submit class="loginBtnStyle" value="登   录"/>
        </div>
      </div>
    </s:form>

  <script type="text/javascript">
    $(document).ready(function(){
        //验证用户名和密码
        $('#loginFrom').validate({
            rules:{
                "user.username":{required:true},
                "user.password":{required:true}
                },
            messages:{
                "user.username":{
                    required:"*用户名不可为空"
                },
                "user.password":{
                    required:"*密码不可为空"
                }
            }
        })
    })

  </script>
</body>
</html>

