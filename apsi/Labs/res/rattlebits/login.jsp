<%@ page import="java.util.*" %>
<html>
	<head>
		<title>APSI Lab2</title>
		<h1>Rattle Bits</h1>
	</head>
	<body>
		<form method="POST" action="/rattlebits/RattleBitsFront?page=login" name="login">
			<table>
				<tr>
					<th>Benutzername</th>
					<td><input type="text" name="username" value="${param.username}"/></td>
				</tr>
				<tr>
					<th>Passwort</th>
					<td><input type="password" name="password" value="${param.password}"/></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" name="login" value="Login"/>
						<a href="/rattlebits/RattleBitsFront?page=register">Registrieren</a>
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