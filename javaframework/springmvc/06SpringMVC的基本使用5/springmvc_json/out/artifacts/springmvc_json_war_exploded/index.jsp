<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2020/3/21
  Time: 14:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <%
    pageContext.setAttribute("ctp",request.getContextPath());
  %>
  <body>
  <a href="${ctp}/i18n">国际化页面</a>
  <a href="${ctp}/exception1">触发异常</a>
  </body>
</html>
