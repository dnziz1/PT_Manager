<?php

use PHPUnit\Framework\TestCase;

require_once 'vendor/autoload.php';
require_once 'bootstrap.php';

class loginTest extends TestCase
{
    public function testDBConnection()
    {
        include_once '../DBConnection.php';
        
        // Capture the output of the connectToDB function
        ob_start();
        $conn = connectToDB();
        $output = ob_get_clean();

        $conn->close();

        // Assert that the output contains the expected message
        $this->assertStringContainsString('Connected successfully', $output);
    }

    public function testTrainerLoginSuccess()
    {
        include_once '../login.php';

        $servername = "localhost";
        $username = "root";
        $password = "test";
        $dbname = "appDB";
    
        //Create connection
        $conn = new mysqli($servername, $username, $password, $dbname);

        $testUser = "SessionTest";
        $testPass = "pass";

        // Capture the output of the login.php script
        ob_start();
        
        // Simulate a trainer login request using the test user's credentials
        $_SERVER['REQUEST_METHOD'] = 'GET';
        $_GET['accountType'] = 'trainerlogin';
        $_GET['user'] = $testUser;
        $_GET['pass'] = $testPass;

        $output = ob_get_clean();

        $conn->close();

        $this->assertStringContainsString('Login successful', $output);
    }
}