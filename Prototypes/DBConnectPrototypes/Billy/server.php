<?php
//Set the content type to text/plain
header('Content-Type: text/plain');

//Database server settings
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
echo "Connected Successfully!\r\n\n";


//Check if the 'query' parameter is set in the GET request
if (isset($_GET['query'])) {
    //Get query and send it to the DB, getting the result
    $sql = $_GET['query'];
    $result = $conn->query($sql);

    if ($result) {
        //For any SELECT query, send each value into an array
        if ($result->num_rows > 0) {
            $data = array();
            while($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
            //Display the result as json
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
