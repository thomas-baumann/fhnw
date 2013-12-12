<%@ page import="java.util.*" %>
<html>
	<head>
		<title>APSI Lab2</title>
		<h1>Rattle Bits</h1>
	</head>
	<body>
		<h5>Hello there!</h5>
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