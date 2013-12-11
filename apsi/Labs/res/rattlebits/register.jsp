<html>
	<head>
		<title>APSI Lab2</title>
		<h1>Rattle Bits</h1>
	</head>
	<body>
		<form method="POST" action="/rattlebits" name="register">
			<table>
				<tr>
					<th>Firmenname</th>
					<td><input type="text" name="companyname" value="${param.companyname}"/></td>
				</tr>
				<tr>
					<th>Strasse</th>
					<td><input type="text" name="address" value="${param.address}"/></td>
				</tr>
				<tr>
					<th>PLZ</th>
					<td><input type="text" name="plz" value="${param.plz}"/></td>
				</tr>
				<tr>
					<th>Stadt</th>
					<td><input type="text" name="city" value="${param.city}"/></td>
				</tr>
				<tr>
					<th>E-Mail</th>
					<td><input type="text" name="mail" value="${param.mail}"/></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" name="register" value="Registrieren"/>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>