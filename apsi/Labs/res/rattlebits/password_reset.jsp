<%@ page import="java.util.*" %>
<html>
	<head>
		<title>APSI Lab2</title>
		<h1>Rattle Bits</h1>
	</head>
	<body>
		<form method="POST" action="/rattlebits/RattleBitsFront?password_reset=true" name="login">
			<table>
				<tr>
					<th>Altes Passwort</th>
					<td><input type="password" name="oldpass" value="${param.oldpass}"/></td>
				</tr>
				<tr>
					<th>Neues Passwort</th>
					<td><input type="password" name="newpass" value="${param.newpass}"/></td>
				</tr>
				<tr>
					<th>Neues Passwort best&aumltigen</th>
					<td><input type="password" name="newpassconf" value="${param.newpassconf}"/></td>
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