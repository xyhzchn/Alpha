<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/8/15
  Time: 18:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>待执行用例列表</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script src="<%=request.getContextPath()%>/js/myJquery.js"></script>
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
      top:40%;
      left:25%;
      display:none;
    }
  </style>
  <script type="text/javascript">
    /**
     * 行点击事件
     * @param caseId

    $(function(){
        $("tr[name='rows']").click(function(){
            //用例id
            //var caseId = this.childNodes.item(3).textContent;
            var caseId = this.id;
            //任务id
            var taskId = $("#taskId").val();
            //发送请求
            window.location.href="runCaseDetail.action?caseId="+caseId+"&taskId="+taskId;
        });
    });*/
    /**
     * 手动执行
     * @param caseId
     */
    function handRun(caseId) {
        //任务id
        var taskId = $("#taskId").val();
        //发送请求
        window.location.href="runCaseDetail.action?caseId="+caseId+"&taskId="+taskId;
    }
    /**
     * 单个用例自动执行
     * @param caseId
     */
    function autoRunByOne(caseId) {
        //任务id
        var taskId = $("#taskId").val();
        //发送请求
        window.location.href="scriptRun.action?caseId="+caseId+"&taskId="+taskId;
        //弹出遮罩层
        window.top.$('.theme-popover-mask').fadeIn(100);
        $('.theme-popover').slideDown(100);
    }
      //一键自动执行
      function autoRun() {
        //判断是否有复选框选中
        if($("input[name='checkbox']").is(':checked')){
          //定义用例ID数组
          var ids = new Array();
          //获取所有的行
          var rows = document.getElementById("tableId").rows;
          //获取所有的复选框
          var r = document.getElementsByName("checkbox");
          //循环所有的复选框
          for(var i=0;i<r.length;i++){
            //当某个复选框选中
            if(r[i].checked){
              //获取该复选框对应的行
              var row = r[i].parentElement.parentElement.rowIndex;
              //获取选中复选框对应行的第10列-脚本名称
              var scriptName = rows[row].cells[9].innerHTML;
              //判断脚本名称是否为空
              if(scriptName.length == 0){
                //是的话，弹出提示框，并跳出循环
                alert("一键自动执行仅针对包含自动化测试脚本的用例，请重新选择");
                return false;
              }else {
                //否的话，将对应复选框的值加到数组中
                ids.push(r[i].value);//如果选中则将 value放到变量中
              }
            }
          }
        }else{

          alert("请选择自动自行用例列表");
          return false;
        }

        //任务id
        var taskId = $("#taskId").val();
        //发送请求
        window.location.href="moreScriptRun.action?ids="+ids+"&taskId="+taskId;
        //弹出遮罩层
        window.top.$('.theme-popover-mask').fadeIn(100);
        $('.theme-popover').slideDown(100);
      }

    /**
     * 页面刷新时执行
     */
    function onLoadFun() {
        window.top.$('.theme-popover-mask').fadeOut(100);
        $('.theme-popover').slideUp(100);
      }
  </script>
</head>

<body class="bodyTextStyle" onload="onLoadFun()">
  <!--面包屑-->
  <a href="testerTaskList.action" class="aStyle">任务管理</a><label>></label><a href="#" class="aStyle">待执行任务列表</a>
  <div class="context">
    <s:form theme="simple" method="post">
      <!--用例查询-->
      <%--<div class="textMar" >--%>
        <%--<label>用&nbsp;&nbsp;例&nbsp;&nbsp;ID：</label>--%>
        <%--<s:textfield class="textStyle" style="width:80px;margin-right: 2%" name="acase.search_caseId"/>--%>
        <%--<label>用例简述：</label>--%>
        <%--<s:textfield class="textStyle" style="width:80px;margin-right: 2%" name="acase.search_caseTitle"/>--%>
        <%--<label>执行结果：</label>--%>
        <%--<select name="acase.search_result">--%>
          <%--<option value="0" selected>暂无</option>--%>
          <%--<option value="1">SUCCESS</option>--%>
          <%--<option value="2">FAILURE</option>--%>
          <%--<option value="3" >BLOCK</option>--%>
        <%--</select>--%>
        <%--<input type="hidden" name="flag" value="run">--%>
      <%--</div>--%>
      <%--<div class="textMar" >--%>
        <%--<s:submit class="btnStyle" value="检   索" style="margin-left:60%"/>--%>
      <%--</div>--%>
      <div class="textMar">
        <s:if test="task.caselist.size() >0">
          <table id="tableId" border="1" cellpadding="0" cellspacing="0">
            <tr>
              <th><input type="checkbox" id="all" onclick="checkAll(this)"></th>
              <th style="width: 5%;">用例ID</th>
              <th style="width: 12%;">用例简述</th>
              <th style="width: 12%;">预置条件</th>
              <th style="width: 18%;">测试步骤</th>
              <th style="width: 18%;">预期结果</th>
              <th style="width: 5%;">优先级</th>
              <th style="width: 5%;">版本号</th>
              <th style="width: 6%;">执行结果</th>
              <th style="width: 6%;">脚本名称</th>
              <th colspan="2" style="width: 13%;">操作</th>
            </tr>
            <s:iterator value="task.caselist" var="caseList" status="c">

              <tr name="rows">
                <td><input type="checkbox" name="checkbox" value="<s:property value="case_id"/>"></td>
                <td><s:property value="project"/><span>_</span><s:property value="case_id"/></td>
                <input type="hidden" id="case_Id" value="<s:property value="case_id"/>"/>
                <td><s:property value="caseTitle" /></td>
                <td><s:property value="precondition" /></td>
                <td><s:property value="testStep" /></td>
                <td><s:property value="expectedRes" /></td>
                <td><s:property value="priority" /></td>
                <td><s:property value="caseVersion" /></td>
                <s:if test="#caseList.lastResult == 0" >
                  <td>暂无</td>
                </s:if>
                <s:elseif test="#caseList.lastResult == 1">
                  <td style="color: green;">PASS</td>
                </s:elseif>
                <s:elseif test="#caseList.lastResult == 2">
                  <td style="color: red;">FAILURE</td>
                </s:elseif>
                <s:elseif test="#caseList.lastResult == 3">
                  <td style="color: blue;">BLOCK</td>
                </s:elseif>
                <td><s:property value="scriptName" /></td>
                <td><input type="button" class="btnStyle" value="手动执行" onclick="handRun($(this).parents('tr').find('#case_Id').val())"/></td>
                <s:if test="scriptId > 0">
                  <td><input type="button" class="btnStyle" value="自动执行" onclick="autoRunByOne($(this).parents('tr').find('#case_Id').val())"/></td>
                </s:if>
              </tr>
            </s:iterator>
          </table>
        </s:if>
        <s:else>
          <div style="text-align: center">暂无数据……</div>
        </s:else>
      </div>
      <input type="hidden" id="taskId" name="taskId" value="<s:property value='task.taskId' />">
      <div class="textMar">
        <input type="button" class="btnStyle" style="margin-left: 60%" value="取   消" onclick="window.location.href='testerTaskList.action?tabId=1'"/>
          <s:if test="scriptCount > 0">
              <input type="button" class="btnStyle" style="margin-left: 3%;width: 100px;" value="一键自动执行" onclick="autoRun()"/>
          </s:if>
      </div>
    </s:form>
  </div>
  <div class="theme-popover-mask"></div>
  <div class="theme-popover">
    <img src="<%=request.getContextPath()%>/img/loading.gif">
  </div>
</body>
</html>

