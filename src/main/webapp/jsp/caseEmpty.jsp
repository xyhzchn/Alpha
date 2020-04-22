
<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/8/1
  Time: 11:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script type="text/javascript" language="JavaScript">
      //模板文件自动下载
      function downloadFile() {
          //获取目录id
          var catalog_id = $("#hidden_catalogId").val();
          window.location.href="download.action?catalog_id="+catalog_id;
      };
      //弹出文件浏览框
      function fileOpenDialog() {
          $("#btn_file").click();
      }
      //判断上传文件类型
      function checkImportFile(obj){
          //获取当前所在的目录id
          var catalog_id = $("#hidden_catalogId").val();
          //如果文件为空
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
                document.getElementById("form1").action ='uploadFile.action?catalog_id='+catalog_id;
                document.getElementById("form1").submit();
            }
          }
      }
  </script>
</head>
  <body class="bodyTextStyle">
  <div class="context">
    <div style="margin: 25%">
      <s:form id="form1" theme="simple" enctype="multipart/form-data" name="form1">
        <label>暂无测试用例，</label>
        <s:file id="btn_file" style="display: none;" name="upload" value="" onchange="checkImportFile(this)"></s:file>
        <a href="#" class="aStyle" onclick="fileOpenDialog()">上传</a>
        <label>用例，需</label>
        <a href="javascript:;" class="aStyle" id="download" onclick="downloadFile()">点击此处</a>
        <label>下载用例模板</label>
        <input type="hidden" id="hidden_catalogId" value="<s:property value="#request.catalog_id" />">
        <s:fielderror style="color:red" name="fileError"/>
      </s:form>
    </div>
  </div>
  </body>
</html>
