<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";
$drinkId = $_POST["drinkId"];

$q="DELETE FROM `Drinks_table` WHERE `id_drinks` LIKE '$drinkId'";
$dbconnect=mysqli_connect($host,$dbuser,$pass,$db);

if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}

if (!mysqli_query($dbconnect, $q)) {
  die('An error occurred=  '. mysqli_error($dbconnect));
} else {
  echo "Success.";
}

mysqli_close($dbconnect);
?>
