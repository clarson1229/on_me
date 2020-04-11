<?php
$db = "on_me";
$dbuser = "root";
$pass = "";
$host = "";
$username = $_POST["user"];

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if($conn){
        $q="SELECT * FROM Friends_table WHERE fk_id_user LIKE '$username'";
        $result= mysqli_query($conn,$q);
        if(mysqli_num_rows($result) >0){
                while($row = mysqli_fetch_assoc($result)){
                        $friend= $row["fk_id_user_friend"];
                        $results['Friends'][] = array('Friend' => $friend);
                }
                echo json_encode($results);
        }else{
                echo json_encode("No Friends found");
        }
}else{
        echo "not connected";
}
mysqli_close($conn);
?>