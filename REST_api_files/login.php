<?php

$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";
$username = $_POST["user"];
$password = $_POST["password"];

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if ($conn){
        $q = "SELECT * from Users_table Where id_User like '$username' and pass$
        $result = mysqli_query($conn, $q);
        if (mysqli_num_rows($result) > 0){
                $row = mysqli_fetch_assoc($result);
                $response->status = true;
                $response->userName = $row["id_User"];
                $myObj->response= $response;
                $myJSON = json_encode($myObj);
                echo $myJSON;
         }else{
                $response->status = false;
                $response->userName = "NULL";
                $myObj->response=$response;
                $myJSON = json_encode($myObj);
                echo $myJSON;
        }
}else {
        echo "not connected";
}
mysqli_close($conn);
?>