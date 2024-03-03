<?php
//Check if sessions are already started
if (session_status() == PHP_SESSION_NONE) {
    //Start session
    session_start();
}

//Set the content type to text/plain
header('Content-Type: text/plain');

// Include the sessionData.php file
include_once 'sessionData.php';
include_once 'DBConnection.php';

//Create connection and set it as '$conn'.
$conn = connectToDB();

//Check for any existing session data
if (isset($_GET['checkSession'])) {
    $checkSession = $_GET['checkSession'];
    //Handle session check request
    if ($checkSession === "True") {
        //Check if a session exists
        if (isset($_SESSION['userId'])) {
            echo "Session active\r\n\n";
            //Send session data over as JSON
            getSession();
        } else {
            echo "No active session\r\n\n";
        }
        exit();
    }

//If not checking session - instead logging in normally
} else {
    //Check if the 'accountType' parameter is set in the GET request
    if (isset($_GET['accountType'])) {
        $accountType = $_GET['accountType'];
        //If accountType is trainer login
        if ($accountType === "trainerlogin") {
            //Get username and password from app
            $user = $_GET['user'];
            $pass = $_GET['pass'];
            
            //Create SQL query using values
            $sql = "SELECT * FROM trainerLoginTest WHERE username = '$user' AND password = '$pass'";
            $result = $conn->query($sql);

            //If login exists
            if ($result->num_rows > 0) {
                //Retrieves ID value from the SQL result
                $row = $result->fetch_assoc();
                $userID = $row['trainerID'];
                $username = $row['username'];

                //Saves session data with user ID and username
                setSessionData($userID, $user, "trainer");

                echo "Login successful\r\n\n";
                getSession();

            //If login doesn't exist
            } else {
                echo "Login failed";
            }
        
        //If accountType is trainer login
        } elseif ($accountType === "clientlogin") {
            //Get username and password from app
            $user = $_GET['user'];
            $pass = $_GET['pass'];
            
            //Create SQL query using values
            $sql = "SELECT * FROM clientLoginTest WHERE username = '$user' AND password = '$pass'";
            $result = $conn->query($sql);
            
            //If login exists
            if ($result->num_rows > 0) {
                //Retrieves ID value from the SQL result
                $row = $result->fetch_assoc();
                $userID = $row['clientID'];
                $username = $row['username'];

                //Saves session data with user ID and username
                setSessionData($userID, $user, "client");

                echo "Login successful\r\n\n";
                getSession();

            //If login doesn't exist
            } else {
                echo "Login failed";
            }
        }
    } else {
        echo "No account type provided";
    }
}
?>
