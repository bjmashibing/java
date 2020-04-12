<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2020/3/15
  Time: 21:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Title</title>
</head>
<%
    pageContext.setAttribute("ctp",request.getContextPath());
%>
<body>
<%--<form action="${ctp}/validation" method="post">--%>
<%--    编码：<input type="text" name="id">${errors.id}<br>--%>
<%--    姓名：<input type="text" name="name">${errors.name}<br>--%>
<%--    年龄：<input type="text" name="age">${errors.age}<br>--%>
<%--    性别：<input type="text" name="gender">${errors.gender}<br>--%>
<%--    生日：<input type="text" name="birth">${errors.birth}<br>--%>
<%--    邮箱：<input type="text" name="email">${errors.email}<br>--%>
<%--    <input type="submit" value="login"><br>--%>
<%--</form>--%>
<form:form action="dataValidate"  modelAttribute="user" method="post">
    id:<form:input path="id"></form:input><form:errors path="id"></form:errors> <br/>
    name:<form:input path="name"></form:input><form:errors path="name"></form:errors><br/>
    age:<form:input path="age"></form:input><form:errors path="age"></form:errors><br/>
    gender:<form:input path="gender"></form:input><form:errors path="gender"></form:errors><br/>
    birth:<form:input path="birth"></form:input><form:errors path="birth"></form:errors><br/>
    email:<form:input path="email"></form:input><form:errors path="email"></form:errors><br/>
    <input type="submit" value="submit">
</form:form>
</body>
</html>
