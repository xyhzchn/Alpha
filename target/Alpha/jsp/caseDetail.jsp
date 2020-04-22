<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/8/9
  Time: 13:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <style type="text/css">
    #runLog{
      overflow-x: auto;
      overflow-y:auto;
      width: 80%;
      height:10%;
      border:1px solid;
      font-size: 10px;
    }
  </style>
</head>
<body class="bodyTextStyle" style="text-align:left">
  <!--面包屑-->
  <a href="#" class="aStyle">用例管理</a><label>></label><a href="#" class="aStyle">用例详情</a>

  <div id="contextDiv" class="context" style="margin-top: 2%">

    <s:if test='#request.flag == "run"'>
      <!--用例列表-->
      <s:iterator value="allCases" var="caseList">
        <!--div 层的id为用例id-->
        <div id="<s:property value="case_id"/>" class="caseDtl">
          <!--用例信息-->
          <table style="font-size: 12px;color: #666;">
            <tr>
              <td>
                  <label>用&nbsp;&nbsp;例&nbsp;&nbsp;ID：</label><label><s:property value="project"/><span>_</span><s:property value="case_id"/></label></br>
                  <label>用例简述：</label><label><s:property value="caseTitle" /></label></br>
                  <label>用例版本：</label><label><s:property value="caseVersion" /></label></br>
              </td>
              <td style="padding-left:50px">
                  <label >自动化脚本：</label><label><s:property value="scriptName" /></label></br>
                  <label >关联BugID：</label><label>暂无</label></br>
                  <label >用例优先级：</label><label><s:property value="priority" /></label></br>
              </td>
            <tr>
          </table>
          <!--分隔符-->
          <hr style="float:left;"/>
            <div class="textMar">
              <table border="1" cellpadding="0" cellspacing="0" width="80%">
                <tr>
                  <td style="width: 100px;">前置条件</td>
                  <td><s:property value="precondition" /></td>
                </tr>
                <tr>
                  <td style="width: 100px;">测试步骤</td>
                  <td><s:property value="testStep" />
                  </td>
                </tr>
                <tr>
                  <td style="width: 100px;">预期结果</td>
                  <td><s:property value="expectedRes" /></td>
                </tr>
                <tr>
                  <td style="width: 100px;">备注</td>
                  <td><s:property value="caseDesc" /></td>
                </tr>
              </table>
            </div>
          <!--分隔符-->
          <s:if test="runLogs.size() >0">
            <hr style="float:left;width:80%;"/></br>
              <!--用例的执行log-->
              <label>执行日志：</label>
              <div id="runLog">
                <s:iterator value="runLogs">
                <p><s:property/></p>
                </s:iterator>
              </div>
          </s:if>
          <!--分隔符-->
          <hr style="float:left;width:80%;"/></br>
          <!--用例执行结果选择-->
              <label>用例执行结果:</label>
              <select id="<s:property value="case_id"/>-res">
                <option value="1" selected>PASS</option>
                <option value="2">FAIL</option>
                <option value="3">BLOCK</option>
              </select></br></br>
              <!--关联bug-->
              <label >关联BUG_ID:</label>
              <input type="text" disabled placeholder="暂不可用" id="<s:property value="case_id"/>-bug"></br></br>
              <!--用例备注-->
              <label >用例备注:</label></br>
              <textarea id="<s:property value="case_id"/>-desc" placeholder="用例备注……" class="textareaStyle"></textarea></br></br>
              <!--按钮-->
              <input type="button" class="btnStyle" style="margin-left: 7%" value="返   回" onclick="getRunnedCase()">
              <input type="submit" id="<s:property value="case_id"/>-next" class="btnStyle" value="下 一 个" onclick="nextdiv(this.id)">
              <!--隐藏域-->
              <input type="hidden" id="passCaseId" value="<s:property value="#request.passCaseId"/>">
              <input type="hidden" id="caseId">
        </div>
      </s:iterator>
      <!--隐藏域-->
      <input type="hidden" id="hiddenTaskId" value="<s:property value="taskId"/>" />
    </s:if>
    <!--普通的用例详情-->
    <s:if test='#request.flag == "detail"'>
      <div id="detailView">
        <!--用例基本信息-->
        <table style="margin-bottom:10px;font-size: 12px;color: #666;">
          <tr>
            <td>
              <label>用&nbsp;&nbsp;例&nbsp;&nbsp;ID：</label><label style="margin-left: 10px"><s:property value="acase.project"/><span>_</span><s:property value="acase.case_id"/></label></br>
              <label>用例简述：</label><label><s:property value="acase.caseTitle" /></label></br>
              <label>用例版本：</label><label><s:property value="acase.caseVersion" /></label></br>
            </td>
            <td style="padding-left:50px">
              <label >自动化脚本：</label><label><s:property value="acase.scriptName" /></label></br>
              <label >关联BugID：</label><label>暂无</label></br>
              <label >用例优先级：</label><label><s:property value="acase.priority" /></label></br>
            </td>
          <tr>
        </table>
        <!--分隔符-->
        <hr style="float:left;width:80%;"/>
        <!--用例详情-->
        <div class="textMar">
          <table border="1" cellpadding="0" cellspacing="0" width="80%">
            <tr>
              <td style="width: 100px;">前置条件</td>
              <td style="text-align: left"><s:property value="acase.precondition" /></td>
            </tr>
            <tr>
              <td style="width: 100px;">测试步骤</td>
              <td style="text-align: left"><s:property value="acase.testStep" />
              </td>
            </tr>
            <tr>
              <td style="width: 100px;">预期结果</td>
              <td style="text-align: left"><s:property value="acase.expectedRes" /></td>
            </tr>
            <tr>
              <td style="width: 100px;">备注</td>
              <td style="text-align: left"><s:property value="acase.caseDesc" /></td>
            </tr>
          </table>
        </div>
        <!--按钮-->
        <input type="button" class="btnStyle" value="返   回" onclick="history.back()">
        <input type="submit" class="btnStyle" value="修   改" onclick="editCase(this.value)">
      </div>
    </s:if>
  <!--用例修改-->
  <s:form id="updateFrom" theme="simple" method="post">
    <div id="detailEdit" style="display: none">
      <table style="margin-bottom:10px;font-size: 12px;color: #666;">
        <tr>
          <td>
            <label>用&nbsp;&nbsp;例&nbsp;&nbsp;ID：</label><s:property value="acase.project"/><span>_</span><s:property value="acase.case_id"/> </br>
            <label>用例简述：</label><s:textfield class="textStyle" name="acase.caseTitle" /> </br>
            <label>用例版本：</label><s:textfield class="textStyle" name="acase.caseVersion" /> </br>
          </td>
          <td style="padding-left:50px">
            <label >自动化脚本：</label><s:select name="acase.scriptId" list="scriptList" listkey="key" listvalue="value" headerKey="0" headerValue="请选择或输入" /></br>
            <label >关联BugID：</label><label>暂无</label></br>
            <label >用例优先级：</label><s:textfield class="textStyle" name="acase.priority" /> </br>
          </td>
        <tr>
      </table>
      <hr style="float:left;width:80%;"/>
      <div class="textMar">
        <table border="1" cellpadding="0" cellspacing="0" width="80%">
          <tr>
            <td style="width: 100px;">前置条件</td>
            <td style="text-align: left"><s:textarea class="textareaStyle" name="acase.precondition"/></td>
          </tr>
          <tr>
            <td style="width: 100px;">测试步骤</td>
            <td style="text-align: left"><s:textarea class="textareaStyle" id="testStepStr" name="acase.testStep"/></td>
          </tr>
          <tr>
            <td style="width: 100px;">预期结果</td>
            <td style="text-align: left"><s:textarea class="textareaStyle" id="resultStr" name="acase.expectedRes"/></td>
          </tr>
          <tr>
            <td style="width: 100px;">备注</td>
            <td style="text-align: left"><s:textarea class="textareaStyle" name="acase.caseDesc"/></td>
          </tr>
        </table>
        <div id="errorDiv"></div>
      </div>

      <input type="hidden" name="catalog_id" value="<s:property value='acase.catalog_id'/>"/>
      <input type="hidden" name="caseId" value="<s:property value='acase.case_id'/>"/>
      <input type="button" class="btnStyle" value="返   回" onclick="history.back()">
      <input type="button" class="btnStyle" value="保   存" onclick="updateCase()"/>
    </div>
  </s:form>
  </div>
  <script type="text/javascript">
      //页面第一次加载，加载所有数据
      $(document).ready(function(){
          //所有数据的div默认隐藏
          $(".caseDtl").hide();
          //获取从上个页面传递进来的用例Id
          var passCaseId = document.getElementById("passCaseId").value;
          //显示id值与传过来的用例id 一致的div
          $('#'+passCaseId).show();
      });
      /**
       * 点击下一个按钮，显示该div的下一个div
       * @param id
       */
      function nextdiv(id){
          //根据下一个按钮的id分割
          var idstr = id.split("-");
          //获取caseId
          var caseStr = idstr[0];
          //选择的结果
          var result = $('#'+caseStr+'-res').val();
          //var bugId = $('#'+caseStr+'-bug').val();
          //填写的用例描述
          var runDesc =$('#'+caseStr+'-desc').val();
          //获取id中用例id的部分
          var run = {
              'run.caseId':caseStr,
              'run.result':result, //执行结果
              //'run.bugId':bugId, //bug_id
              'run.runDesc':runDesc //用例描述
          };
          var taskId = $("#hiddenTaskId").val();
          $.ajax({
              type:'post',
              url:'runCase.action?taskId='+taskId,
              data:run,
              dataType:'json',
              success:function(data){
                  //隐藏id为上述用例id的div
                  $('#'+caseStr+'').hide();
                  if($('#'+caseStr+'').next("div").length>0){
                      //显示该div的下一个div
                      $('#'+caseStr+'').next("div").show();
                  }else {
                      $('#contextDiv').append("<h1>所有用例都已执行完毕</h1><br><input type='button' class='btnStyle' style='margin-left: 7%' value='返   回' onclick='getRunnedCase()'/>");
                  }

              }
          });
       }
      /**
       * 修改测试用例
       * @param btnValue
       */
      function editCase(btnValue){
        if(btnValue == "修   改"){
          $("#detailView").css("display","none");
          $("#detailEdit").css("display","block");
        }
       }
      //修改用例前的数据验证
      function updateCase(){
        //错误信息默认设置
        $('.errorMsg').html(" ");
        //获取测试步骤和预期结果的值
        var testStep = $('#testStepStr').val();
        var result = $('#resultStr').val();
        //验证测试步骤
        if(testStep == "" || testStep.length == 0){
            $('#errorDiv').after("<label class='errorMsg'>*测试步骤不可为空</label>");
            return false;
        }
        //验证预期结果
        if(result == "" || result.length == 0){
            $('#errorDiv').after("<label class='errorMsg'>*预期结果不可为空</label>");
            return false;
        }
        //设置form的action，并提交
        $('#updateFrom').attr('action','updateCase');
        $('#updateFrom').submit();
      }
      /**
       * 用例执行页面，点击返回后，刷新任务下的用例列表
       */
      function getRunnedCase() {
        var taskId = $('#hiddenTaskId').val();
        window.location.href = "runningTask.action?taskId="+taskId+"&flag=run";
      }
  </script>
</body>
</html>
