<?php
	//Set the content type to text/plain
	header('Content-Type: text/plain');
	
	//Database server settings	
	include "db.php";

	//Check for argument1
	if (isset($_GET['arg1'])) {
		$arg1 = strval($_GET['arg1']);
	} else {
		$arg1 = Null;
	}

	//Check for argument2
	if (isset($_GET['arg2'])) {
		$arg2 = strval($_GET['arg2']);
	} else {
		$arg2 = Null;
	}

	//Check for argument3
	if (isset($_GET['arg3'])) {
		$arg3 = strval($_GET['arg3']);
	} else {
		$arg3 = Null;
	}

	//Check for argument4
	if (isset($_GET['arg4'])) {
		$arg4 = strval($_GET['arg4']);
	} else {
		$arg4 = Null;
	}

	//echo $_SERVER['REQUEST_METHOD'];
	
	if ($_SERVER['REQUEST_METHOD'] == 'GET') {
		readPrograms($conn,$arg1);
	} else if ($_SERVER['REQUEST_METHOD'] == 'POST') {
		insertProgram($conn,$arg1,$arg2,$arg3); 
	} else if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
		updatePrograms($conn,$arg1,$arg2,$arg3,$arg4);
	} else if ($_SERVER['REQUEST_METHOD'] == 'DELETE') {
		deletePrograms($conn,$arg1,$arg2);
	} else {
			$arr = ["status" => "Error","msg" => "Invalid HTTP request method: " . $_SERVER['REQUEST_METHOD']];
			die(json_encode($arr));
	}
	
	$stmt = null;
	$conn = null;

	function readPrograms($conn,$arg1) {
		// CODE FOR SELECTING ROWS TO THE PROGRAMS TABLE
		// arg1 is optional to select a specific programID

		try {
			// prepare sql and bind parameters
			if(empty($arg1)) {
				$stmt = $conn->prepare("SELECT * FROM programs");
			} else {
				$stmt = $conn->prepare("SELECT * FROM programs WHERE programID = :progID");
				$stmt->bindParam(':progID', $arg1, PDO::PARAM_INT);
			}
			
			//echo $sql;
			$stmt->execute();
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				$data[] = $row;
			}
			
			//Display the result as json
			$arr = ["status" => "OK","msg" => "Data retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No rows found"];
			echo json_encode($arr);
		}
	}
	
	function insertProgram($conn,$arg1,$arg2,$arg3) {
		// CODE FOR INSERTING ROWS TO THE PROGRAMS TABLE
		// arg1 must contain the userid, arg2 must contain the email address and arg3 must contain the password
		
		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("INSERT INTO programs (userID, email,password) VALUES ( :userID , :email , :pwd)");
			$stmt->bindParam(':userID', $arg1, PDO::PARAM_INT);
			$stmt->bindParam(':email', $arg2, PDO::PARAM_STR);
			$stmt->bindParam(':pwd', $arg3, PDO::PARAM_STR);
			
			//echo $sql;
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				// Return the programID just created
				$progID = $conn->lastInsertId();
				$arr = ["status" => "OK","msg" => "Program added successfully","progID" => $progID];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Program not added successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception adding program: " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
	function updatePrograms($conn,$arg1,$arg2,$arg3,$arg4) {
		// CODE FOR UPDATING ROWS IN THE PROGRAMS TABLE
		// arg1 must contain the userid, arg2 is optional to apply the update to a specific programID, arg3 must contain the email address, arg4 must contain the password

		try {
			// prepare sql and bind parameters
			if(empty($arg2)) {
				$stmt = $conn->prepare("UPDATE programs SET email = :email, password = :pwd WHERE userID = :userID");
			} else {
				$stmt = $conn->prepare("UPDATE programs SET email = :email, password = :pwd WHERE userID = :userID AND programID = :progID");
				$stmt->bindParam(':progID', $arg2, PDO::PARAM_INT);
			}

			$stmt->bindParam(':userID', $arg1, PDO::PARAM_INT);
			$stmt->bindParam(':email', $arg3, PDO::PARAM_STR);
			$stmt->bindParam(':pwd', $arg4, PDO::PARAM_STR);
			
			echo $sql;
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				$arr = ["status" => "OK","msg" => "Program/s updated successfully"];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Program/s not updated successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception updating program/s: " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
	function deletePrograms($conn,$arg1,$arg2) {
		// CODE FOR DELETING ROWS FROM THE PROGRAMS TABLE
		// arg1 must contain the userID, arg2 is optional to apply the delete to a specific programID
		try {
			// prepare sql and bind parameters
			if(empty($arg2)) {
				$stmt = $conn->prepare("DELETE FROM programs WHERE userID = :userID");
			} else {
				$stmt = $conn->prepare("DELETE FROM programs WHERE userID = :userID AND programID = :progID");
				$stmt->bindParam(':progID', $arg2, PDO::PARAM_INT);
			}

			$stmt->bindParam(':userID', $arg1, PDO::PARAM_INT);
			
			echo $sql;
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				$arr = ["status" => "OK","msg" => "Program/s deleted successfully"];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Program/s not deleted successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception deleting program/s: " . $e->getMessage()];
			die(json_encode($arr));
		}
	}

?>
