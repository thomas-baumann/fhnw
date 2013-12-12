<%@page import="java.util.List"%>
<% List errors = (List) session.getAttribute("errors"); %>
<html>
	<head>
		<title>APSI Lab2</title>
		<h1>Rattle Bits</h1>
	</head>
	<body>
		<a href="/rattlebits/RattleBitsFront?page=login">Login</a> <a href="/rattlebits/RattleBitsFront?page=register">Registrieren</a>
	</body>
</html>