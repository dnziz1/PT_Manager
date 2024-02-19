<?php

use PHPUnit\Framework\TestCase;

require_once 'vendor/autoload.php';
require_once 'bootstrap.php';

include_once '../sessionData.php';
include '../login.php';

class loginTest extends TestCase
{
    public function testTrainerLoginSuccess()
    {
        ob_start();

        $servername = "localhost";
        $username = "root";
        $password = "test";
        $dbname = "appDB";
        
        //$connMock = $this->createMock(mysqli::class);
        //$connMock->method('query')->willReturn($this->createMock(mysqli_result::class));

        $conn = new mysqli($servername, $username, $password, $dbname);

        //Simulate a trainer login request
        $_SERVER['REQUEST_METHOD'] = 'GET';
        $_GET['accountType'] = 'trainerlogin';
        $_GET['user'] = 'SessionTest';
        $_GET['pass'] = 'pass';

        ob_start();

        $output = ob_get_clean();


        //Expect the output to contain specific strings
        $this->expectOutputString('Connected successfully');
        //$this->expectOutputString('Login successful');
        //$this->assertStringContainsString('Connected successfully', $output);
        //$this->assertStringContainsString('Login successful', $output);
        
    }
}