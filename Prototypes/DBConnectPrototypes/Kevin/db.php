<?php

	//Database server settings
	$servername = "localhost:3316";
	$username = "Kevin";
	$password = "";
	$dbname = "co5590";

	try {
		$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
		// set the PDO error mode to exception
		$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		//$arr = ["status" => "OK","msg" => "Database connection was successful"];
		//echo json_encode($arr);
	} catch(PDOException $e) {
		//die "Connection failed: " . $e->getMessage();
		$arr = ["status" => "Error","msg" => "PDO exception: " . $e->getMessage()];
		die(json_encode($arr));
	}
?>
