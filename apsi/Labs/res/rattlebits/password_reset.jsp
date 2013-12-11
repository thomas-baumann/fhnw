<html>
	<head>
		<title>APSI Lab2</title>
		<h1>Rattle Bits</h1>
	</head>
	<body>
		<form method="POST" action="/rattleBits" name="login">
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
					<th>Neues Passwort bestätigen</th>
					<td><input type="password" name="newpassconf" value="${param.newpassconf}"/></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" name="reset" value="Zurücksetzen"/>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>