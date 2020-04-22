<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/8/9
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css">
  <%--<link rel="stylesheet" href="/resources/demos/style.css">--%>
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
  <script>

      var i = <%=request.getParameter("tabId")%>;
      //点击不同tab
      $( function() {
          $( "#tabs" ).tabs({active:i});
      } );
      <!--创建任务-->
      function createTask(){
          window.location.href='goCreateTask.action?flag=add'
      }
      <!--发布任务-->
      function releaseTask(taskId){
          window.location.href="releaseTask.action?taskId="+taskId;
      }

      <!--查看任务详情-->
      function taskDetail(taskId){
          window.location.href="taskDetail.action?taskId="+taskId;
      }
//      <!--查询任务-->
//      function selectTask(){
//          $('#preForm')
//      }
  </script>
</head>
<body>
  <!--面包屑-->
  <a href="#" class="aStyle">任务管理</a>
  <%--<div style="width:85%;margin-top: 4%">--%>
    <div id="tabs">
      <!--tabs-->
      <ul>
        <li style="width:30%;margin-left: 30px"><a href="#tabs-1" style="margin-left: 40%">未&nbsp;开&nbsp;始</a></li>
        <li style="width:30%"><a href="#tabs-2" style="margin-left: 40%">进&nbsp;行&nbsp;中</a></li>
        <li style="width:30%"><a href="#tabs-3" style="margin-left: 40%">已&nbsp;结&nbsp;束</a></li>
      </ul>
      <!--未开始tab下的内容-->
      <div id="tabs-1">
        <div class="context" style="margin-left: 10%">
          <s:form theme="simple" id="preForm" method="post" action="adminTaskList">
            <!--查询项目-->
            <div class="textMar" >
              <s:textfield class="textStyle" style="width:50px" name="task.search_taskId" placeholder="任务ID" value=""/>
              <s:textfield class="textStyle" name="task.search_name" style="margin-right:2%" placeholder="任务名称"/>
              <select name="task.search_status">
                <option value="0">请选择状态</option>
                <option value="1">未发布</option>
                <option value="2">已发布</option>
              </select>
              <s:hidden name="tabId" value="0"/>
              <s:submit class="btnStyle" value="检   索" style="margin-left:30px" />
              <!--创建任务-->
              <input type="button" class="btnStyle" value="创建任务" style="margin-left:50px" onclick="createTask()">
            </div>
            <div class="textMar">
              <!--未开始任务列表不为空-->
              <s:if test="tasks.size() > 0">
                <table border="0" cellpadding="0" cellspacing="0" width="650px">
                  <tr>
                    <th>ID</th>
                    <th>任务名称</th>
                    <th>任务描述</th>
                    <th>创建者</th>
                    <th>执行者</th>
                    <th>状态</th>
                    <th colspan="2">操作</th>
                  </tr>
                  <s:iterator value="tasks" var="taskList">
                    <tr>
                      <td><s:property value="taskId" /></td>
                      <td><s:property value="taskName" /> </td>
                      <td style="width:30%;"><s:property value="taskDesc" /></td>
                      <td><s:property value="creatorName" /></td>
                      <td><s:property value="executorName" /></td>
                      <s:if test="#taskList.status == 1">
                        <td>未发布</td>
                        <td><input type="button" id="releaseBtn" value="发   布" class="btnStyle" onclick="releaseTask(<s:property value='#taskList.taskId'/>)"></td>
                        <td><input type="button" value="详   情" class="btnStyle" onclick="taskDetail(<s:property value='#taskList.taskId'/>)"></td>
                      </s:if>
                      <s:if test="#taskList.status == 2">
                        <td>已发布</td>
                        <td colspan="2"><input type="button" value="详   情" class="btnStyle" onclick="taskDetail(<s:property value='#taskList.taskId'/>)"></td>
                      </s:if>
                    </tr>
                  </s:iterator>
              </table>
              </s:if>
              <s:else>
                <div style="text-align: center">暂无数据……</div>
              </s:else>
          </div>
          </s:form>
      </div>
    </div>
      <div id="tabs-2">
      <div class="context" style="margin-left: 10%">
        <s:form theme="simple" method="post" action="adminTaskList">
          <div class="textMar">
            <s:textfield class="textStyle" style="width:50px" name="task.search_taskId" placeholder="任务ID" value=""/>
            <s:textfield class="textStyle" name="task.search_name" style="margin-right:2%" placeholder="任务名称"/>
            <select name="task.search_status">
              <option value="0">请选择状态</option>
              <option value="3">已接受</option>
              <option value="5">进行中</option>
            </select>
            <s:hidden name="tabId" value="1"/>
            <s:submit class="btnStyle" value="检   索" style="margin-left:30px" />
          </div>
          <div class="textMar">
            <s:if test="runtasks.size() >0">
              <table border="0" cellpadding="0" cellspacing="0" width="650px">
                <tr>
                  <th>ID</th>
                  <th>任务名称</th>
                  <th>任务描述</th>
                  <th>创建者</th>
                  <th>执行者</th>
                  <th>状态</th>
                  <th>执行进度</th>
                  <th>操作</th>
                </tr>
                <s:iterator value="runtasks" var="runtasks">
                  <tr>
                    <td><s:property value="taskId" /></td>
                    <td><s:property value="taskName" /> </td>
                    <td style="width:30%;"><s:property value="taskDesc" /></td>
                    <td><s:property value="creatorName" /></td>
                    <td><s:property value="executorName" /></td>
                    <s:if test="#runtasks.status == 3">
                      <td>已接受</td>
                      <td>0%</td>
                      <td><input type="button" value="详   情" class="btnStyle" onclick="taskDetail(<s:property value='#runtasks.taskId'/>)"></td>
                    </s:if>
                    <s:if test="#runtasks.status == 5">
                      <td>进行中</td>
                      <td><s:property value="precent" /></td>
                      <td><input type="button" value="详   情" class="btnStyle" onclick="taskDetail(<s:property value='#runtasks.taskId'/>)"></td>
                    </s:if>
                  </tr>
                </s:iterator>
              </table>
            </s:if>
            <s:else>
              <div style="text-align: center">暂无数据……</div>
            </s:else>
          </div>
        </s:form>
      </div>
    </div>
      <div id="tabs-3">
      <div class="context" style="margin-left: 10%">
        <s:form theme="simple" method="post" action="adminTaskList">
          <div class="textMar" >
            <s:textfield class="textStyle" style="width:50px" name="task.search_taskId" placeholder="任务ID" value=""/>
            <s:textfield class="textStyle" name="task.search_name" style="margin-right:2%" placeholder="任务名称"/>
            <select name="task.search_status">
              <option value="0">请选择状态</option>
              <option value="4">已拒绝</option>
              <option value="6">已完成</option>
            </select>
            <s:hidden name="tabId" value="2"/>
            <s:submit class="btnStyle" value="检   索" style="margin-left:30px" />
          </div>
          <div class="textMar">
            <s:if test="overtasks.size() >0">
              <table border="0" cellpadding="0" cellspacing="0" width="650px">
                <tr>
                  <th>ID</th>
                  <th>任务名称</th>
                  <th>任务描述</th>
                  <th>创建者</th>
                  <th>执行者</th>
                  <th>状态</th>
                  <th>操作</th>
                </tr>
                <s:iterator value="overtasks" var="overtasks">
                <tr>
                  <td><s:property value="taskId" /></td>
                  <td><s:property value="taskName" /> </td>
                  <td style="width:30%;"><s:property value="taskDesc" /></td>
                  <td><s:property value="creatorName" /></td>
                  <td><s:property value="executorName" /></td>
                  <s:if test="#overtasks.status == 6">
                    <td>已完成</td>
                  </s:if>
                  <s:if test="#overtasks.status == 4">
                    <td>已拒绝</td>
                  </s:if>
                  <td><input type="button" value="详   情" class="btnStyle" onclick="taskDetail(<s:property value='#overtasks.taskId' />)"></td>
                </tr>
                </s:iterator>
              </table>
            </s:if>
            <s:else>
              <div style="text-align: center">暂无数据……</div>
            </s:else>
          </div>
        </s:form>
    </div>
  </div>
</div>
</body>
</html>

