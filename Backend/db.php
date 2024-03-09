<?php

	//Database server settings (CHANGE TO YOUR OWN SETTINGS)
	$servername = "localhost";
	$username = "root";
	$password = "test";
	$dbname = "appDB";

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
