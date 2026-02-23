<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

</head>
<body>
<h1>
<%String msg = (String) request.getAttribute("msg");%>

<h3 align='center'><%=msg %></h3>
<jsp:include page="login.html"></jsp:include>
</h1>
</body>
</html>