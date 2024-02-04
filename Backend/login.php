<?php
// Include the sessionData.php file
include_once 'sessionData.php';

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

//Start session
session_start();

//Check for any existing session data
if (isset($_GET['checkSession'])) {
    $checkSession = $_GET['checkSession'];
    //Handle session check request
    if ($checkSession === "True") {
        //Check if a session exists
        if (isset($_SESSION['userId'])) {
            echo "Session active";
        } else {
            echo "No active session";
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
                $userID = $row['tId'];
                $username = $row['username'];

                //Saves session data with user ID and username
                setSessionData($userID, $user);

                echo "Login successful";

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
                $userID = $row['cId'];
                $username = $row['username'];

                //Saves session data with user ID and username
                setSessionData($userID, $user);

                echo "Login successful";

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
