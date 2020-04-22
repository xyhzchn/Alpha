<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>用例管理</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css" type="text/css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
  <script src="<%=request.getContextPath()%>/js/myJquery.js"></script>
  <script type="text/javascript">
      //日期选择
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
    <!--查询-->
    function searchData(){
        $("input[name='is_search']").attr("value",true);
        document.forms["form1"].action = 'getCaseList.action';
        document.forms["form1"].submit();
    }
    <!--跳转到指定页面-->
    function goPage() {
        var page = $("#searchPage").val();
        var totalPage = <s:property value="#request.pageBean.totalPage"/>;
        if(page > totalPage){
            alert("输入的页码超过总页数,请重新输入");
        }else{
            $("#page").attr("value",page);
            document.forms.item(0).action = "getCaseList.action";
            document.forms.item(0).submit();
        }
    }

    //导入用例
    function importExcel(){
        document.getElementById("btn_file").click();
    }

    //判断上传文件类型
    function checkFile(obj){
       // var catalog_id =  $("#hidden_catalog_id").val();
        if (obj.value == ""){
            alert("请选择上传文件");
        }else{
            //获取文件后缀名
            var index1 = obj.value.lastIndexOf(".");
            var index2 = obj.value.length;
            var fileType = obj.value.substring(index1,index2);
            //对文件后缀名进行判断
            if(fileType != ".xls"){
                alert("不支持该文件类型，请上传后缀名为.xls的文件");
            }else{
                //实现文件上传操作
                document.getElementById("form1").action ='uploadFile.action';
                document.getElementById("form1").submit();
            }
        }
    }

    //选择用例导出?catalog_id='+catalogId
    function exportExcel() {
        // var catalogId = $("#hidden_catalog_id").val();
        document.getElementById("form1").action ='exportExcel.action';
        document.getElementById("form1").submit();
        // //获取选择的用例id列表
        // if($("input[name='checkbox']").is(':checked')){
        //    var obj = document.getElementsByName("checkbox");//返回所有name=checkbox的对象数组
        //    var ids = new Array();
        //    for(var i = 0;i<obj.length;i++){
        //      if(obj[i].checked){
        //          ids.push(obj[i].value);//如果选中则将 value放到变量中
        //      }
        //    }
        //    //模拟表单提交
        //     //var params = $.param({'ids':ids},true);
        //            var form = $("<form>");//定义一个form表单
        //            form.attr("style","display:none");
        //            form.attr("target","");
        //            form.attr("method","post");
        //            form.attr("action","exportExcel.action");
        //            var input = $("<input>");
        //            input.attr("type","hidden");
        //            input.attr("name","ids");
        //            input.attr("value",ids);
        //            $("body").append(form);//将表单放置到body中
        //            form.append(input);
        //            form.submit();//表单提交
        //
        // }else{
        //     alert("请选择要导出的数据");
        // }
    }

    /**
     * 获取用例详情
     * @param id
     */
    function getCaseDetail(id){
        var eName = $("#hidden_eName").val();
        window.location.href='caseDetail.action?caseId='+id+"&ename="+eName;
    }
    /**
     * 获取用例详情
     */
    function addCase() {
        var catalog_id =  $("#hidden_catalog_id").val();
        window.location.href='addCase.action?catalog_id='+catalog_id;
    }
    /**
     * 删除测试用例
     * @param id
     */
    function deleteCase(id){
        var eName = $("#hidden_eName").val();
        var isDel = confirm("确定要删除用例["+eName+"_"+id+"]么？");
        if(isDel){
            $.ajax({
                type:'post',
                url:'deleteCase.action?',
                async:false,
                data:{
                    caseId:id
                },
                dataType:'text',
                success:function(data){
                    if(data == "success"){
                        alert("删除成功！");
                        //页面刷新
                        window.location.reload();

                    }else{
                        alert("删除失败！");
                    }
                }
            });
        }else{
            return isDel;
        }
    }

      function downloadFile() {
          //获取目录id
          var catalog_id = $("#hidden_catalogId").val();
          window.location.href="download.action?catalog_id="+catalog_id;
      };

      //弹出文件浏览框
      function fileOpenDialog() {
          $("#file_btn").click();
      }

      /**
       * 分页
       * @param id 不同连接的id
       */
      function changePage(id) {
          //如果是上一页
          if(id == "opevPage"){

              $("#page").attr("value",<s:property value="#request.pageBean.currentPage - 1"/>);

          }else if(id =="nextPage"){ //下一页

              $("#page").attr("value",<s:property value="#request.pageBean.currentPage + 1"/>);

          }else if(id =="lastPage"){ //尾页

              $("#page").attr("value",<s:property value="#request.pageBean.totalPage" />);
          }else {
              $("#page").attr("value",0);
          }
          //提交表单
          document.getElementById("form1").action ='getCaseList.action';
          document.getElementById("form1").submit();
      }

      /**
       * 页面加载操作
       * 设置对应的查询条件
       */
      function pageOnLoad() {
          $("#searchCaseId").attr("value",${acase.search_caseId} == 0?'':${acase.search_caseId});        //用例id
          $("#searchCaseTitle").attr("value",${acase.search_caseTitle});  //用例简述
          $("#searchPriority").attr("value",${acase.search_priority});    //用例优先级
          $("#searchResult").attr("value",${acase.search_result});        //执行结果
          $("#searchVersion").attr("value",${acase.search_version});      //版本号
      }
  </script>
