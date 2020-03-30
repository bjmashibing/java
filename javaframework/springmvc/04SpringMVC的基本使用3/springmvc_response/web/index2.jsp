<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2020/3/15
  Time: 15:03
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
666666666<br>
请按照id-name-age-password的方式输入数据<br>
<form action="${ctp}/testDateConverter" method="post">
  id:<input type="text" name="id"><br>
  name:<input type="text" name="name"><br>
  age:<input type="text" name="age"><br>
  password:<input type="password" name="password"><br>
  birth:<input type="text" name="birth"><br>
  <input type="submit" value="提交">
</form>
 <%-- <img src="${ctp}/images/timg.jpg">--%>
  </body>
</html>
