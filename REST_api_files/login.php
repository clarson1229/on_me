<?php

$db = "on_me";
$dbuser = "root";
$pass = "gunni";
$host = "localhost";
$username = $_POST["user"];
$password = $_POST["password"];

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if ($conn){
        $q = "SELECT * from Users_table Where id_User like '$username' and password_user like '$passw$
        $result = mysqli_query($conn, $q);
        if (mysqli_num_rows($result) > 0){
                echo "Login Sucess \n";
                $row = mysqli_fetch_assoc($result);
                echo $row["id_User"];
         }else{
                echo "UserName not found";
                echo "Login failed!!!";
        }
}else {
        echo "not connected";
}
mysqli_close($conn);
?>
