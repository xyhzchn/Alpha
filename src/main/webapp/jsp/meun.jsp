<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/7/27
  Time: 13:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <!--
    <link rel="stylesheet" href="/resources/demos/style.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>-->
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
      jQuery(document).ready(function($) {
          $('.theme-login').click(function(){
              $('.theme-popover-mask').fadeIn(100);
              $('.theme-popover').slideDown(200);
          })
          $('.theme-poptit .close').click(function(){
              $('.theme-popover-mask').fadeOut(100);
              $('.theme-popover').slideUp(200);
          })

      })
  </script>
</head>
<body>
<div class="theme-buy">
  <a class="btn btn-primary btn-large theme-login" href="javascript:;">点击查看效果</a>
</div>
<div class="theme-popover">
  <div class="theme-poptit">
    <a href="javascript:;" title="关闭" class="close">×</a>
    <h3>登录 是一种态度</h3>
  </div>
  <div class="theme-popbod dform">
    <form class="theme-signin" name="loginform" action="" method="post">
      <ol>
        <li><h4>你必须先登录！</h4></li>
        <li><strong>用户名：</strong><input class="ipt" type="text" name="log" value="lanrenzhijia" size="20" /></li>
        <li><strong>密码：</strong><input class="ipt" type="password" name="pwd" value="***" size="20" /></li>
        <li><input class="btn btn-primary" type="submit" name="submit" value=" 登 录 " /></li>
      </ol>
    </form>
  </div>
</div>
<div class="theme-popover-mask"></div>
</body>
</html>
