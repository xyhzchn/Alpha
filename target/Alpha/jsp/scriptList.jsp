<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/12/11
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
  <meta charset="UTF-8">
  <title>脚本管理</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <script language="JavaScript" src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script type="text/javascript">
    //删除自动化脚本
    function deleteScript(scriptId) {
        //弹出框
        var isDel = confirm("确定要删除脚本"+scriptId+"吗？");
        //点击确定按钮
        if(isDel){
            window.location.href = "deleteScript.action?scriptId="+scriptId;
        }else{
            return isDel;
        }

    }
    //查看脚本详情
    function scriptDetail(scriptId) {
        window.location.href = "scriptDetail.action?scriptId="+scriptId;
    }

    //添加脚本
    function addScript(){
      window.location.href="<%=request.getContextPath()%>/jsp/addScript.jsp";
    }
  </script>
</head>
  <body class="bodyTextStyle">
    <a href="#" class="aStyle">脚本管理</a>
    <div class="context">
      <s:form theme="simple" method="post" action="script">
          <div class="textMar">
            <!--创建脚本-->
            <input type="button" class="btnStyle" value="创建脚本" style="margin-left:480px" onclick="addScript()">
          </div>
          <!--查询项目-->
          <div class="textMar" >
            <s:textfield class="textStyle" style="width:50px" name="script.searchId" placeholder="ID" value=""/>
            <s:textfield class="textStyle" name="script.searchName" style="margin-right:2%" placeholder="自动化测试脚本名称"/>
            <s:if test="#session.userRole == 1">
              <s:select list="creatorList" listKey="key" listValue="value" name="script.searchCreator" headerKey="0" headerValue="请选择创建者"/>
          </div>
              <div class="textMar">
                <s:submit class="btnStyle" value="检   索" style="margin-left:480px"/>
              </div>
            </s:if>
            <s:else>
              <s:submit class="btnStyle" value="检   索" style="margin-left:40px"/>
          </div>
            </s:else>

          <!--脚本列表相关-->
          <div class="textMar">
            <!--脚本列表记录大于0-->
            <s:if test="scripts.size() >0">
              <table border="0" cellpadding="0" cellspacing="0" >
                <tr>
                  <th>ID</th>
                  <th>自动化测试脚本名称</th>
                  <th>脚本路径</th>
                  <th>脚本描述</th>
                  <!--管理员权限下列表显示创建者信息-->
                  <s:if test="#session.userRole == 1">
                    <th>创建者</th>
                  </s:if>
                  <th colspan="2">操作</th>
                </tr>
                <s:iterator value="scripts" var="scriptList">
                  <tr>
                    <td><s:property value="id"/> </td>
                    <td><s:property value="name"/></td>
                    <td><s:property value="location"/></td>
                    <td><s:property value="scriptDesc"/></td>
                    <!--管理员权限下列表显示创建者名字-->
                    <s:if test="#session.userRole == 1">
                      <td><s:property value="creatorName"/></td>
                    </s:if>
                    <td><input type="button" value="详   情" class="btnStyle" onclick="scriptDetail(<s:property value="id"/>)"></td>
                    <td><input type="button" value="删   除" class="btnStyle" onclick="deleteScript(<s:property value="id"/>)"></td>
                  </tr>
                </s:iterator>
              </table>
            </s:if>
            <!--脚本列表为空-->
            <s:else>
              <div style="text-align: center">暂无数据……</div>
            </s:else>
          </div>
      </s:form>
    </div>
  </body>
</html>

