<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/12/11
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
    <meta charset="UTF-8">
    <title>添加脚本</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
    <script language="JavaScript" src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
    <script type="text/javascript">
      $(document).ready(function(){
          //验证用户名和密码
          $('#scriptForm').validate({
              rules:{
                  "script.name":{required:true},
                  "script.location":{required:true}
              },
              messages:{
                  "script.name":{
                      required:"脚本名称不可为空"
                  },
                  "script.location":{
                      required:"脚本路径不可为空"
                  }
              }
          });
      });

      //修改脚本
      function updateScript(btnValue) {
          if(btnValue == '修   改') {
              //脚本名称、路径、描述可编辑
              $('#scriptName').removeAttr("disabled");
              $('#scriptName').attr("style","border:1");
              $('#scriptLacation').removeAttr("disabled");
              $('#scriptLacation').attr("style","border:1");
              $('#scriptDesc').removeAttr("disabled");
              $('#scriptDesc').attr("style","border:1");
              //修改按钮为：保存
              //$('#editBtn').attr("value", "保   存");
              $('#editBtn').hide();
              $('#saveBtn').show();
          }
      }

    </script>
  </head>

  <body class="bodyTextStyle">
    <!--面包屑-->
    <a href="script.action" class="aStyle">脚本管理</a>>
    <s:if test="flag=='detail'">
      <a href="#" class="aStyle">脚本详情</a>
    </s:if>
    <s:else>
      <a href="#" class="aStyle">创建脚本</a>
    </s:else>
    <div class="context">
      <s:form id="scriptForm" theme="simple" method="post" action="addScript">
        <!--脚本详情页显示-->
        <s:if test="flag=='detail'">

          <div class="textMar">
            <label>脚本ID：</label>
            <s:label name="script.id"/>
            <s:hidden name="script.id" />
          </div>

          <div class="textMar">
            <label>脚本名称：</label>
            <s:textfield id="scriptName" style="border:0" name ="script.name" class="textStyle" disabled="true"/>
          </div>

          <div class="textMar">
            <label>脚本路径：</label>
            <s:textfield id="scriptLacation" name="script.location" class="textStyle" style="width:250px;border:0" disabled="true"/>
          </div>

          <div class="textMar">
            <label>脚本描述：</label><br>
            <s:textarea id="scriptDesc" style="border:0" name="script.scriptDesc" class="textareaStyle" disabled="true"/>
          </div>
          <!--管理员权限显示脚本创建者-->
          <s:if test="#session.userRole == 1">
            <div class="textMar">
              <label>脚本创建者：</label>
              <s:label name="script.creatorName"/>
            </div>
          </s:if>
          <!--按钮显示-->
          <div class="textMar" style="margin-left:60px">
            <input type="button" value="取   消" class="btnStyle" style="margin-right:30px" onclick="history.back();">
            <input type="button" value="修   改" id="editBtn" class="btnStyle" onclick="updateScript(this.value)"/>
            <s:submit id="saveBtn" value="保  存" class="btnStyle" style="display:none;" onclick="this.form.action='updateScript.action'"/>
          </div>
        </s:if>
        <!--脚本添加页显示-->
        <s:else>
            <div class="textMar">
              <label>脚本名称：</label>
              <s:textfield name="script.name" placeholder="与用例名一致" class="textStyle" />
            </div>

            <div class="textMar">
              <label>脚本路径：</label>
              <s:textfield name="script.location" placeholder="请输入脚本路径" class="textStyle" style="width:250px;"/>
            </div>

            <div class="textMar">
              <label>脚本描述：</label><br>
              <s:textarea id="roleDesc" name="script.scriptDesc" placeholder="请输入脚本描述" class="textareaStyle" style="width:250px;"/>
            </div>
            <!--按钮显示-->
            <div class="textMar" style="margin-left:60px">
              <input type="button" value="取   消" class="btnStyle" style="margin-right:30px" onclick="history.back();">
              <s:submit value="保   存" class="btnStyle"/>
            </div>
        </s:else>
      </s:form>
    </div>
  </body>
</html>
