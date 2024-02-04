<?php
session_start();

function setSessionData($userId, $username) {
    $_SESSION['userId'] = $userId;
    $_SESSION['username'] = $username;
    // Add other user-related information as needed
}

function checkSession() {
    // Redirect to login if session data is not present
    if (!isset($_SESSION['userId'])) {
        header("Location: login.php");
        exit();
    }
}

function destroySession() {
    // Unset all session variables
    session_unset();

    // Destroy the session
    session_destroy();

    // Redirect to the login page or homepage
    header("Location: login.php");
    exit();
}
?>