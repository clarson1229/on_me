<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";

$userId = $_POST["user"];
$firstName = $_POST["firstName"];
$lastName = $_POST["lastName"];
$email = $_POST["email"];
$address = $_POST["address"];

$dbconnect=mysqli_connect($host,$dbuser,$pass,$db);

if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}
$query = "UPDATE `Users_table` SET `first_name_user`='$firstName',`last_name_user`='$lastName',`address_user`='$address',`email_user`='$email' WHERE `Users_table`.`id_User` = '$userId'";

if (!mysqli_query($dbconnect, $query)) {
  die('An error occurred=  '. mysqli_error($dbconnect));
} else {
  echo "Success.";
}

mysqli_close($dbconnect);
?>
