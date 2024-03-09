<?php
//Check if sessions are already started
if (session_status() == PHP_SESSION_NONE) {
    //Start session
    session_start();
}

include_once 'sessionData.php';

destroySession();
?>