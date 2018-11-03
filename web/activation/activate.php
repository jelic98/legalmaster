<?php
	require "config.php";

	if(isset($_GET['kod'])) {
		$kod = $_GET['kod'];

		$cmd = "SELECT * FROM `korisnik` WHERE `kod`='" . $kod . "';";
		$rows = mysqli_query($connect, $cmd);

		$number_of_rows = mysqli_num_rows($rows);

		if($number_of_rows > 0) {
			while($row = mysqli_fetch_array($rows)) {
				$aktivan = $row['aktivan'];
			}

			if($aktivan == "0") {
				$cmd = "UPDATE `korisnik` SET `aktivan`='1' WHERE `kod`='".$kod."';";
				mysqli_query($connect, $cmd);

				echo "true";
			}else {
				echo "false";
			}
		}else {
			echo "false";
		}
	}
?>

