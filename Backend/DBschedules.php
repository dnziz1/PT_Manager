<?php
// Database connection details
$host = "localhost";
$dbname = "naazshop_appDbfinal";
$username = "naazshop_appDbfinal";
$password = "420aF375$";

try {
    // Create a PDO instance
    $pdo = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);

    // Set PDO to throw exceptions on error
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // API endpoint for getting all schedules or a specific schedule by sId
    if ($_SERVER['REQUEST_METHOD'] === 'GET') {
        // Check if the URL has an sId parameter
        $urlParts = explode('/', $_SERVER['REQUEST_URI']);
        $sId = $urlParts[count($urlParts) - 1];

        if (is_numeric($sId)) {
            // Fetch the schedule by sId
            $stmt = $pdo->prepare("SELECT * FROM schedules WHERE sId = :sId");
            $stmt->bindParam(':sId', $sId, PDO::PARAM_INT);
            $stmt->execute();
            $schedule = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($schedule) {
                echo json_encode($schedule);
            } else {
                echo json_encode(['error' => 'Schedule not found']);
            }
        } else {
            // Return all schedules if no valid sId provided
            $stmt = $pdo->query("SELECT * FROM schedules");
            $schedules = $stmt->fetchAll(PDO::FETCH_ASSOC);
            echo json_encode($schedules);
        }
    }
            // API endpoint for adding a new schedule (POST method)
            if ($_SERVER['REQUEST_METHOD'] === 'POST') {
              // Assuming data is sent as JSON in the request body
              $data = json_decode(file_get_contents("php://input"), true);

              $sId = $data['sId'];
              $trainerId = $data['trainerId'];
              $clientId = $data['clientId'];
              $eventDate = $data['eventDate'];
              $startTime = $data['startTime'];
              $endTime = $data['endTime'];
              $title = $data['title'];
              $details = $data['details'];

              $stmt = $pdo->prepare("INSERT INTO schedules (sId, trainerId, clientId, eventDate, startTime, endTime, title, details) VALUES (:sId, :trainerId, :clientId, :eventDate, :startTime, :endTime, :title, :details)");
              $stmt->bindParam(':sId', $sId, PDO::PARAM_INT);
              $stmt->bindParam(':trainerId', $trainerId, PDO::PARAM_INT);
              $stmt->bindParam(':clientId', $clientId, PDO::PARAM_INT);
              $stmt->bindParam(':eventDate', $eventDate, PDO::PARAM_STR);
              $stmt->bindParam(':startTime', $startTime, PDO::PARAM_STR);
              $stmt->bindParam(':endTime', $endTime, PDO::PARAM_STR);
              $stmt->bindParam(':title', $title, PDO::PARAM_STR);
              $stmt->bindParam(':details', $details, PDO::PARAM_STR);

              if ($stmt->execute()) {
                  // Fetch the saved schedule and return it along with a success message
                  $savedStmt = $pdo->prepare("SELECT * FROM schedules WHERE sId = :sId");
                  $savedStmt->bindParam(':sId', $sId, PDO::PARAM_INT);
                  $savedStmt->execute();
                  $savedSchedule = $savedStmt->fetch(PDO::FETCH_ASSOC);

                  echo json_encode(['message' => 'Schedule added successfully', 'data' => $savedSchedule]);
              } else {
                  echo json_encode(['error' => 'Failed to add schedule']);
              }
          }
        

    


    // API endpoint for updating an existing schedule (PUT method)
    if ($_SERVER['REQUEST_METHOD'] === 'PUT') {
        // Assuming data is sent as JSON in the request body
        $data = json_decode(file_get_contents("php://input"), true);

        $sId = $data['sId'];
        $trainerId = $data['trainerId'];
        $clientId = $data['clientId'];
        $eventDate = $data['eventDate'];
        $startTime = $data['startTime'];
        $endTime = $data['endTime'];
        $title = $data['title'];
        $details = $data['details'];

        // Check if the schedule with the given sId and trainerId exists
        $checkStmt = $pdo->prepare("SELECT * FROM schedules WHERE sId = :sId AND trainerId = :trainerId");
        $checkStmt->bindParam(':sId', $sId, PDO::PARAM_INT);
        $checkStmt->bindParam(':trainerId', $trainerId, PDO::PARAM_INT);
        $checkStmt->execute();
        $existingSchedule = $checkStmt->fetch(PDO::FETCH_ASSOC);

        if ($existingSchedule) {
            // Update the existing schedule
            $updateStmt = $pdo->prepare("UPDATE schedules SET clientId = :clientId, eventDate = :eventDate, startTime = :startTime, endTime = :endTime, title = :title, details = :details WHERE sId = :sId AND trainerId = :trainerId");
            $updateStmt->bindParam(':clientId', $clientId, PDO::PARAM_INT);
            $updateStmt->bindParam(':eventDate', $eventDate, PDO::PARAM_STR);
            $updateStmt->bindParam(':startTime', $startTime, PDO::PARAM_STR);
            $updateStmt->bindParam(':endTime', $endTime, PDO::PARAM_STR);
            $updateStmt->bindParam(':title', $title, PDO::PARAM_STR);
            $updateStmt->bindParam(':details', $details, PDO::PARAM_STR);
            $updateStmt->bindParam(':sId', $sId, PDO::PARAM_INT);
            $updateStmt->bindParam(':trainerId', $trainerId, PDO::PARAM_INT);

            if ($updateStmt->execute()) {
                // Fetch the updated schedule and return it along with a success message
                $updatedStmt = $pdo->prepare("SELECT * FROM schedules WHERE sId = :sId AND trainerId = :trainerId");
                $updatedStmt->bindParam(':sId', $sId, PDO::PARAM_INT);
                $updatedStmt->bindParam(':trainerId', $trainerId, PDO::PARAM_INT);
                $updatedStmt->execute();
                $updatedSchedule = $updatedStmt->fetch(PDO::FETCH_ASSOC);

                echo json_encode(['message' => 'Schedule updated successfully', 'data' => $updatedSchedule]);
            } else {
                echo json_encode(['error' => 'Failed to update schedule']);
            }
        } else {
            echo json_encode(['error' => 'Schedule not found for update']);
        }
    }
    } catch (PDOException $e) {
    // Handle database connection error
    echo "Connection failed: " . $e->getMessage();
  }
?>
