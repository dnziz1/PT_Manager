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

//Required inputs
if (isset($_GET['username']) && isset($_GET['password']) && isset($_GET['firstname']) && isset($_GET['lastname']) && isset($_GET['dob'])) {
    $user = $_GET['username'];
    $pass = $_GET['password'];
    $fname = $_GET['firstname'];
    $lname = $_GET['lastname'];
    $dob = $_GET['dob'];

    //Optional inputs
    //if (isset($_GET['phonenumber'])) {
    //    $accountType = $_GET['phonenumber'];
    //}
    //if (isset($_GET['address'])) {
    //    $accountType = $_GET['address'];
    //}
    
    //Searches database for username
    $sql = "SELECT * FROM clientLoginTest WHERE username = '$user'";
    $result = $conn->query($sql);
    //If username does not already exist
    if ($result->num_rows === 0) {
        //Create SQL query using values and execute
        $sql = "SELECT * FROM clientLoginTest";
        $result = $conn->query($sql);
        //Gets userID as number of existing users + 1
        $userID = mysqli_num_rows($result)+1;

        //Insert new account
        $sql = "INSERT INTO clientLoginTest (clientID, username, password) VALUES ('$userID', '$user', '$pass')";

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