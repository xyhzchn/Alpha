<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
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
  <style type="text/css">
    .theme-popover-mask {
      position:fixed;
      top:0;
      left:0;
      width:100%;
      height:100%;
      background:#000;
      opacity:0.4;
      filter:alpha(opacity=40);
      display:none
    }
    .theme-popover {
      position:fixed;
      top:50%;
      left:40%;
      width:400px;
      height:300px;
      margin:-180px 0 0 -30px;
      border-radius:5px;
      border:solid 1px #666;
      background-color:#fff;
      display:none;
      box-shadow: 0 0 10px #666;
    }
    .theme-poptit {
      border-bottom:1px solid #ddd;
      padding:12px;
      position: relative;
    }
    .theme-popbod {
      padding:60px 15px;
      color:#444;
      height: 148px;
    }

    .theme-poptit .close {
      float:right;
      color:#999;
      margin:-2px -5px -5px;
      font:bold 14px/14px simsun;
      text-shadow:0 1px 0 #ddd
    }
    .theme-poptit .close:hover {
      color:#444;
    }

    .dform {
      padding-top: 5%;
      margin-left: 10%;
    }
  </style>
  <script>

      var i = <%=request.getParameter("tabId")%>;

      $(function(){
          $( "#tabs" ).tabs({active:i});
      });

      window.onload = function () {
        var tabid = location.href.hash;
        if(tabid && tabid.length > 0){
          var tab = document.querySelector(tabid);
          tab.onclick();
        }
      }
      /**
       * 接受任务
       * @param taskId
       */
      function acceptTask(taskId){
         window.location.href='acceptTask.action?taskId='+taskId;
      }
      /**
       * 执行任务
       * @param taskId
       */
      function runningTask(taskId){
          window.location.href="runningTask.action?taskId="+taskId+"&flag=run";
      }

      <!--弹出框事件定义-->
      function alertTaskReason(id){
          $("#hidden_taskId").attr("value",id);
          $('.theme-popover-mask').fadeIn(100);
          $('.theme-popover').slideDown(200);
      }
      function closeTaskReason(){
          $('.theme-popover-mask').fadeOut(100);
          $('.theme-popover').slideUp(200);
      }
      /**
       * 查看任务详情
       * @param taskId
       */
      function taskDetail(taskId){
          window.location.href="taskDetail.action?taskId="+taskId;
      }
  </script>
