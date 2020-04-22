
<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/7/25
  Time: 19:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>添加用户</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
  <script type="text/javascript">
    $(document).ready(function(){
        $('#saveBtn').click(function(){

            $('.errorMsg').html(" ");
            //获取输入框和下拉列表框的值
            var username = $('#username').val();
            var password = $('#password').val();
            var phone = $('#phone').val();
            var role = $('#role').val();

            //验证用户名
            if(username == "" || username.length == 0){
                $('#username').after("<label class='errorMsg'>*用户名不可为空</label>");
                return false;
            }else{
                if(username.length <2 || username.length > 20){
                    $('#username').after("<label class='errorMsg'>*用户名不可小于2位字符，不可大于20位字符</label>");
                    return false;
                }
            }
            //验证密码
            if(password =="" || password.length == 0){
                $('#password').after("<label class='errorMsg'>*密码不可为空</label>");
                return false;
            }else{
                if(password.length <2 || password.length > 16){
                    $('#password').after("<label class='errorMsg'>*密码不可小于2位字符，不可大于16位字符</label>");
                    return false;
                }
            }
            //验证手机号
            if(! (phone =="") || !(phone.length == 0)){
              if(!(/^1(3|4|5|7|8)\d{9}$/.test(phone))){
                  $('#phone').after("<label class='errorMsg'>*手机号格式有误，请检查！</label>");
                  return false;
              }
            }
            //验证角色
            if(role == 0){
                $('#role').after("<label class='errorMsg'>*角色不可为空,请选择角色</label>");
                return false;
            }
            //提交表单
            $('#userForm').attr("action","addUser");
            $('#userForm').submit();

        })
    })
  </script>
</head>
<body class="bodyTextStyle">
  <!--面包屑-->
  <a href="user.action" class="aStyle">用户管理</a><label>></label><a href="#" class="aStyle">添加用户</a>
  <!--添加用户-->
  <div class="context">
    <s:form id="userForm" theme="simple" method="post">
      <div class="textMar">
        <label>用&nbsp;&nbsp;户&nbsp;名：</label>
        <s:textfield id="username" name="user.username" class="textStyle" placeholder="请输入用户名" />
      </div>
      <div class="textMar">
        <label>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
        <s:password id="password" name="user.password" class="textStyle" placeholder="请输入密码" />
      </div>
      <div class="textMar">
        <label>联系方式：</label>
        <s:textfield id="phone" name="user.phone" class="textStyle" placeholder="请输入手机号" />
      </div>
      <div class="textMar">
        <label>角&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色：</label>
        <s:select id="role" list="roleset" listKey="key" listvalue="value" headerKey="0" headerValue="请选择角色" name="user.roleId"/>
      </div>
      <div class="textMar">
      <input type="button" value="取   消" class="btnStyle" style="margin-right:30px" onclick="window.location.href = 'user.action'">
      <input type="button"  id="saveBtn" value="保   存" class="btnStyle" />
    </div>
    </s:form>
  </div>
</body>
</html>
