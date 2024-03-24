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

//Immediately check for any existing session data
if (isset($_SESSION['userId'])) {
    //Login if session exists
    echo "Login successful\r\n\n";
    //Send session data over as JSON
    getSession();
} else {
    echo "Session unavailable";
}

//Check if the 'accountType' parameter is set in the GET request
if (isset($_GET['accountType'])) {
    //If trying to log in, create connection and set it as '$conn'
    $conn = connectToDB();

    $accountType = $_GET['accountType'];

    //If accountType is trainer login
    if ($accountType === "trainerlogin") {
        //Get username and password from app
        $user = $_GET['user'];
        $pass = $_GET['pass'];
        
        //Create SQL query using values
        $sql = "SELECT * FROM trainers WHERE username = '$user' AND password = '$pass'";
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
        $sql = "SELECT * FROM clients WHERE username = '$user' AND password = '$pass'";
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
    
    mysqli_close($conn);

} else {
    echo "No account type provided";
}
?>
