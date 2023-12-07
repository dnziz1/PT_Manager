<?php
//server.php

//Set the content type to text/plain
header('Content-Type: text/plain');

$servername = "localhost";
$username = "root";
$password = "test";
$dbname = "appDB";

//Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

//Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
echo "Connected successfully\r\n\n";


//Check if the 'action' parameter is set in the GET request

if (isset($_GET['action'])) {
    $action = $_GET['action'];
    //If action is trainer login
    if ($action === "trainerlogin") {
        //Get username and password from app
        $user = $_GET['user'];
        $pass = $_GET['pass'];
        
        //Create SQL query using values
        $sql = "SELECT * FROM trainerLoginTest WHERE username = '$user' AND password = '$pass'";
        $result = $conn->query($sql);
        
        //If login exists
        if ($result->num_rows > 0) {
            echo "Login successful";
        //If login doesn't exist
        } else {
            echo "Login failed";
        }
    } elseif ($action === "clientlogin") {
        //Get username and password from app
        $user = $_GET['user'];
        $pass = $_GET['pass'];
        
        //Create SQL query using values
        $sql = "SELECT * FROM clientLoginTest WHERE username = '$user' AND password = '$pass'";
        $result = $conn->query($sql);
        
        //If login exists
        if ($result->num_rows > 0) {
            echo "Login successful";
        //If login doesn't exist
        } else {
            echo "Login failed";
        }
    }
} else {
    echo "No action provided";
}
?>
