<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";

$friend1 = $_GET["user1"];
$friend2 = $_GET["user2"];

$dbconnect=mysqli_connect($host,$dbuser,$pass,$db);

if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}

$query = " INSERT INTO `Friends_table` (`fk_id_user`, `fk_id_user_friend`)
  VALUES ('$friend1', '$friend2')";

if (!mysqli_query($dbconnect, $query)) {
  die('An error occurred in 1=  '. mysqli_error($dbconnect));
} else {
  echo "Success 1 of 2. \n";
}
$query2 = " INSERT INTO `Friends_table` (`fk_id_user`, `fk_id_user_friend`)
  VALUES ('$friend2', '$friend1')";

if (!mysqli_query($dbconnect, $query2)) {
  die('\n '.'An error occurred in 2=  '. mysqli_error($dbconnect));
} else {
  echo "Success 2 of 2. \n";
}

mysqli_close($dbconnect);
?>
