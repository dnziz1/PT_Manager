<?php

// database connection
include_once 'DBConnection.php';

$conn = connectDB();

// get_messages
function getMessages() {

    $result = $conn->query("SELECT * FROM messages");

    $messages = array();
    while ($row = $result->fetch_assoc()) {
        $messages[] = $row;
    }

    echo json_encode($messages);

    $conn->close();
}

// add_message
function addMessage($sender, $message) {

    $stmt = $conn->prepare("INSERT INTO messages (sender, message, timestamp) VALUES (?, ?, NOW())");
    $stmt->bind_param("ss", $sender, $message);
    $stmt->execute();
    $stmt->close();

    $conn->close();
}

// Handle requests
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Handle GET requests, e.g., retrieve messages
    getMessages();
} elseif ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Handle POST requests, e.g., add a new message
    $sender = $_POST['sender'];
    $message = $_POST['message'];

    addMessage($sender, $message);
}
?>
