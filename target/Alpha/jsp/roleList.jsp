
<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/7/21
  Time: 14:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>角色管理</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <script language="JavaScript" src="<%=request.getContextPath()%>
  /js/jquery-3.2.1.js"></script>
  <script type="text/javascript">

      //删除角色
      function delRole(id) {
          //弹出框
          var isDel = confirm("您确定要删除该角色吗？\n \n删除后该角色下的所有用户将都不可用！");
          //判断
          if (isDel){
              window.location.href='delRole.action?rid='+id;
          }else{
              return isDel;
          }
      }

      //查看角色详情
      function roleDetail(id) {
          //执行角色详情action
          window.location.href='roleDetail.action?rid='+id;
      }

      //添加角色
      function addRole(){
          //跳转到角色添加页面
          window.location.href='<%=request.getContextPath()%>/jsp/addRole.jsp';
      }

  </script>
</head>
<body class="bodyTextStyle">
  <a href="#" class="aStyle">角色管理</a>
  <div class="context">
    <!--当用户的权限是管理员时，才可以添加角色-->
    <s:if test="#session.userRole == 1">
      <!--添加按钮相关行-->
      <div class="textMar" >
        <input type="button" class="btnStyle" value="添加角色" style="margin-left:350px" onclick="addRole()">
      </div>
    </s:if>
    <!--角色列表相关-->
    <div class="textMar">
      <s:if test="roles.size() > 0">
        <table border="0" cellpadding="0" cellspacing="0">
          <tr>
            <th>ID</th>
            <th>角色名称</th>
            <th>角色描述</th>
            <th>创建时间</th>
            <th>修改时间</th>
            <!--当用户的权限是管理员时,可以查看角色详情和删除角色-->
            <s:if test="#session.userRole == 1">
              <th colspan="2">操作</th>
            </s:if>
            <!--其他用户只能查看角色详情-->
            <s:else>
              <th>操作</th>
            </s:else>
          </tr>

          <s:iterator value="roles" var="roleList">
            <tr>
              <td><s:property value="roleId" /> </td>
              <td><s:property value="roleName" /></td>
              <!--判断描述是否为空-->
              <s:if test="roleDesc != ''">
                <td><s:property value="roleDesc" /></td>
              </s:if>
              <s:else>
                <td>暂无描述</td>
              </s:else>
              <td><s:property value="createTime" /></td>
              <td><s:property value="updateTime" /></td>
              <!--当用户的权限是管理员时,可以查看角色详情和删除角色-->
              <s:if test="#session.userRole == 1">
                <td><input type="button" value="详   情" class="btnStyle" onclick="roleDetail(<s:property value="roleId"/>)"></td>
                <td><input type="button" value="删   除" class="btnStyle" onclick="delRole(<s:property value="roleId"/>)"></td>
              </s:if>
              <!--其他用户只能查看角色详情-->
              <s:else>
                <td><input type="button" value="详   情" class="btnStyle" onclick="roleDetail(<s:property value="roleId"/>)"></td>
              </s:else>
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
