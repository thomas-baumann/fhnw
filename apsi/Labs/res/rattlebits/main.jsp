<%@ page import="java.util.*,lab2.model.Company" %>
<html>
	<head>
		<title>APSI Lab2</title>
		<h1>Rattle Bits</h1>
	</head>
	<body>
		<h5>Hello there!</h5>
		<% Company c = (Company)request.getAttribute("company"); %>
		<dl>
			<dt>Firmenname:</dt>
			<dd><%= c.getName() %></dd>
			<dt>Benutzername:</dt>
			<dd><%= c.getUsername() %></dd>
			<dt>E-Mail Adresse;:</dt>
			<dd><%= c.getEmail() %></dd>
			<dt>Adresse:</dt>
			<dd><%= c.getAddress() %></dd>
			<dt>PLZ Stadt:</dt>
			<dd><%= c.getPostcode() %> <%= c.getTown() %></dd>
		</dl>
		<ul style="color:green;">
		<%
		    List<String> errors = (List<String>)request.getAttribute("success");
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
		<a href="/rattlebits/RattleBitsFront?page=logout">Logout</a>
		<a href="/rattlebits/RattleBitsFront?page=password_reset">Passwort zur&uumlcksetzen</a>
	</body>
</html>