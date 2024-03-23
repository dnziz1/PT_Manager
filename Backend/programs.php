<?php
	//Set the content type to text/plain
	header('Content-Type: text/plain');
	
	if (session_status() == PHP_SESSION_NONE) {
		//Start session
		session_start();
	}
 
	// Include the sessionData.php file
	include_once 'sessionData.php';
 
	//Immediately check for any existing session data
	if (!isset($_SESSION['userId'])) {
		echo "Session unavailable\r\n\n";
	}
	
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
//		case "gsess":
//			// get user session data
//			if (isset($_SESSION['userId'])) {
//				//
//				echo "Session data retrieved successful\r\n\n";
//				//Send session data over as JSON
//				getSession();
//			} else {
//				echo "Session unavailable";
//			}
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
		case "gpe":
			// arg2 = programID
			getProgramEvents ($conn,$arg2);
			break;
		case "ipe":
			// arg2 = programID, arg3 = dayID, arg4 = workoutID, arg5 = notes
			insertProgramEvent ($conn,$arg2,$arg3,$arg4, $arg5);
			break;
		case "upe":
			// arg2 = programID, arg3 = dayID, arg4 = eventID, arg5 = workoutID, arg6 = notes
			updateProgramEvent ($conn,$arg2,$arg3,$arg4, $arg5, $arg6); 
			break;
		case "dpe":
			// arg2 = programID, arg3 = dayID, arg4 = eventID
			deleteProgramEvent ($conn,$arg2,$arg3,$arg4); 
			break;
		case "upae":
			updateProgramAndEvents ($conn,$arg2,$arg3,$arg4,$arg5);
			// arg2 = programID, arg3 = program name, arg4 = duration, arg5 = notes
			break;
		case "gaw":
			getAllWorkouts ($conn); 
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
			$arr = ["status" => "Error","msg" => "PDO exception retrieving programs: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				$data[] = $row;
			}
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Programs retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No programs found","data" => Null];
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
			$arr = ["status" => "Error","msg" => "PDO exception retrieving programs: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				$data[] = $row;
			}
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Programs retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No programs found","data" => Null];
			echo json_encode($arr);
		}
	}


	function getProgramByID($conn,$progID) {
		// GET A SPECIFIC PROGRAM FROM THE PROGRAMS TABLE

		//if(empty($progID)) {
		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Program not retrieved - Invalid blank ProgramID"];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM programs WHERE programID = :progID");
			$stmt->bindParam(':progID', $progID, PDO::PARAM_INT);
			
			$stmt->execute();
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception retrieving program: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				$data[] = $row;
			}
			
			//Display the result as json
			$arr = ["status" => "OK","msg" => "Program retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is Error as the ProgramID was not found
			$arr = ["status" => "Error","msg" => "No program found - programID: " . $progID];
			echo json_encode($arr);
		}
	}
	
	
	function insertProgram($conn,$progName,$duration,$notes,$trainerID) {
		// INSERT A NEW PROGRAM TO THE PROGRAMS TABLE

		if($progName == Null) {
			$arr = ["status" => "Error","msg" => "Program not added - Invalid blank program name"];
			die(json_encode($arr));
		}
		
		if($duration == Null) {
			$arr = ["status" => "Error","msg" => "Program not added - Invalid blank duration"];
			die(json_encode($arr));
		}
		
		if($trainerID == Null) {
			$arr = ["status" => "Error","msg" => "Program not added - Invalid blank trainer ID"];
			die(json_encode($arr));
		}
		
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
				$arr = ["status" => "OK","msg" => "Program added successfully: " . $progID,"programID" => $progID];
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
			$arr = ["status" => "Error","msg" => "Program not updated - Invalid blank programID"];
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
		// DELETE A SPECIFIC PROGRAM FROM THE PROGRAMS TABLE WHICH ALSO DELETES ALL EVENTS (FROM THE PROGRAM EVENTS TABLE) FOR THIS PROGRAM

		//if(empty($progID)) {
		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Program not deleted - Invalid blank programID"];
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
			$arr = ["status" => "Error","msg" => "PDO exception deleting program: " . $e->getMessage()];
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
			$arr = ["status" => "Error","msg" => "PDO exception retrieving trainers: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				$data[] = $row;
			}
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Trainers retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No trainers found","data" => Null];
			echo json_encode($arr);
		}
	}

	function getProgramEvents($conn,$progID) {
		// GET ALL PROGRAM EVENTS FROM THE PROGRAM_EVENTS TABLE FOR A SPECIFIC PROGRAM

		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Program events not retrieved - Invalid blank ProgramID"];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM program_events INNER JOIN workouts ON program_events.workoutID = workouts.workoutID WHERE programID = :progID ORDER BY dayID,eventID");
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
			$arr = ["status" => "OK","msg" => "Program events retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No program events found","data" => Null];
			echo json_encode($arr);
		}
	}


	function insertProgramEvent($conn,$progID,$dayID,$workoutID,$notes) {
		// INSERT A NEW EVENT TO THE PROGRAM_EVENTS TABLE
		
		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not added - Invalid blank programID"];
			die(json_encode($arr));
		}
		if($dayID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not added - Invalid blank dayID"];
			die(json_encode($arr));
		}
		if($workoutID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not added - Invalid blank workoutID"];
			die(json_encode($arr));
		}

		
		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("INSERT INTO program_events (programID, dayID, workoutID, notes) VALUES ( :progID, :dayID, :workoutID, :notes)");
			$stmt->bindParam(':progID', $progID, PDO::PARAM_INT);
			$stmt->bindParam(':dayID', $dayID, PDO::PARAM_INT);
			$stmt->bindParam(':workoutID', $workoutID, PDO::PARAM_INT);
			$stmt->bindParam(':notes', $notes, PDO::PARAM_STR);
			
			//echo $stmt;
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				// Return the eventID just created
				$eventID = $conn->lastInsertId();

				$arr = ["status" => "OK","msg" => "Program event added successfully:" . $eventID,"eventID" => $eventID];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Program event not added successfully (" . $progid . "," . $dayID . "," . $eventID . ")"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception adding program event (" . $progID . "," . $dayID . "," . $eventID . "): " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
	function updateProgramEvent($conn,$progID,$dayID,$eventID,$workoutID,$notes) {
		// CODE FOR UPDATING ROWS IN THE PROGRAM EVENTS TABLE

		//if(empty($progID)) {
		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not updated - Invalid blank programID"];
			die(json_encode($arr));
		}
		if($dayID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not updated - Invalid blank dayID"];
			die(json_encode($arr));
		}
		if($eventID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not updated - Invalid blank eventID"];
			die(json_encode($arr));
		}
		if($workoutID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not updated - Invalid blank workoutID"];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("UPDATE program_events SET workoutID = :workoutID, notes = :notes WHERE programID = :progID AND dayID = :dayID AND eventID = :eventID");
			$stmt->bindParam(':progID', $progID, PDO::PARAM_INT);
			$stmt->bindParam(':dayID', $dayID, PDO::PARAM_INT);
			$stmt->bindParam(':eventID', $eventID, PDO::PARAM_INT);
			$stmt->bindParam(':workoutID', $workoutID, PDO::PARAM_INT);
			$stmt->bindParam(':notes', $notes, PDO::PARAM_STR);
			
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				$arr = ["status" => "OK","msg" => "Program event updated successfully"];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Program event not updated successfully (" . $progid . "," . $dayID . "," . $eventID . ")"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception updating program event (" . $progid . "," . $dayID . "," . $eventID . "): " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
	function deleteProgramEvent($conn,$progID,$dayID,$eventID) {
		// DELETE A SPECIFIC PROGRAM EVENT FROM THE PROGRAM EVENTS TABLE

		//if(empty($progID)) {
		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not deleted - Invalid blank programID"];
			die(json_encode($arr));
		}
		if($dayID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not deleted - Invalid blank dayID"];
			die(json_encode($arr));
		}
		if($eventID == Null) {
			$arr = ["status" => "Error","msg" => "Program event not deleted - Invalid blank eventID"];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			
			$stmt = $conn->prepare("DELETE FROM program_events WHERE programID = :progID AND dayID = :dayID AND eventID = :eventID");
			$stmt->bindParam(':progID', $progID, PDO::PARAM_INT);
			$stmt->bindParam(':dayID', $dayID, PDO::PARAM_INT);
			$stmt->bindParam(':eventID', $eventID, PDO::PARAM_INT);
			
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				$arr = ["status" => "OK","msg" => "Program event deleted successfully"];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Program event not deleted successfully (" . $progid . "," . $dayID . "," . $eventID . ")"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception deleting program event (" . $progid . "," . $dayID . "," . $eventID . "): " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
		function updateProgramAndEvents($conn,$progID,$progName,$duration,$notes) {
		// CODE FOR UPDATING ROWS IN THE PROGRAMS TABLE

		//if(empty($progID)) {
		if($progID == Null) {
			$arr = ["status" => "Error","msg" => "Program and events not updated - Invalid blank programID"];
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
				// update program was successful so now also remove any events for days that are greater than the new duration
				// prepare sql and bind parameters
				$stmt = $conn->prepare("DELETE FROM program_events WHERE programID = :progID AND dayID > :newDuration");
				$stmt->bindParam(':progID', $progID, PDO::PARAM_INT);
				$stmt->bindParam(':newDuration', $duration, PDO::PARAM_INT);
				
				$stmt->execute();
				$arr = ["status" => "OK","msg" => "Program and events updated successfully"];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Program and events not updated successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception updating program and events: " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
		function getAllWorkouts($conn) {
		// GET ALL WORKOUTS FROM THE WORKOUTS TABLE

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM workouts");
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
			$arr = ["status" => "OK","msg" => "Workouts retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No workouts found","data" => Null];
			echo json_encode($arr);
		}
	}



?>
