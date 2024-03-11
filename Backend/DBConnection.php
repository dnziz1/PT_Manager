<?php
//Static variable to track the connection state
static $conn;

function connectToDB() {
    //Change parameters to fit database settings
    $servername = "localhost";
    $username = "root";
    $password = "test";
    $dbname = "appDB";

    //Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);

    //Check if a connection already exists
    if ($conn instanceof mysqli && $conn->ping()) {
        echo "Connected successfully\r\n\n";
        return $conn;
    }

    //Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }
    echo "Connected successfully\r\n\n";

    return $conn;
}
?>