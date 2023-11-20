<?php
// server.php

// Set the content type to text/plain
header('Content-Type: text/plain');

$servername = "localhost";
$username = "root";
$password = "test";
$dbname = "appDB";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
echo "Connected successfully\r\n\n";


// Check if the 'query' parameter is set in the GET request
if (isset($_GET['query'])) {
    $sql = $_GET['query'];
    $result = $conn->query($sql);

    if ($result) {
        // Assuming a SELECT query, you can modify this based on your needs
        if ($result->num_rows > 0) {
            $data = array();
            while($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
            echo json_encode($data);
        } else {
            echo "No results";
        }
    } else {
        echo "Query failed: " . $conn->error;
    }
} else {
    echo "No query provided";
}
?>
