<%--
  Created by IntelliJ IDEA.
  User: Administrator
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    System.out.println(path);
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    System.out.println(basePath);

%>
<html>
<head>
    <base href="<%=basePath%>">

    <title>Title</title>
</head>
<body>
bbbbbbbbbbbbb
<a href="page.jsp">page</a>
</body>
</html>
