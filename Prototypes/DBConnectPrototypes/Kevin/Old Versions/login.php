<?php
$user = $_POST['user'];
$password = $_POST['password'];
$response = array();
//Check if all fieds are given
if (empty($user) || empty($password)) {
    $response['success'] = "0";
    $response['message'] = "Some fields are empty. Please try again!";
    echo json_encode($response);
    die;
}
$userdetails = array(
    'user' => $user,
    'password' => md5($password)
);
//Insert the user into the database
$success = loginUser($userdetails);
if (!empty($success)) {
    $response['success'] = "1";
    $response['message'] = "Login successfully!";
    $response['details'] = $success;
    echo json_encode($response);
} else {
    $response['success'] = "0";
    $response['message'] = "Login failed. Please try again!";
    echo json_encode($response);
}
function loginUser($userdetails) {
    require './db.php';
    $array = array();
    $stmt = $pdo->prepare("SELECT * FROM Co5590.users WHERE (email = :user OR username = :user ) AND password = :password");
    $stmt->execute($userdetails);
    $array = $stmt->fetch(PDO::FETCH_ASSOC);
    $stmt = null;
    return $array;
}