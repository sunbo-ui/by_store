<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<meta http-equiv="Access-Control-Allow-Origin" content="*"/>
<meta charset="UTF-8"/>
<body>

<h2>努力跳转中~</h2>
<%
   request.getRequestDispatcher("/username").forward(request,response);
%>
</body>
</html>
