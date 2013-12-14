<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>APSI Lab2</title>
	</head>
	<body>
		<h1>Rattle Bits</h1>
		<form method="POST" action="/rattlebits/?page=register" name="register">
			<table>
				<tr>
					<th>Firmenname</th>
					<td><input type="text" name="companyname" value="<c:out value="${param.companyname}"/>"/></td>
				</tr>
				<tr>
					<th>Adresse</th>
					<td><input type="text" name="address" value="<c:out value="${param.address}"/>"/></td>
				</tr>
				<tr>
					<th>PLZ</th>
					<td><input type="text" name="plz" value="<c:out value="${param.plz}"/>"/></td>
				</tr>
				<tr>
					<th>Stadt</th>
					<td><input type="text" name="city" value="<c:out value="${param.city}"/>"/></td>
				</tr>
				<tr>
					<th>E-Mail</th>
					<td><input type="text" name="mail" value="<c:out value="${param.oldpmailass}"/>"/></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" name="register" value="Registrieren"/>
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