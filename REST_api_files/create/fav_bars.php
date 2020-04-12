<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";

$resName = $_POST["name"];
$address = $_POST["address"];
$user = $_POST["user"];
$resId = $_POST["resId"];

$dbconnect=mysqli_connect($host,$dbuser,$pass,$db);

if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}
$query = "INSERT INTO `Fav_Bars_table` (`id_fav_bar`, `fk_name_restaurant`, `fk_address_restaurant`, `fk_id_User`, `fk_id_restaurant`)
VALUES (NULL, '$resName', '$address', '$user', '$resId')";


if (!mysqli_query($dbconnect, $query)) {
  die('An error occurred=  '. mysqli_error($dbconnect));
} else {
  echo "Success.";
}

mysqli_close($dbconnect);
?>
