<?php
// Include database connection
include "db.php";

// Debugging statement to check if the date parameter is received
if(isset($_GET['date'])) {
    $selectedDate = $_GET['date'];
    echo "Date parameter received: " . $selectedDate; // Output the received date parameter for debugging
    $selectedDate = date('Y-m-d H:i:s', strtotime($selectedDate)); // Convert to expected datetime format
} else {
    // Handle missing date parameter error
    echo "Error: Date parameter is missing";
    exit;
}

// Prepare SQL query to fetch events for the selected date
$sql = "SELECT * FROM class_schedules WHERE startDate <= '$selectedDate' AND endDate >= '$selectedDate'";

// Execute SQL query
$result = $conn->query($sql);

// Array to store fetched events
$events = array();

// Check if there are any results
if ($result->num_rows > 0) {
    // Loop through each row of the result set
    while($row = $result->fetch_assoc()) {
        // Format event data
        $eventInfo = "Class ID: " . $row['classID'] . ", Start Date: " . $row['startDate'] . ", End Date: " . $row['endDate'] . ", Trainer ID: " . $row['trainerID'];
        
        // Add event info to events array
        $events[] = $eventInfo;
    }
}

// Close database connection
$conn->close();

// Encode events array into JSON format
$jsonResponse = json_encode($events);

// Set response content type to JSON
header('Content-Type: application/json');

// Output JSON response
echo $jsonResponse;
?>
