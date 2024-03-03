<?php
session_start();

function setSessionData($userId, $username, $accountType) {
    $_SESSION['userId'] = $userId;
    $_SESSION['username'] = $username;
    $_SESSION['accountType'] = $accountType;
}

function checkSession() {
    //Redirect to login if session data is not present
    if (!isset($_SESSION['userId'])) {
        header("Location: login.php");
        exit();
    }
}

function getSession() {
    if (isset($_SESSION['userId'])) {
        //Store each value into an array
        $sessionData = array(
            'userId' => $_SESSION['userId'],
            'username' => $_SESSION['username'],
            'accountType' => $_SESSION['accountType']
        );

        //Convert array to json then echo
        $jsonSessionData = json_encode($sessionData);
        echo $jsonSessionData;
    } else {
        //Redirect to login if session data is not present
        header("Location: login.php");
        exit();
    }
}

function destroySession() {
    //Unset all session variables
    session_unset();

    //Destroy the session
    session_destroy();

    //Redirect to the login page or homepage
    header("Location: login.php");
    exit();
}
?>