</head>

<body class="bodyTextStyle" onload="pageOnLoad()">
<a href="#" class="aStyle">用例管理</a>
<s:form id="form1" theme="simple" enctype="multipart/form-data" name="form1">
  <div class="context">
    <div class="textMar" >
        <!--检索项目-->
        <div class="textMar" >
          <s:textfield class="textStyle" id="searchCaseId" style="width:80px;margin-right: 2%" name="acase.search_caseId" placeholder="用例ID"/>
          <s:textfield class="textStyle" id="searchCaseTitle" style="width:80px;margin-right: 2%" name="acase.search_caseTitle" placeholder="用例简述"/>
          <s:select list="priorityList" id="searchPriority" style="margin-right:2%" theme="simple" headerKey="0" headerValue="请选择优先级" name="acase.search_priority"/>
          <s:select list="resultList" id="searchResult" listkey="key" listvalue="value" headerKey="0" headerValue="请选择执行结果" name="acase.search_result"/>
        </div>
        <div class="textMar" >
          <s:select list="versionList" id="searchVersion" style="margin-right:2%" name="acase.search_version" theme="simple" headerKey="0" headerValue="请选择版本"/>
          <!--<label>创建时间：From</label>-->
          <input type="text" id="datepickerFrom" name="acase.search_timeFrom" placeholder="开始时间">
          <!-- <label>To</label>-->
           <input type="text" id="datepickerTo" name="acase.search_timeTo" placeholder="结束时间">
         </div>
         <!--检索按钮-->
        <div class="textMar" >
            <input type="button" class="btnStyle" value="检   索" style="margin-left:400px" onclick="searchData()">
            <s:file id="btn_file" style="display: none;" name="upload" value="" onchange="checkFile(this)"></s:file>
            <input type="button" class="btnStyle" value="导   入" style="margin-left:50px" onclick="importExcel()">
            <input type="button" class="btnStyle" value="导   出" style="margin-left:10px" onclick="exportExcel()">
            <input type="button" class="btnStyle" value="添   加" style="margin-left:10px" onclick="addCase()">
        </div>
    </div>

    <div class="textMar" id="divSearchResult">
      <s:if test="#request.pageBean.list.size() > 0">
        <input type="hidden" id="hidden_catalog_id" name="catalog_id" value="<s:property value="#request.catalog_id"/>"/>
        <input type="hidden" name="is_search" value="<s:property value="#request.is_search"/>">
        <table id="caseListTable" border="0" cellpadding="0" cellspacing="0" style="overflow: auto;padding-bottom: 5%;" width="1200px">
          <tr id="thRow">
            <th style="width:2%"><input type="checkbox" id="all" onclick="checkAll(this)"></th>
            <th style="width:5%">用例ID</th>
            <th style="width:10%">用例简述</th>
            <th style="width:10%">预置条件</th>
            <th style="width:18%">测试步骤</th>
            <th style="width:18%">预期结果</th>
            <th style="width:5%">优先级</th>
            <th style="width:5%">版本号</th>
            <th style="width:6%">创建者</th>
            <th style="width:6%">执行结果</th>
            <th style="width:6%">脚本名称</th>
            <th colspan="3" style="width:15%">操作</th>
          </tr>

            <s:iterator value="#request.pageBean.list" var="case">
              <tr>
                <td><input type="checkbox" name="checkbox" value="<s:property value="#case.case_id"/>" style="margin-left: 6px;"></td>
                <td style="text-align: center;"><s:property value="#case.project"/><span>_</span><s:property value="#case.case_id"/></td>
                <td><s:property value="#case.caseTitle" /></td>
                <td><s:property value="#case.precondition" /></td>
                <td><s:property value="#case.testStep" /></td>
                <td><s:property value="#case.expectedRes" /></td>
                <td style="text-align: center;"><s:property value="#case.priority" /></td>
                <td><s:property value="#case.caseVersion" /></td>
                <td><s:property value="#case.creatorName" /></td>
                <!--最后执行结果显示-->
                <s:if test="#case.lastResult == 0" >
                  <td>暂无</td>
                </s:if>
                <s:elseif test="#case.lastResult == 1">
                  <td>PASS</td>
                </s:elseif>
                <s:elseif test="#case.lastResult == 2">
                  <td>FAILURE</td>
                </s:elseif>
                <s:elseif test="#case.lastResult == 3">
                  <td>BLOCK</td>
                </s:elseif>
                <!--自动化测试脚本显示-->
                <td><s:property value="scriptName" /></td>
                <td><input type="button" id="<s:property value="#case.case_id"/>" value="详   情" class="btnStyle" onclick="getCaseDetail(this.id)"></td>
                <td><input type="button" id="<s:property value="#case.case_id"/>" value="删   除" class="btnStyle" onclick="deleteCase(this.id)"></td>
                <s:if test="scriptId > 0">
                  <td><input type="button" id="<s:property value="#case.case_id"/>" value="解除绑定" class="btnStyle" onclick="getCaseDetail(this.id)"></td>
                </s:if>
                <s:else>
                  <td><input type="button" id="<s:property value="#case.case_id"/>" value="绑定脚本" class="btnStyle" onclick="getCaseDetail(this.id)"></td>
                </s:else>
              </tr>
            </s:iterator>
           <input type="hidden" id="hidden_eName" value="<s:property value="#case.project"/>"/>
        </table>
      </s:if>
      <s:else>
        <div style="margin-left: 25%;margin-top: 10%">暂无数据……</div>
        <div style="margin-left: 15%;margin-top: 10%">
            <label>暂无测试用例，</label>
            <s:file id="file_btn" style="display: none;" name="upload" value="" onchange="checkFile(this)"></s:file>
            <a href="#" class="aStyle" onclick="fileOpenDialog()">上传</a>
            <label>用例，需</label>
            <a href="javascript:;" class="aStyle" id="download" onclick="downloadFile()">点击此处</a>
            <label>下载用例模板</label>
            <input type="hidden" id="hidden_catalogId" name="catalog_id" value="<s:property value="#request.catalog_id"/>">
            <input type="hidden" name="is_search" value="<s:property value="#request.is_search"/>">
          <s:fielderror style="color:red" name="fileError"/>
        </div>
      </s:else>
    </div>
    <!--分页相关代码，暂时备注-->
    <%--<s:if test="#request.flag == 'allList'">--%>
    <s:if test="#request.pageBean.list.size() > 0">
        <div style="text-align: center" class="textMar">
          <label>共</label><s:property value="#request.pageBean.totalPage" /><label>页</label>
          <label>共</label><s:property value="#request.pageBean.allRows" /><label>条记录</label>

          <s:if test="#request.pageBean.currentPage == 1">
            首页&nbsp;&nbsp;&nbsp;上一页
          </s:if>
          <s:else>
            <a href="#" id="firstPage" onclick="changePage(this.id)">首页</a>
            &nbsp;&nbsp;&nbsp;
            <a href="#" id="opevPage" onclick="changePage(this.id)">上一页</a>
          </s:else>
          <s:if test="#request.pageBean.currentPage != #request.pageBean.totalPage">
            <a href="#" id="nextPage" onclick="changePage(this.id)">下一页</a>
            &nbsp;&nbsp;&nbsp;
            <a href="#" id="lastPage" onclick="changePage(this.id)">尾页</a>
          </s:if>
          <s:else>
            下一页&nbsp;&nbsp;&nbsp;尾页
          </s:else>
          <label>跳转至</label><s:textfield id="searchPage" style="width:20px" value=""/>页
          <input type="button" value="跳转" onclick="goPage()"/>
        </div>
        <input type="hidden" id="page" name="page">
    </s:if>
    <%--</s:if>--%>
  </div>
</s:form>
</body>
</html>
