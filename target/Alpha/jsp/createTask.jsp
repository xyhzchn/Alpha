<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/8/9
  Time: 20:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="org.json.JSONArray" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <title>创建任务</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css" type="text/css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
  <script src="<%=request.getContextPath()%>/js/myJquery.js"></script>

  <script type="text/javascript">
      $( function() {
          //日期
          $( "#datepickerFrom" ).datepicker({
              showButtonPanel:true,//是否显示按钮面板
              dateFormat:'yy-mm-dd',//日期格式
              prevText:'<上月',
              prevBigText:'<<',
              nextText:'下月>',
              nextBigText:'>>',
              minDate:'2017-01-01',
              monthNames:['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
              dayNamesMin:['日','一','二','三','四','五','六'],
              onSelect:function(selectedDate){
                  $( "#datepickerTo" ).datepicker("option","minDate",selectedDate);
              }
          });
          $( "#datepickerTo" ).datepicker({
              showButtonPanel:true,//是否显示按钮面板
              dateFormat:'yy-mm-dd',//日期格式
              prevText:'<上月',
              prevBigText:'<<',
              nextText:'下月>',
              nextBigText:'>>',
              maxDate:'2030-01-01',
              monthNames:['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
              dayNamesMin:['日','一','二','三','四','五','六'],
              onSelect:function(selectedDate){
                  $( "#datepickerFrom" ).datepicker("option","maxDate",selectedDate);
              }
          });
      } );

      //下一步
      function nextStep(){

          if($("input[name='checkbox']").is(':checked')){
              var obj = document.getElementsByName("checkbox");//返回所有name=checkbox的对象数组
              var ids = [];
              for(var i = 0;i<obj.length;i++){
                  if(obj[i].checked){
                      ids.push(obj[i].value);//如果选中则将 value放到变量中
                  }
              }
              var taskId = $('#taskId').val();
              //任务id大于0，代表是编辑
              if(taskId > 0){
                  //页面跳转是传递任务id
                  window.location.href='goCreateTaskSec.action?ids='+ids+'&taskId='+taskId;
              }else{
                  window.location.href='goCreateTaskSec.action?ids='+ids;
              }
          }else{
              alert("请选择用例");
          }
      }
      //查询用例
      function searchCase(){
          //如果复选框选中
          if($("#getSons").is(":checked")){
              $("#getSons").attr("value",true);  //则设置为true
          }else {
              $("#getSons").attr("value",false); //否则，设置为false
          }
         $('#caseForm').attr("action","goCreateTask.action");
         $('#caseForm').submit();
      }

          //获取后台返回的目录结构，以json格式返回
          var json =<%=request.getAttribute("trees")%>;
          function getNextSelect(is) {
              //当前选择的select的ID
              var idvalue = $(is).attr("id");
              //根据当前选择的select的ID值截取到当前的节点等级
              var nodeLevel = parseInt(idvalue.substr(idvalue.length - 1, idvalue.length));
              //当前选择的目录的值
              var optionValue = $('#' + idvalue + '').val();
              $('#selectCatalogId').val(optionValue);
              //获取当前节点下子节点的数目
              var count = 0;
              $.each(json, function (idx, obj) {
                  if (obj.parentId == optionValue && obj.level == nodeLevel + 1) {
                      count++;
                  }
              });
              //获取待显示的子节点的等级
              var nowLevel = nodeLevel + 1;
              //如果子节点的数目大于0
              if (count > 0) {
                  //新的节点显示并且节点下的除了value=-1的option元素都移除
                  $('#level' + nowLevel + '').attr("style","display:inline;");
                  $('#level' + nowLevel + '').find("option:not([value='-1'])").remove();

              }else{
                  //当新的节点的数目不大于0时，新的节点以及其之后的所有节点移除
                  $('#level' + nowLevel + '').attr("style","display:none");
                  $('#level' + nowLevel + '').nextAll().attr("style","display:none");
              }
              //再次循环所有的节点
              $.each(json, function (idx, obj) {
                  //如果某个节点的父节点=之前选中的节点ID并且某个节点的level等于当前要显示的节点的等级时
                  if (obj.parentId == optionValue && obj.level == nowLevel) {
                      //当前select添加option元素，设置option元素的value和text属性
                      $('#level' + nowLevel + '').append($("<option value='" + obj.id + "'>" + obj.catalogName + "</option>"));
                  }
              });
          }
  </script>
</head>
<body class="bodyTextStyle">
  <!--面包屑-->
  <a href="adminTaskList.action" class="aStyle">任务管理</a><label>></label><a href="#" class="aStyle">创建任务</a>

    <div class="context">
      <s:form id="caseForm" theme="simple" method="post">
        <!--任务id-->
        <s:if test="#request.flag == 'edit'">
          <div class="textMar" >
            任务ID：<s:label name="taskId"/>
            <input type="hidden" id="taskId" name="taskId" value="<s:property value="taskId"/> ">
          </div>
        </s:if>
        <!--检索项目-->
        <div class="textMar" >
          <s:textfield class="textStyle" style="width:80px;margin-right: 2%" name="acase.search_caseId"  placeholder="用例ID" value=""/>
          <s:textfield class="textStyle" style="width:80px;margin-right: 2%" name="acase.search_caseTitle"  placeholder="用例简述"/>
          <s:select list="createrMap" style="margin-right:2%" listKey="key" listValue="value" headerKey="0" headerValue="请选择创建者" name="acase.search_creater"/>
          <s:select list="priorityList" style="margin-right:2%" theme="simple" headerKey="0" headerValue="请选择优先级" name="acase.search_priority"/>
          <select name="acase.search_result">
            <option value="0" selected>请选择执行结果</option>
            <option value="1">PASS</option>
            <option value="2">FAILURE</option>
            <option value="3" >BLOCK</option>
          </select>
        </div>
        <div class="textMar" >
          <%
             //获取存储在request中的变量
             JSONArray jsonArray =(JSONArray)request.getAttribute("trees");
             //定义map，来接收ID和name值
             Map<Integer,String> level0Map = new HashMap<Integer,String>();
             Map<Integer,String> level1Map = new HashMap<Integer,String>();
             Map<Integer,String> level2Map = new HashMap<Integer,String>();
             Map<Integer,String> level3Map = new HashMap<Integer,String>();
             Map<Integer,String> level4Map = new HashMap<Integer,String>();
             //循环遍历jsonArray
             for(int i=0;i<jsonArray.length();i++){
                 //当JSONObject的level=0时
               switch (jsonArray.getJSONObject(i).getInt("level")) {
                 //map中增加对应的节点ID和节点名字
                 case 0:
                   level0Map.put(jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("catalogName"));break;
                 case 1:
                   level1Map.put(jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("catalogName"));break;
                 case 2:
                   level2Map.put(jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("catalogName"));break;
                 case 3:
                   level3Map.put(jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("catalogName"));break;
                 case 4:
                   level4Map.put(jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("catalogName"));break;
               }
             }
             //当map不为空
             if(level0Map.size() > 0){
          %>
          <select id="level0" onchange="getNextSelect(this)">
            <option value="-1" selected>请选择一级节点</option>
            <% for(Integer key:level0Map.keySet()){ //获取map中的所有key值%>
            <option value="<%=key%>"><%=level0Map.get(key)%></option>
            <%}%>
          </select>
          <%
             }if(level1Map.size()>0){
          %>
          <select id="level1" onchange="getNextSelect(this)" style="display: none;">
            <option value="-1" selected>请选择二级节点</option>
          </select>
          <%
            } if(level2Map.size()>0){
          %>
          <select id="level2" onchange="getNextSelect(this)" style="display: none;">
            <option value="-1" selected>请选择三级节点</option>
          </select>
          <%
            } if(level3Map.size()>0){
          %>
          <select id="level3" onchange="getNextSelect(this)" style="display: none;">
            <option value="-1" selected>请选择四级节点</option>
          </select>
          <%
            } if(level4Map.size()>0){
          %>
          <select id="level4" onchange="getNextSelect(this)" style="display: none;">
            <option value="-1" selected>请选择五级节点</option>
          </select>
          <%
            }
          %>
          <s:checkbox id="getSons" name="containSons"/>包含子节点
          <s:hidden id="selectCatalogId" name="acase.search_catalogId" value=""/>
        </div>
        <div class="textMar">
          <s:select list="versionList" style="margin-right:2%" theme="simple" headerKey="0" headerValue="请选择版本" name="acase.search_version"/>
          <input type="text" id="datepickerFrom" name="acase.search_timeFrom"  placeholder="开始时间">
          <input type="text" id="datepickerTo" name="acase.search_timeTo"  placeholder="结束时间">
        </div>
        <!--检索按钮-->
        <div class="textMar" >
          <input type="button" class="btnStyle" value="检   索" style="margin-left:60%" onclick="searchCase()">
        </div>
        <!--用例列表-->
        <div class="textMar">
          <s:if test="cases.size() >0">
            <table border="0" cellpadding="0" cellspacing="0" style="width: 1200px;height: 600px;">
              <thead>
                <tr>
                  <th width="2%"><input type="checkbox" onclick="checkAll(this)"></th>
                  <th width="5%">用例ID</th>
                  <th width="10%">用例简述</th>
                  <th width="10%">预置条件</th>
                  <th width="18%">测试步骤</th>
                  <th width="18%">预期结果</th>
                  <th width="5%">优先级</th>
                  <th width="5%">版本号</th>
                  <th width="6%">执行结果</th>
                  <th width="6%">脚本名称</th>
                  <th width="15%">操作</th>
                </tr>
              </thead>
              <tbody style="overflow: auto">
                <s:iterator value="cases" var="caseList">
                  <tr>
                    <td><input type="checkbox" style="margin-left: 6px;" name="checkbox" value="<s:property value="case_id" />"></td>
                    <td><s:property value="project"/><span>_</span><s:property value="case_id" /></td>
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
                      <td>PASS</td>
                    </s:elseif>
                    <s:elseif test="lastResult == 2">
                      <td>FAILURE</td>
                    </s:elseif>
                    <s:elseif test="lastResult == 3">
                      <td>BLOCK</td>
                    </s:elseif>
                    <td><s:property value="scriptName" /></td>
                    <td><input type="button" value="详   情" class="btnStyle"></td>
                  </tr>
                </s:iterator>
              </tbody>
            </table>
          </s:if>
          <s:else>
            <div style="text-align: center">暂无数据……</div>
          </s:else>
        </div>
    </div>
        <!--取消和下一步按钮-->
        <div class="textMar" style="margin-left: 60%">
          <input type="button" class="btnStyle" value="取   消" onclick="window.location.href='adminTaskList.action'">
          <s:if test="cases.size() >0">
            <input type="button" class="btnStyle" value="下 一 步" onclick="nextStep()">
          </s:if>
        </div>
    </s:form>
</body>
</html>