</head>
<body class="bodyTextStyle">
<div style="width:85%">
  <div id="tabs">
    <ul>
      <li style="width:30%;margin-left: 30px"><a href="#tabs-1" style="margin-left: 40%">未&nbsp;开&nbsp;始</a></li>
      <li style="width:30%"><a href="#tabs-2" style="margin-left: 40%">进&nbsp;行&nbsp;中</a></li>
      <li style="width:30%"><a href="#tabs-3" style="margin-left: 40%">已&nbsp;结&nbsp;束</a></li>
    </ul>
    <div id="tabs-1">
      <div class="context" style="margin-left: 10%">
        <s:form theme="simple" method="post" action="testerTaskList">
          <!--查询项目-->
          <div class="textMar" >
            <s:textfield class="textStyle" style="width:50px" name="task.search_taskId" placeholder="任务ID" value=""/>
            <s:textfield class="textStyle" name="task.search_name" placeholder="任务名称"/>
            <s:hidden name="tabId" value="0"/>
            <s:submit class="btnStyle" value="检   索" style="margin-left:30px" />
          </div>
          <div class="textMar">
              <!--未开始任务列表为空-->
              <s:if test="tasks.size() >0">
              <table border="0" cellpadding="0" cellspacing="0" width="650px">
                <tr>
                  <th>ID</th>
                  <th>任务名称</th>
                  <th>任务描述</th>
                  <th>创建者</th>
                  <th colspan="2">操作</th>
                </tr>
                <s:iterator value="tasks" var="taskList">
                  <tr>
                    <td><s:property value="taskId" /></td>
                    <td><s:property value="taskName" /></td>
                    <td style="width:30%;"><s:property value="taskDesc" /></td>
                    <td><s:property value="creatorName" /></td>
                    <td><input type="button" value="接   受" class="btnStyle" onclick="acceptTask(<s:property value="#taskList.taskId"/>)"></td>
                    <td><input type="button" value="拒   绝" class="btnStyle" onclick="alertTaskReason(<s:property value="taskId"/>)"></td>
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
        <s:form theme="simple" method="post" action="testerTaskList">
          <div class="textMar" >
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
                  <th>状态</th>
                  <th>执行进度</th>
                  <th>操作</th>
                </tr>
                <s:iterator value="runtasks" var="runtasks">
                  <tr>
                    <td><s:property value="taskId" /></td>
                    <td><s:property value="taskName" /></td>
                    <td style="width:30%;"><s:property value="taskDesc" /></td>
                    <td><s:property value="creatorName" /></td>
                    <s:if test="#runtasks.status == 3">
                      <td>已接受</td>
                      <td><s:property value="precent"/></td>
                      <td><input type="button" value="开始执行" class="btnStyle" onclick="runningTask(<s:property value="#runtasks.taskId"/>)"></td>
                    </s:if>
                    <s:if test="#runtasks.status == 5">
                      <td>进行中</td>
                      <td><s:property value="precent"/></td>
                      <td><input type="button" value="继续执行" class="btnStyle" onclick="runningTask(<s:property value="#runtasks.taskId" />)"></td>
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
        <s:form theme="simple" method="post" action="testerTaskList">
          <div class="textMar" >
            <s:textfield class="textStyle" style="width:50px" name="task.search_taskId" placeholder="任务ID" value=""/>
            <s:textfield class="textStyle" name="task.search_name" style="margin-right:2%" placeholder="任务名称"/>
            <select name="task.search_status">
              <option value="0">请选择状态</option>
              <option value="6">已完成</option>
              <option value="4">已拒绝</option>
            </select>
            <s:hidden name="tabId" value="2"/>
            <s:submit class="btnStyle" value="检   索" style="margin-left:30px" />
          </div>
          <div class="textMar">
            <s:if test="overtasks.size() > 0">
              <table border="0" cellpadding="0" cellspacing="0" width="650px">
                <tr>
                  <th>ID</th>
                  <th>任务名称</th>
                  <th>任务描述</th>
                  <th>创建者</th>
                  <th>状态</th>
                  <th>操作</th>
                </tr>
                <s:iterator value="overtasks" var="overtasks">
                  <tr>
                    <td><s:property value="taskId" /></td>
                    <td><s:property value="taskName" /></td>
                    <td style="width:30%;"><s:property value="taskDesc" /></td>
                    <td><s:property value="creatorName" /></td>
                    <s:if test="#overtasks.status == 6">
                      <td>已完成</td>
                      <td><input type="button" value="详   情" class="btnStyle" onclick="taskDetail(<s:property value='#overtasks.taskId'/>)"></td>
                    </s:if>
                    <s:if test="#overtasks.status == 4">
                      <td>已拒绝</td>
                      <td><input type="button" value="详   情" class="btnStyle" onclick="taskDetail(<s:property value='#overtasks.taskId'/>)"></td>
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
  </div>
</div>
  <!--拒绝理由，因为位于iframe。不能遮到上级页面-->
  <div style="width: 100%;height: 100%;">
    <div class="theme-popover-mask"></div>
    <div class="theme-popover">
      <div class="theme-poptit">
        <a href="javascript:;" title="关闭" class="close" onclick="closeTaskReason()">×</a>
      </div>
      <div class="theme-popbod dform">
        <s:form name="form1" theme="simple" method="POST" action="refuseTask">
          <!--任务id-->
          <label>任务ID：</label>
          <input type="text" id="hidden_taskId" name="task.taskId" readonly value="">

          <div class="textMar">
            <label>拒绝理由：</label><br/>
            <s:textarea name="task.reason" class="textareaStyle" placeholder="请输入拒绝理由"/>
          </div>
          <div class="textMar">
            <s:submit id="save" style="margin-left:60%" class="btnStyle" value="保   存" />
          </div>
        </s:form>
      </div>
  </div>
</div>
</body>
</html>

