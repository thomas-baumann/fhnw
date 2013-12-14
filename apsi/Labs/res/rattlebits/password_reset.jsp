<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>APSI Lab2</title>
	</head>
	<body>
		<h1>Rattle Bits</h1>
		<form method="POST" action="/rattlebits/?page=password_reset" name="login">
			<table>
				<tr>
					<th>Altes Passwort</th>
					<td><input type="password" name="oldpass" value="<c:out value="${param.oldpass}"/>"/></td>
				</tr>
				<tr>
					<th>Neues Passwort</th>
					<td><input type="password" name="newpass" value="<c:out value="${param.newpass}"/>"/></td>
				</tr>
				<tr>
					<th>Neues Passwort best&aumltigen</th>
					<td><input type="password" name="newpassconf" value="<c:out value="${param.newpassconf}"/>"/></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" name="reset" value="Zur&uuml;cksetzen"/>
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