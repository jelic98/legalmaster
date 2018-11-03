<?php
	require "config.php";

	function generate_code() {
		$code = "";
		$code_chars = array_merge(range("A", "Z"), range("a", "z"), range(0, 9));

		for($i = 0; $i < 10; $i++) {
			$code .= $code_chars[array_rand($code_chars)];
		}	

		return $code;
	}
	
	if(isset($_GET['ime'])) {
		$ime = $_GET['ime'];
	
		$cmd = "INSERT INTO `korisnik`(`ime`, `kod`, `aktivan`) VALUES ('".$ime."', '".generate_code()."', '0');";	
		mysqli_query($connect, $cmd);

		mysqli_close($connect);
	}

	header("location: index.php");
?>

