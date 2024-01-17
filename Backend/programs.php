<?php
	//Set the content type to text/plain
	header('Content-Type: text/plain');
	
	//Database server settings	
	include "db.php";

	// initialise argument fields
	$arg1 = $arg2 = $arg3 = $arg4 = $arg5 = $arg6 = $arg7 = $arg8 = $arg9 = $arg10 = NULL;
	
	//Check for passed in arguments and store if so
	if (isset($_GET['arg1'])) $arg1 = strval($_GET['arg1']);
	if (isset($_GET['arg2'])) $arg2 = strval($_GET['arg2']);
	if (isset($_GET['arg3'])) $arg3 = strval($_GET['arg3']);
	if (isset($_GET['arg4'])) $arg4 = strval($_GET['arg4']);
	if (isset($_GET['arg5'])) $arg5 = strval($_GET['arg5']);
	if (isset($_GET['arg6'])) $arg6 = strval($_GET['arg6']);
	if (isset($_GET['arg7'])) $arg7 = strval($_GET['arg7']);
	if (isset($_GET['arg8'])) $arg8 = strval($_GET['arg8']);
	if (isset($_GET['arg9'])) $arg9	= strval($_GET['arg9']);
	if (isset($_GET['arg10'])) $arg10 = strval($_GET['arg10']);

	// arg1 is the identifier that determines which SQL function to run
	switch ($arg1) {
		case "gap":
			getAllPrograms($conn);
			break;
		case "gpbi":
			getProgramByID($conn,$arg2); // arg2 = programID	
			break;
		case "gpbf":
			getProgramsByFilter ($conn,$arg2,$arg3,$arg4,$arg5); 
			// arg2 = optional program name criteria, arg3 = optional min days, arg4 = optional max days, arg5 = optional trainerID 
			break;
		case "ip":
			insertProgram ($conn,$arg2,$arg3,$arg4,$arg5); 
			// arg2 = program name, arg3 = duration (no of days), arg4 = program notes, arg5 = trainerID
			break;
		case "up":
			updateProgram ($conn,$arg2,$arg3,$arg4, $arg5); 
			// arg2 = programID, arg3 = program name, arg4 = duration, arg5 = notes
			break;
		case "dp":
			deleteProgram ($conn,$arg2); 
			// arg2 = programID
			break;
		case "gat":
			getAllTrainers ($conn); 
			break;
		default:
			$arr = ["status" => "Error","msg" => "Invalid SQL Identifier: " . $arg1];
			die(json_encode($arr));
	}
	
	$stmt = null;
	$conn = null;

	function getProgramsByFilter($conn,$progNameCriteria,$minDays,$maxDays,$trainerID) {
		// GET PROGRAMS FROM THE PROGRAMS TABLE BASED ON FILTER CRITERIA
		
		// check which selection criteria has been passed in if any
		$progNameSearch = ($progNameCriteria == Null) ? 0 : 1;
		$minDaysSearch = ($minDays == Null) ? 0 : 1;
		$maxDaysSearch = ($maxDays == Null) ? 0 : 1;
		$trainerSearch = ($trainerID == Null) ? 0 : 1;
		
		// build sql based on search criteria
		$sql = "SELECT * FROM programs ";
		$keyword = "WHERE";
				
		// check if program name filter criteria is passed in criteria 
		if($progNameSearch) {
			$progNameLike = "%" . $progNameCriteria . "%";
			$sql = $sql . $keyword . " name like :progNameCriteria ";
			$keyword = "AND";
		}
		
		// check if minimum days is passed in criteria 
		if($minDaysSearch) {
			$sql = $sql . $keyword . " duration >= :minDays ";
			$keyword = "AND";
		}
		
		// check if maximum days is passed in criteria 
		if($maxDaysSearch) { 
			$sql = $sql . $keyword . " duration <= :maxDays ";
			$keyword = "AND";
		}

		// check if searchitrainerID is part of the passed in criteria 
		if($trainerSearch) { 
			$sql = $sql . $keyword . " trainerID = :trainerID ";
			$keyword = "AND";
		}

		//echo $sql;
	
		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare($sql);
			//echo $sql;

			if ($progNameSearch) $stmt->bindParam(':progNameCriteria', $progNameLike, PDO::PARAM_STR);
			if ($minDaysSearch) $stmt->bindParam(':minDays', $minDays, PDO::PARAM_INT);
			if ($maxDaysSearch) $stmt->bindParam(':maxDays', $maxDays, PDO::PARAM_INT);
			if ($trainerSearch) $stmt->bindParam(':trainerID', $trainerID, PDO::PARAM_INT);
			
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
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Data retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No rows found","data" => Null];
			echo json_encode($arr);
		}
	}
	
	function getAllPrograms($conn) {
		// GET ALL PROGRAMS FROM THE PROGRAMS TABLE

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM programs");
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
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Data retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No rows found","data" => Null];
			echo json_encode($arr);
		}
	}

	function getProgramByID($conn,$progID) {
		// GET A SPECIFIC PROGRAM FROM THE PROGRAMS TABLE

		//if(empty($progID)) {
		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Invalid ProgramID: " . $progID];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM programs WHERE programID = :progID");
			$stmt->bindParam(':progID', $progID, PDO::PARAM_INT);
			
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
			// Return code is Error as the ProgramID was not found
			$arr = ["status" => "Error","msg" => "No rows found for programID: " . $progID];
			echo json_encode($arr);
		}
	}
	
	function insertProgram($conn,$progName,$duration,$notes,$trainerID) {
		// INSERT A NEW PROGRAM TO THE PROGRAMS TABLE
		
		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("INSERT INTO programs (name,duration,notes,trainerID) VALUES ( :progName , :duration , :notes, :trainerID)");
			$stmt->bindParam(':progName', $progName, PDO::PARAM_STR);
			$stmt->bindParam(':duration', $duration, PDO::PARAM_STR);
			$stmt->bindParam(':notes', $notes, PDO::PARAM_STR);
			$stmt->bindParam(':trainerID', $trainerID, PDO::PARAM_INT);
			
			//echo $sql;
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				// Return the programID just created
				$progID = $conn->lastInsertId();
				$arr = ["status" => "OK","msg" => "Program added successfully: " . $progID];
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
	
	function updateProgram($conn,$progID,$progName,$duration,$notes) {
		// CODE FOR UPDATING ROWS IN THE PROGRAMS TABLE

		//if(empty($progID)) {
		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Invalid programID: " . $progID];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("UPDATE programs SET name = :progName, duration = :duration, notes = :notes WHERE programID = :progID");
			$stmt->bindParam(':progID', $progID, PDO::PARAM_INT);
			$stmt->bindParam(':progName', $progName, PDO::PARAM_STR);
			$stmt->bindParam(':duration', $duration, PDO::PARAM_INT);
			$stmt->bindParam(':notes', $notes, PDO::PARAM_STR);
			
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				$arr = ["status" => "OK","msg" => "Program updated successfully"];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Program not updated successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception updating program: " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
	function deleteProgram($conn,$progID) {
		// DELETE A SPECIFIC PROGRAM FROM THE PROGRAMS TABLE

		//if(empty($progID)) {
		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Invalid programID:" . $progID];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("DELETE FROM programs WHERE programID = :progID");
			$stmt->bindParam(':progID', $progID, PDO::PARAM_INT);
			
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				$arr = ["status" => "OK","msg" => "Program deleted successfully"];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Program not deleted successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception deleting program/s: " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
		function getAllTrainers($conn) {
		// GET ALL Trainers FROM THE Trainers TABLE

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM trainers");
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
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Data retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No rows found","data" => Null];
			echo json_encode($arr);
		}
	}


?>
