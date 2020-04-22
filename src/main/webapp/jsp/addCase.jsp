<%--
  Created by IntelliJ IDEA.
  User: guoxx
  Date: 2018/3/14
  Time: 15:21
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
        <script src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                //验证用户名和密码
                $('#caseForm').validate({
                    rules:{
                        "acase.case_id":{required:true},
                        "acase.testStep":{required:true},
                        "acase.expectedRes":{required:true}
                    },
                    messages:{
                        "acase.case_id":{
                            required:"用例编号不可为空"
                        },
                        "acase.testStep":{
                            required:"测试步骤不可为空"
                        }
                        ,
                        "acase.expectedRes":{
                            required:"预期结果不可为空"
                        }
                    }
                });
            });
        </script>
    </head>
    <body>

        <s:form id="caseForm" theme="simple" method="post" action="addSingleCase">
            <div>
                <table style="margin-bottom:10px;font-size: 12px;color: #666;">
                    <tr>
                        <td>
                            <%--<label>用例编号：</label><s:textfield class="textStyle" name="acase.case_id"/> </br>--%>
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
                <input type="hidden" name="catalog_id" value="<s:property value="#request.catalog_id"/>"/>
                <input type="button" class="btnStyle" value="返   回" onclick="history.back()">
                <s:submit value="保   存" class="btnStyle"/>
            </div>
        </s:form>
</body>
</html>
