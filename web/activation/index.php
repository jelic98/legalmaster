<html>
	<head>
		<title>Dodaj korisnika</title>
	</head>
	<body>
		<form action="add.php" method="get">
			<input type="text" name="ime" placeholder="Ime korisnika">
			<input type="submit" value="Dodaj">
		</form>
		<?php
			required "config.php";

			$cmd = "SELECT * FROM `korisnik`;";
			$result = mysqli_query($connect, $cmd);

			while($row = mysqli_fetch_array($result)){
				echo $row['ime']." ".$row['kod']." ".$row['aktivan']."<br/>";
			}

			mysqli_close($connect);
		?>

	</body>
</html>
