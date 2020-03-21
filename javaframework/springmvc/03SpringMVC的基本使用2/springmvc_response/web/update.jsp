<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2020/3/15
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<%
    pageContext.setAttribute("ctp",request.getContextPath());
%>
<body>
<form action="${ctp}/update">
    <input type="hidden" value="1" name="id"><br>
    name:张三<br>
    password:<input type="password" name="password"><br>
    age:<input type="text" name="age"><br>
    <input type="submit" value="更新"><br>
</form>
</body>
</html>
