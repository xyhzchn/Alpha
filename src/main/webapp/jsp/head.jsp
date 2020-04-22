<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
 <div id="headDivStyle">
    <label style="font-size: large;color:#6C6C6C;"><s:property value="#session.userName" /> </label>
    <a href="<%=request.getContextPath()%>/Login.jsp" class="aStyle">登出</a>
 </div>

