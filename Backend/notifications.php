<?php
header('Content-Type: application/json');

// Include database connection
include "db.php";

// Function to get all notifications
function getAllNotifications($conn) {
    try {
        // Prepare SQL statement to select all notifications
        $stmt = $conn->prepare("SELECT * FROM schedules");
        // Execute SQL statement
        $stmt->execute();
    } catch(PDOException $e) {
        // If an exception occurs, return error response
        $response = ["status" => "Error", "msg" => "PDO exception retrieving notifications: " . $e->getMessage()];
        echo json_encode($response);
        return;
    }
    
    // Fetch all notifications from the result set
    $result = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    // Check if notifications were found
    if ($result) {
        // If notifications found, encode them as JSON and send response
        $response = ["status" => "OK", "msg" => "Notifications retrieved successfully", "data" => $result];
        echo json_encode($response);
    } else {
        // If no notifications found, send response indicating no data
        $response = ["status" => "OKND", "msg" => "No notifications found", "data" => null];
        echo json_encode($response);
    }
}


// Call the function to get all notifications
getAllNotifications($conn);

// Close the database connection
$conn = null;
?>
