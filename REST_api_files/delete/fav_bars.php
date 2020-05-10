<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";
$barId = $_POST["barId"];


$q="DELETE FROM `Fav_Bars_table` WHERE `id_fav_bar` LIKE '$barId'";
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
