<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/7/22
  Time: 11:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>添加角色</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script type="text/javascript">
      //编辑角色
      function edit(btnValue) {
          //判断当前按钮的值

          if(btnValue == "修   改"){
              //角色名称和角色描述变为可编辑
              $('#roleNameDetail').removeAttr('disabled');
              $('#roleNameDetail').attr("style","border:1");
              $('#roleDescDetail').removeAttr('disabled');
              $('#roleDescDetail').attr("style","border:1");
              //修改按钮为 保存
              $('#editBtn').attr('value',"保   存");
          }else if(btnValue == "保   存"){ //按钮值为保存
              //角色名称
              var roleName = $("#roleNameDetail").val();
              //角色名称为空
              if(roleName.length == 0){
                alert("角色名称不可为空");
              }else{
                  //修改角色
                  $.ajax({
                      type:'post',
                      url:'editRole.action',
                      data:{
                          'role.roleId':document.getElementById("roleId").innerText,
                          'role.roleName':roleName,
                          'role.roleDesc':$("#roleDescDetail").val()
                      },
                      dataType:'text',
                      success:function(msg){
                        if(msg == 'success'){
                            window.location.href = "role.action";
                        }
                      },
                      error:function(msg){
                          if(msg == 'failure'){
                              window.location.href = "/jsp/error.jsp";
                          }
                      }
                  });
                  //角色名称和角色描述变为不可编辑
                  $("#roleNameDetail").attr("disabled","true");
                  $("#roleDescDetail").attr("disabled","true");
              }
          }
      }
  </script>
</head>

<body class="bodyTextStyle">
  <!--面包屑-->
  <a href="role.action" class="aStyle">用户管理</a><label>>
    <!--查看角色详情时-->
  <s:if test="flag=='detail'">
    </label><a href="#" class="aStyle">角色详情</a>
  </s:if>
    <!--添加角色时-->
  <s:else>
    </label><a href="#" class="aStyle">添加角色</a>
  </s:else>
  <div class="context">
    <s:form id="roleForm" theme="simple" method="post" action="addRole">
      <!--查看角色详情时-->
      <s:if test="flag=='detail'">
          <div class="textMar">
            <label>角&nbsp;色&nbsp;ID：</label>
            <s:label id="roleId" class="textStyle" style="border:0" name="role.roleId" />
          </div>
          <div class="textMar">
            <label>角色名称：</label>
            <s:textfield id="roleNameDetail" style="border:0" class="textStyle" name="role.roleName" disabled="true"/>
          </div>
          <div class="textMar">
            <label>角色描述：</label><br>
            <s:textarea id="roleDescDetail" style="border:0" class="textareaStyle" name="role.roleDesc" disabled="true"/>
          </div>
      </s:if>
      <!--角色添加时-->
      <s:else>
        <div class="textMar">
          <label>角色名称：</label>
          <s:textfield id="roleName" placeholder="请输入角色名称" class="textStyle" name="role.roleName" />
          <s:fielderror style="color:red" name="roleNameIsNull" />
        </div>
        <div class="textMar">
          <label>角色描述：</label><br>
          <s:textarea id="roleDesc" placeholder="请输入角色描述信息" class="textareaStyle" name="role.roleDesc"/>
        </div>
      </s:else>
      <!--按钮显示-->
        <div class="textMar" style="margin-left:60px">
          <input type="button" value="取   消" class="btnStyle" style="margin-right:30px" onclick="window.location.href = 'role.action'">
          <!--当用户的权限是管理员时,可以修改角色-->
            <s:if test="#session.userRole == 1">
                <!--角色详情时，显示修改按钮-->
                <s:if test="flag=='detail'">
                <input type="button" id="editBtn" value="修   改" class="btnStyle" onclick="edit(this.value)"/>
                </s:if>

                <!--角色添加，提交表单-->
                <s:else>
                  <s:submit value="保   存" class="btnStyle"/>
                </s:else>
          </s:if>
        <div>
    </s:form>
  </div>
</body>
</html>
