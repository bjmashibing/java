<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2020/3/21
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<script src="scripts/jquery-1.9.1.min.js"></script>
<head>
    <title>Title</title>
</head>
<%
    pageContext.setAttribute("ctp",request.getContextPath());
%>
<body>
<form action="${ctp}/test" method="post">
    <input name="username" value="zhangsan"><br>
    <input name="password" value="123456"><br>
    <input type="file" name="file"><br>
    <input type="submit" value="提交">
</form>
<hr/>
<a href="${ctp}/testRequestJson">发送接送数据</a>
</body>
<script type="text/javascript">
    $("a:first").click(function(){
        var user = {id:"1",name:"zhangsan",age:"12",gender:"man"};
        var jsonuser = JSON.stringify(user);
        $.ajax({
            url:"${ctp}/testRequestJson",
            type:"post",
            data:jsonuser,
            contentType:"application/json",
            success:function (data) {
                alert(data)
            }
        });
        return false;
    })
</script>
</html>
