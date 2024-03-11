<?php
	//Set the content type to text/plain
	header('Content-Type: text/plain');
	
	//Database server settings	
	include "db.php";
	
	if ($_SERVER['REQUEST_METHOD'] != 'GET') {
     		// Refuse any request method other than GET as the android app currently only uses GET by default for all database queries
		$arr = ["status" => "Error","msg" => "Invalid DB request mode: " . $_SERVER['REQUEST_METHOD']];
		die(json_encode($arr));
	}


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
		case "gac":
			getAllClasses($conn);
			break;
		case "gcbi":
			getClassByID($conn,$arg2); // arg2 = classID	
			break;
		case "gcbf":
			getClassesByFilter ($conn,$arg2,$arg3,$arg4,$arg5); 
			// arg2 = optional class name criteria, arg3 = optional min duration (mins), arg4 = optional max duration (mins), arg5 = optional trainerID 
			break;
//		case "gacb":
//			getAllClassBookings($conn);
//			break;
		case "gcbbc":
			getClassBookingsByClientID($conn,$arg2);
			// arg2 = clientID
			break;
		case "icb":
			insertClassBooking ($conn,$arg2,$arg3); 
			// arg2 = timeslotID, arg3 = clientID
			break;
//		case "ucb":
//			updateClassBooking ($conn,$arg2,$arg3,$arg4, $arg5); 
//			// arg2 = clientID, arg3 = current timeslotID, arg4 = new timeslotID
//			break;
		case "dcb":
			deleteClassBooking ($conn,$arg2,$arg3); 
			// arg2 = timeslotID, arg3 = clientID
			break;
