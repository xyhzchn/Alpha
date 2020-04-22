<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/8/10
  Time: 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <title>创建任务</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
</head>
<script type="text/javascript">
  //选择执行者时，将选择的值传递到隐藏域
  $(function(){
      $('#executorMap').change(function(){
          var checkText = $('#executorMap').find("option:selected").text();
          $('#executorName').attr("value",checkText);
      })
  });

  //数据验证并创建任务
  function saveTask(){

       //定义错误输出
       $('.errorMsg').html(" ");
       //获取任务名称和执行者id
       var taskNameVal = $('#taskName').val();
       var executorId = $('#executorMap').val();
       //如果任务名称为空
       if(taskNameVal == "" || taskNameVal.length == 0){
           $('#taskName').after("<label class='errorMsg'>*任务名称不可为空</label>");
           return false;
       }
       //如果执行者id = 0
       if(executorId == 0){
           $('#executorMap').after("<label class='errorMsg'> *请选择执行者</label>");
           return false;
       }
       //根据隐藏的任务id判断，点击保存时走的action
       if($('#hidden_taskId').val() > 0){
           //当隐藏的任务id值大于0，则修改任务
           $('#taskForm').attr('action','editTask');
       }else{
           //否则创建任务
           $('#taskForm').attr('action','createTask');
       }
       //表单提交
       $('#taskForm').submit();
  }
</script>

<body class="bodyTextStyle">
  <!--面包屑-->
  <a href="goCreateTask.action" class="aStyle">任务管理</a><label>></label><a href="#" class="aStyle">创建任务</a>

  <div class="context">
    <s:form id="taskForm" theme="simple" method="post">
      <!--任务id-->
      <s:if test="#request.flag == 'edit'">
        <div class="textMar" >
          任务ID：<s:label name="task.taskId"/>
        </div>
      </s:if>
      <div class="textMar">
        <label>任务名称：</label>
        <s:textfield id="taskName" name="task.taskName" placeholder="请输入任务名称" class="textStyle" />
      </div>
      <div class="textMar">
        <label>任务描述：</label><br>
        <s:textarea id="taskDesc" name="task.taskDesc" placeholder="请输入任务描述信息" class="textareaStyle" />
      </div>
      <div class="textMar">
        <label>执行者：</label>
        <s:select id="executorMap" list="map" listKey="key" name="task.executorId" listValue="value" headerKey="0" headerValue="请选择执行者"/>
        <input type="hidden" id="executorName" name="task.executorName">
      </div>
      <input type="hidden" name="ids" value="<s:property value="#request.ids"/>">
      <input type="hidden" id="hidden_taskId" name="task.taskId" value="<s:property value="task.taskId"/>">
      <div class="textMar" style="margin-left:60px">
        <input type="button" value="取   消" class="btnStyle" style="margin-right:30px" onclick="window.location.href='goCreateTask.action'">
        <input type="button" value="保   存" class="btnStyle" onclick="saveTask()"/>
      </div>
    </s:form>
  </div>
</body>
</html>
