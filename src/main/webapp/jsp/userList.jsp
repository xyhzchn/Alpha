<%@ page import="bean.User" %>
<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/7/21
  Time: 14:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>用户管理</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script type="text/javascript">
    //添加用户
    function addUser() {
        window.location.href='getRoleList.action'
    }
    //禁用用户
    function disableUser(id){
        window.location.href='userDisable.action?uid='+id;
    }
    //启用用户
    function enableUser(id){
        window.location.href='userEnable.action?uid='+id;
    }
  </script>
</head>
<body class="bodyTextStyle">
  <!--面包屑-->
  <a href="#" class="aStyle">用户管理</a>
  <div class="context">
    <!--当用户的权限是管理员时，才可以添加用户-->
    <s:if test="#session.userRole == 1">
      <!--添加用户-->
      <div class="textMar" >
        <input type="button" class="btnStyle" value="添加用户" style="margin-left:360px" onclick="addUser()">
      </div>
    </s:if>
    <!--用户列表-->
    <div class="textMar">
      <s:if test="users.size() > 0">
        <table border="0" cellpadding="0" cellspacing="0">
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>角色</th>
            <th>联系方式</th>
            <th>创建日期</th>
            <th>修改日期</th>
            <!--当用户的权限是管理员时，才可以启用或者禁用用户-->
            <s:if test="#session.userRole == 1">
              <th>操作</th>
            </s:if>
          </tr>

          <s:iterator value="users" var="userList">
            <tr>
              <td><s:property value="userId" /></td>
              <td><s:property value="username" /></td>
              <td><s:property value="roleName" /></td>
              <s:if test="phone != ''">
                <td><s:property value="phone" /> </td>
              </s:if>
              <s:else>
                <td>暂无</td>
              </s:else>
              <td><s:property value="createTime" /> </td>
              <td><s:property value="updateTime" /> </td>
              <!--当用户的权限是管理员时，才可以启用或者禁用用户-->
              <s:if test="#session.userRole == 1">
                <s:if test="isDelete == 0">
                  <td><input type="button" value="禁   用" class="btnStyle" onclick="disableUser(<s:property value="userId"/>)"></td>
                </s:if>
                <s:if test="isDelete == 1">
                  <td><input type="button" value="启   用" class="btnStyle" onclick="enableUser(<s:property value="userId"/>)"></td>
                </s:if>
              </s:if>
            </tr>
          </s:iterator>
      </table>
      </s:if>
      <s:else>
        <div style="text-align: center">暂无数据……</div>
      </s:else>
    </div>
  </div>
</body>
</html>
