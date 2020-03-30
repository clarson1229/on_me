<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";
$username = $_POST["user"];

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if($conn){
        $q="SELECT * from Users_table Where id_User like '$username'";
        $result= mysqli_query($conn,$q);
        if(mysqli_num_rows($result) >0){
                $row = mysqli_fetch_assoc($result);
                $response->userName = $row["id_User"];
                $response->firstName = $row["first_name_user"];
                $response->lastName = $row["last_name_user"];
                $response->address = $row["address_user"];
                $response->email = $row["email_user"];
                $myObj->userInfo= $response;
                $myJSON = json_encode($myObj);
                echo $myJSON;
        }else{
                echo json_encode("userInfo not found");
        }
}else{
        echo "not connected";
}
mysqli_close($conn);
?>