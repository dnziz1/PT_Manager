<?php
//Content type
header('Content-Type: text/plain');

// Connect to database
$servername = "localhost";
$username = "Daniel";
$password = "notifs12";
$dbname = "schedules";

// Create conncection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
echo "Connected successfully\r\n\n";

// Query to retrieve data
$sql = "SELECT title, details FROM schedules WHERE is_new = 1";
$result = $conn->query($sql);

// Fetch data and convert it to JSON
$data = array();
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
} else {
    echo"No new notifications.";
}

//Reset is_new to 0 for the retrieved data
$updatedSql = "UPDATE schedules SET is_new = 0 WHERE is_new = 1";
$conn->query($updatedSql);

// Close the connection
$conn->close();

echo json_encode($data);
?>