//		case "gact":
//			getAllClassTimeslots($conn);
//			break;
//		case "gctbc":
//			getClassTimeslotsByClassID($conn,$arg2);
//			// arg2 = classID
//			break;
		case "gavct":
			getAvailableClassTimeslots($conn,$arg2);
			// arg2 = classID
			break;
		case "ict":
			insertClassTimeslot ($conn,$arg2,$arg3,$arg4); 
			// arg2 = classID, arg3 = trainerID, arg4 = timestamp
			break;
		case "dct":
			deleteClassTimeslot ($conn,$arg2); 
			// arg2 = timeslotID
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




	function getClassesByFilter($conn,$classNameCriteria,$minDuration,$maxDuration,$trainerID) {
		// GET CLASSES FROM THE CLASSES TABLE BASED ON FILTER CRITERIA
		
		// check which selection criteria has been passed in if any
		$classNameSearch = ($classNameCriteria == Null) ? 0 : 1;
		$minDurationSearch = ($minDuration == Null) ? 0 : 1;
		$maxDurationSearch = ($maxDuration == Null) ? 0 : 1;
		$trainerSearch = ($trainerID == Null) ? 0 : 1;
		
		// build sql based on search criteria
		$sql = "SELECT * FROM classes ";
		$keyword = "WHERE";
				
		// check if program name filter criteria is passed in criteria 
		if($classNameSearch) {
			$classNameLike = "%" . $classNameCriteria . "%";
			$sql = $sql . $keyword . " name like :classNameCriteria ";
			$keyword = "AND";
		}
		
		// check if minimum duration is passed in criteria 
		if($minDurationSearch) {
			$sql = $sql . $keyword . " duration >= :minDuration ";
			$keyword = "AND";
		}
		
		// check if maximum duration is passed in criteria 
		if($maxDurationSearch) { 
			$sql = $sql . $keyword . " duration <= :maxDuration ";
			$keyword = "AND";
		}

		// check if trainerID is part of the passed in criteria 
		if($trainerSearch) { 
			$sql = $sql . $keyword . " trainerID = :trainerID ";
			$keyword = "AND";
		}

		//echo $sql;
	
		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare($sql);
			//echo $sql;

			if ($classNameSearch) $stmt->bindParam(':classNameCriteria', $classNameLike, PDO::PARAM_STR);
			if ($minDurationSearch) $stmt->bindParam(':minDuration', $minDuration, PDO::PARAM_INT);
			if ($maxDurationSearch) $stmt->bindParam(':maxDuration', $maxDuration, PDO::PARAM_INT);
			if ($trainerSearch) $stmt->bindParam(':trainerID', $trainerID, PDO::PARAM_INT);
			
			$stmt->execute();
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception retrieving classes: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				$data[] = $row;
			}
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Classes retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No classes found","data" => Null];
			echo json_encode($arr);
		}
	}
	
	
	function getAllClasses($conn) {
		// GET ALL CLASSES FROM THE CLASSES TABLE

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM classes");
			$stmt->execute();
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception retrieving classes: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				$data[] = $row;
			}
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Classes retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No classes found","data" => Null];
			echo json_encode($arr);
		}
	}


	function getClassByID($conn,$classID) {
		// GET A SPECIFIC CLASS FROM THE CLASSES TABLE

		if($classID == Null) {
			$arr = ["status" => "Error","msg" => "Class not retrieved - Invalid ClassID: " . $progID];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM classes WHERE classID = :classID");
			$stmt->bindParam(':classID', $classID, PDO::PARAM_INT);
			
			$stmt->execute();
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception retrieving class: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				$data[] = $row;
			}
			
			//Display the result as json
			$arr = ["status" => "OK","msg" => "Class retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is Error as the ClassID was not found
			$arr = ["status" => "Error","msg" => "No class found - ClassID: " . $classID];
			echo json_encode($arr);
		}
	}
	
		function getClassBookingsByClientID($conn,$clientID) {
		// GET ALL CLASS BOOKINGS FOR A PARTICULAR CLIENT FROM THE CLASS_BOOKINGS TABLE

		if($clientID == Null) {
			$arr = ["status" => "Error","msg" => "Class bookings not retrieved - Invalid blank clientID"];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM class_bookings cb INNER JOIN class_timeslots ts ON cb.timeslotID = ts.timeslotID INNER JOIN classes c ON ts.classID = c.classID WHERE cb.clientID = :clientID");
			$stmt->bindParam(':clientID', $clientID, PDO::PARAM_INT);

			$stmt->execute();
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception retrieving class bookings: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				$data[] = $row;
			}
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Class Bookings retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No class bookings found","data" => Null];
			echo json_encode($arr);
		}
	}

	function insertClassBooking($conn,$timeslotID,$clientID) {
		// INSERT A NEW CLASS BOOKING TO THE CLASS_BOOKING TABLE

		if($timeslotID == Null) {
			$arr = ["status" => "Error","msg" => "Class Booking not added - Invalid blank timeslotID"];
			die(json_encode($arr));
		}

		if($clientID == Null) {
			$arr = ["status" => "Error","msg" => "Class Booking not added - Invalid blank clientID"];
			die(json_encode($arr));
		}
		
		try {
			//First check max occupancy hasn't been reached
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT COUNT(*) AS 'BookedCount',c.maxOccupancy FROM class_bookings cb INNER JOIN class_timeslots ts ON cb.timeslotID = ts.timeslotID INNER JOIN classes c ON ts.classID = c.classID WHERE cb.timeslotID = :timeslotID");
			$stmt->bindParam(':timeslotID', $timeslotID, PDO::PARAM_INT);
			
			$stmt->execute();

			$count = $stmt->rowCount();
			
			if ($count < 1) {
				$arr = ["status" => "Error","msg" => "Class Booking not added successfully - Unable to retrieve booking count"];
				die(json_encode($arr));
			}

			$result = $stmt->fetch(PDO::FETCH_ASSOC);

			if ($result['maxOccupancy'] == Null) {
				$arr = ["status" => "Error","msg" => "Class Booking not added successfully - Unable to determine maxOccupancy"];
				die(json_encode($arr));
			}
			

			if ($result['BookedCount'] >= $result['maxOccupancy']) {
				$arr = ["status" => "Error","msg" => "Class Booking not added successfully - Clas is now full"];
				die(json_encode($arr));
			}
			
			// prepare sql and bind parameters for the insert
			$stmt = $conn->prepare("INSERT INTO class_bookings (timeslotID,clientID) VALUES (:timeslotID , :clientID)");
			$stmt->bindParam(':timeslotID', $timeslotID, PDO::PARAM_INT);
			$stmt->bindParam(':clientID', $clientID, PDO::PARAM_INT);
			
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				// Return the classBookingID just created
				$classBookingID = $conn->lastInsertId();
				$arr = ["status" => "OK","msg" => "Class Booking added successfully: " . $classBookingID,"classBookingID" => $classBookingIDprogID];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Class Booking not added successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception adding class booking: " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
	
	function deleteClassBooking($conn,$classBookingID) {
		// DELETE A SPECIFIC CLASS BOOKING FROM THE CLASS_BOOKINGS TABLE

		if($classBookingID == Null) {
			$arr = ["status" => "Error","msg" => "Class Booking not deleted - Invalid blank classBookingID"];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			
			$stmt = $conn->prepare("DELETE FROM class_bookings WHERE classBookingID = :classBookingID");
			$stmt->bindParam(':classBookingID', $classBookingID, PDO::PARAM_INT);
			
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				$arr = ["status" => "OK","msg" => "Class Booking deleted successfully"];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Class Booking not deleted successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception deleting class booking: " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
	
	function getAvailableClassTimeslots($conn,$classID) {
		// GET TIMESLOTS FOR A CLASS. EXCLUDE THOSE THAT ALREADY HAVE MAX OCCUPANCY

		if($classID == Null) {
			$arr = ["status" => "Error","msg" => "Class timeslots not retrieved - Invalid blank ClassID"];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			$stmt = $conn->prepare("SELECT * FROM class_timeslots ts INNER JOIN classes c ON ts.classID = c.classID INNER JOIN (SELECT ts.timeslotID, COUNT(*) AS 'BookedCount' FROM class_timeslots ts INNER JOIN class_bookings cb ON ts.timeslotID = cb.timeslotID GROUP BY ts.timeslotID) bc ON ts.timeslotID = bc.timeslotID WHERE ts.classID= :classID");
			$stmt->bindParam(':classID', $classID, PDO::PARAM_INT);
			$stmt->execute();
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception retrieving class timeslots: " . $e->getMessage()];
			die(json_encode($arr));
		}
	
		$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
		if ($result) {
			$data = array();

			foreach ($result as $row) {
				// ignore class timeslot if already has max occupancy
				if ($row['BookedCount'] < $row['maxOccupancy']) {
					$data[] = $row;
				}
			}
			
			// Output the results as json data
			$arr = ["status" => "OK","msg" => "Class timeslots retrieved successfully","data" => $data];
			echo json_encode($arr);
		} else {
			// Return code is OKND (Query ran ok but no data to return which is not necessarily an error)
			$arr = ["status" => "OKND","msg" => "No class timeslots found","data" => Null];
			echo json_encode($arr);
		}
	}

	
	function insertClassTimeslot($conn,$classID,$trainerID,$timestamp) {
		// INSERT A NEW CLASS TIMESLOT TO THE CLASS_TIMESLOTS TABLE

		if($classID == Null) {
			$arr = ["status" => "Error","msg" => "Class Timeslot not added - Invalid blank classID"];
			die(json_encode($arr));
		}

		if($trainerID == Null) {
			$arr = ["status" => "Error","msg" => "Class Timeslot not added - Invalid blank trainerID"];
			die(json_encode($arr));
		}

		if($timestamp == Null) {
			$arr = ["status" => "Error","msg" => "Class Timeslot not added - Invalid blank timestamp"];
			die(json_encode($arr));
		}
		
		try {
		
			// prepare sql and bind parameters for the insert
			$stmt = $conn->prepare("INSERT INTO class_timeslots (classID,trainerID,timestamp) VALUES (:classID , :trainerID, :timestamp)");
			$stmt->bindParam(':classID', $classID, PDO::PARAM_INT);
			$stmt->bindParam(':trainerID', $trainerID, PDO::PARAM_INT);
			$stmt->bindParam(':timestamp', $timestamp, PDO::PARAM_STR);
			
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				// Return the classTimeslotID just created
				$classTimeslotID = $conn->lastInsertId();
				$arr = ["status" => "OK","msg" => "Class Timeslot added successfully: " . $classTimeslotID,"classTimeslotID" => $classTimeslotID];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Class Timeslot not added successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception adding class timeslot: " . $e->getMessage()];
			die(json_encode($arr));
		}
	}
	
	function deleteClassTimeslot($conn,$classTimeslotID) {
		// DELETE A SPECIFIC CLASS TIMESLOT FROM THE CLASS_TIMESLOTS TABLE
		// THIS WILL ALSO REMOVE ANY CORRESPONDING ROWS IN THE CLASS_BOOKINGS TABLE DUE TO ON DELETE CASCADE TABLE SETTING

		if($classTimeslotID == Null) {
			$arr = ["status" => "Error","msg" => "Class Timeslot not deleted - Invalid blank classTimeslotID"];
			die(json_encode($arr));
		}

		try {
			// prepare sql and bind parameters
			
			$stmt = $conn->prepare("DELETE FROM class_timeslots WHERE classTimeslotID = :classTimeslotID");
			$stmt->bindParam(':classTimeslotID', $classTimeslotID, PDO::PARAM_INT);
			
			$stmt->execute();
			$count = $stmt->rowCount();
			
			if ($count > 0) {
				$arr = ["status" => "OK","msg" => "Class Timeslot deleted successfully"];
				echo json_encode($arr);
			} else {
				$arr = ["status" => "Error","msg" => "Class Timeslot not deleted successfully"];
				echo json_encode($arr);
			}
		} catch(PDOException $e) {
			$arr = ["status" => "Error","msg" => "PDO exception deleting class timeslot: " . $e->getMessage()];
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


?>
