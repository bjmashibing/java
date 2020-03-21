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
<form action="${ctp}/converter" method="post">
  <input type="text" name="user"><br>
  <input type="submit" value="提交">
</form>
 <%-- <img src="${ctp}/images/timg.jpg">--%>
  </body>
</html>
