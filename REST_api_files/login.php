<?php

$db = "on_me";
$dbuser = "*****";
$pass = "*****";
$host = "localhost";
$username = $_GET["user"];
$password = $_GET["password"];

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if ($conn){
        $q = "SELECT * from Users_table Where id_User like '$username' and password_user like '$password'";
        $result = mysqli_query($conn, $q);
        if (mysqli_num_rows($result) > 0){
                $results = mysqli_fetch_array($result);
                echo json_encode($results);
         }else{
                echo  $_GET["user"];
                echo "Login failed!!!";
        }
}else {
        echo "not connected";
}
mysqli_close($conn);
?>