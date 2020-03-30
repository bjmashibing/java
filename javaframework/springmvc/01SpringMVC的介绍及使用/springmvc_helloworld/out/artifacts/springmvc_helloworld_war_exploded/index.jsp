<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2020/3/14
  Time: 15:44
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
  <form action="${ctp}/hello/hello" method="post">
    <input type="text" name="username"><br>
    <input type="submit" value="提交">
  </form>
  </body>
</html>
