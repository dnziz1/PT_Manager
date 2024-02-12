<?php

use PHPUnit\Framework\TestCase;

//require_once 'path/to/vendor/autoload.php';

include_once '../sessionData.php';
include '../login.php';

class loginTest extends TestCase
{
    public function testTrainerLoginSuccess()
    {
        // Mocking the database connection
        $connMock = $this->createMock(mysqli::class);
        $connMock->method('query')->willReturn($this->createMock(mysqli_result::class));

        // Simulate a trainer login request
        $_SERVER['REQUEST_METHOD'] = 'GET';
        $_GET['accountType'] = 'trainerlogin';
        $_GET['user'] = 'SessionTest';
        $_GET['pass'] = 'pass';

        // Set the mocked connection in your server file
        $conn = $connMock;

        // Start capturing output
        ob_start();

        // Include your server file (login.php)
        include '../login.php';

        // Get the captured output
        $output = ob_get_clean();

        // Debugging: Print the captured output
        echo "Captured Output:\n$output\n";

        // Expect the output to contain specific strings
        $this->expectOutputString('Connected successfully');
        $this->expectOutputString('Login successful');

        // Include your server file (login.php)
        include '../login.php';
    }
}