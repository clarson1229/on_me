<?php
$db = "on_me";
$dbuser = "root";
$pass = "gunni";
$host = "localhost";

$userId = $_POST["user"];
$firstName = $_POST["firstName"];
$lastName = $_POST["lastName"];
$email = $_POST["email"];
$address = $_POST["address"];
$password = $_POST["password"];

$dbconnect=mysqli_connect($host,$dbuser,$pass,$db);

if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}

$query = "INSERT INTO `Users_table`(`id_User`, `first_name_user`, `last_name_user`, `address_user`, `email_user`, `password_user`)
VALUES ('$userId','$firstName','$lastName','$email','$address','$password')";

if (!mysqli_query($dbconnect, $query)) {
  die('An error occurred=  '. mysqli_error($dbconnect));
} else {
  echo "Success.";
}

mysqli_close($dbconnect);
?>
