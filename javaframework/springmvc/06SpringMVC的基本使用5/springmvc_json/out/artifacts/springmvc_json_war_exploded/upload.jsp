<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2020/3/21
  Time: 16:31
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
<form action="${ctp}/upload" method="post" enctype="multipart/form-data">
    描述：<input type="text" name="desc"><br>
    文件：<input type="file" name="file"><br>
    文件：<input type="file" name="file"><br>
    文件：<input type="file" name="file"><br>
    <input type="submit" value="提交">
</form>
</body>
</html>
