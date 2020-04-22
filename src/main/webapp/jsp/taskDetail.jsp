<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/8/14
  Time: 19:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>任务详情</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <script type="text/javascript">
    function editTask(taskId){
        window.location.href='goCreateTask.action?taskId='+taskId+'&flag=edit';
    }
  </script>
</head>

<body class="bodyTextStyle" style="text-align:left">
  <!--任务详情-->
  <a href="adminTaskList.action" class="aStyle">任务管理</a><label>></label><a href="#" class="aStyle">任务详情</a>
  <div class="context">
    <!--任务信息-->
    <div class="textMar">
            <label>任&nbsp;&nbsp;务&nbsp;ID：</label><label><s:property value="task.taskId"/></label></br>
            <label>任务名称：</label><label><s:property value="task.taskName"/></label></br>
            <label>任务描述：</label><label><s:property value="task.taskDesc"/></label></br>
            <label>执&nbsp;&nbsp;行&nbsp;者：</label><label><s:property value="task.executorName"/></label></br>
    </div>
    <!--任务包含的用例列表-->
    <div class="textMar">
      <label>用例列表：</label></br>
      <table border="1" cellpadding="0" cellspacing="0">
        <tr>
          <th style="width: 5%;">用例ID</th>
          <th style="width: 15%;">用例简述</th>
          <th style="width: 15%;">预置条件</th>
          <th style="width: 18%;">测试步骤</th>
          <th style="width: 18%;">预期结果</th>
          <th style="width: 6%;">优先级</th>
          <th style="width: 7%;">版本号</th>
          <th style="width: 8%;">执行结果</th>
          <th style="width: 8%;">脚本名称</th>
        </tr>
        <s:iterator value="task.caselist" var="caseList">
          <tr>
            <td><s:property value="project"/><span>_</span><s:property value="case_id" /> </td>
            <td><s:property value="caseTitle" /></td>
            <td><s:property value="precondition" /></td>
            <td><s:property value="testStep" /></td>
            <td><s:property value="expectedRes" /></td>
            <td><s:property value="priority" /></td>
            <td><s:property value="caseVersion" /></td>
            <s:if test="lastResult == 0" >

              <td>暂无</td>
            </s:if>
            <s:elseif test="lastResult == 1">
              <td style="color: green;">PASS</td>
            </s:elseif>
            <s:elseif test="lastResult == 2">
              <td style="color: red;">FAILURE</td>
            </s:elseif>
            <s:elseif test="lastResult == 3">
              <td style="color: blue;">BLOCK</td>
            </s:elseif>
            <td><s:property value="scriptName" /></td>
          </tr>
        </s:iterator>
      </table>
    </div>
    <!--拒绝理由-->
    <s:if test="task.status == 4">
      <div class="textMar">
        <label>拒绝理由：</label>
        <s:if test="task.reason != ''">
          <label><s:property value="task.reason" /> </label>
        </s:if>
        <s:else>
          <label>无理由</label>
        </s:else>
      </div>
    </s:if>
    <!--任务执行流程图-->
    <s:if test="task.status == 2">
    <div class="textMar">
      <label>流&nbsp;&nbsp;程&nbsp;图：</label><br>
      <img src="<%=request.getContextPath()%>/img/release.png" style="width:500px">
    </div>
  </s:if>
    <s:if test="task.status == 3">
      <div class="textMar">
        <label>流&nbsp;&nbsp;程&nbsp;图：</label><br>
        <img src="<%=request.getContextPath()%>/img/accepted.png" style="width:500px">
      </div>
    </s:if>
    <s:if test="task.status == 4">
      <div class="textMar">
        <label>流&nbsp;&nbsp;程&nbsp;图：</label><br>
        <img src="<%=request.getContextPath()%>/img/refused.png" style="width:300px">
      </div>
    </s:if>
    <s:if test="task.status == 5">
      <div class="textMar">
        <label>流&nbsp;&nbsp;程&nbsp;图：</label><br>
        <img src="<%=request.getContextPath()%>/img/running.png" style="width:500px">
      </div>
    </s:if>
    <s:if test="task.status == 6" >
      <div class="textMar">
        <label>流&nbsp;&nbsp;程&nbsp;图：</label><br>
        <img src="<%=request.getContextPath()%>/img/finished.png" style="width:500px">
      </div>
    </s:if>
    <!--底部按钮-->
    <div class="textMar" style="margin-left: 40%">
      <!--返回首页-->
      <input type="button" value="取   消" class="btnStyle" onclick="history.back()">
      <!--任务修改-->
      <s:if test="task.status == 1">
        <input type="button" value="修   改" class="btnStyle" onclick="editTask(<s:property value="task.taskId"/>)">
      </s:if>
    </div>
  </div>
</body>
</html>

