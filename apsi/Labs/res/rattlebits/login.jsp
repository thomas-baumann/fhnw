<%@ page import="java.util.*,org.apache.commons.lang3.StringEscapeUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>APSI Lab2</title>
	</head>
	<body>
		<h1>Rattle Bits</h1>
		<form method="POST" action="/rattlebits/?page=login" name="login">
			<table>
				<tr>
					<th>Benutzername</th>
					<td><input type="text" name="username" value="<c:out value="${param.username}"/>"/></td>
				</tr>
				<tr>
					<th>Passwort</th>
					<td><input type="password" name="password" value="<c:out value="${param.password}"/>"/></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" name="login" value="Login"/>
						<a href="/rattlebits/?page=register">Registrieren</a>
					</td>
				</tr>
			</table>
		</form>
		<ul style="color:red;">
		<%
		    List<String> errors = (List<String>)request.getAttribute("errors");
		    if (errors != null) {
				Iterator<String> it = errors.iterator();
			    while (it.hasNext()) {
		%>
	        <li><%= it.next() %></li>
		<% 
			} 
		}
		%>
		</ul>
	</body>
</html>