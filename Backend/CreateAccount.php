<?php
//Check if sessions are already started
if (session_status() == PHP_SESSION_NONE) {
    //Start session
    session_start();
}

//Set the content type to text/plain
header('Content-Type: text/plain');

include_once 'sessionData.php';
include_once 'DBConnection.php';

$conn = connectToDB();

$userID = NULL;
$user = NULL;
$fname = NULL;
$lname = NULL;
$phone = NULL;
$email = NULL;

//Required inputs
if (isset($_GET['user']) && isset($_GET['pass']) && isset($_GET['fname']) && isset($_GET['lname'])) {
    $user = $_GET['user'];
    $pass = $_GET['pass'];
    $fname = $_GET['fname'];
    $lname = $_GET['lname'];

    //Optional inputs
    if (isset($_GET['phonenumber'])) {
        $phone = $_GET['phonenumber'];
    }
    if (isset($_GET['email'])) {
        $email = $_GET['email'];
    }
    
    //Searches database for username
    $sql = "SELECT * FROM clients WHERE username = '$user'";
    $result = $conn->query($sql);
    //If username does not already exist
    if ($result->num_rows === 0) {
        //Create SQL query using values and execute
        $sql = "SELECT * FROM clients";
        $result = $conn->query($sql);
        //Gets userID as number of existing users + 1
        $userID = mysqli_num_rows($result)+1;

        //Insert new account
        $sql = "INSERT INTO clients (cId, username, password, firstName, lastName, phoneNumber, email) VALUES ('$userID', '$user', '$pass', '$fname', '$lname', " . ($phone ? "'$phone'" : "NULL") . ", " . ($email ? "'$email'" : "NULL") . ")";

        if (mysqli_query($conn,$sql)) {
            echo "New account created successfully";
        } else {
            echo "Error: " . $sql . "\r\n\n" . mysqli_error($conn);
        }

        //Saves session data with user ID and username
        setSessionData($userID, $user, "client");
    }
    else {
        echo("Username already in use");
    }

} else {
    echo("Required inputs are not fulfilled");
}

mysqli_close($conn);
?>