<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";
$friend1 = $_POST["friend1"];
$friend2 = $_POST["friend2"];

$dbconnect=mysqli_connect($host,$dbuser,$pass,$db);
if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}

$query="DELETE FROM `Friends_table` WHERE `fk_id_user` LIKE '$friend1' AND `fk_id_user_friend` LIKE '$friend2'";
if (!mysqli_query($dbconnect, $query)) {
  die('An error occurred in 1=  '. mysqli_error($dbconnect));
} else {
  echo "Success 1 of 2.";
}

$query2="DELETE FROM `Friends_table` WHERE `fk_id_user` LIKE '$friend2' AND `fk_id_user_friend` LIKE '$friend1'";
if (!mysqli_query($dbconnect, $query2)) {
  die('\n '.'An error occurred in 2=  '. mysqli_error($dbconnect));
} else {
  echo "Success 2 of 2.";
}

mysqli_close($dbconnect);
?>
