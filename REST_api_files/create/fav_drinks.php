<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";

$dName = $_POST["name"];
$dDescription = $_POST["description"];
$user = $_POST["user"];

$dbconnect=mysqli_connect($host,$dbuser,$pass,$db);

if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}
$query = "  INSERT INTO `Drinks_table` (`id_drinks`, `name_drink`, `description_drink`, `fk_id_User`)
  VALUES (NULL, '$dName', '$dDescription', '$user')";

if (!mysqli_query($dbconnect, $query)) {
  die('An error occurred=  '. mysqli_error($dbconnect));
} else {
  echo "Success.";
}

mysqli_close($dbconnect);
?>
