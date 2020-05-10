<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";

$tranId = $_POST["tranId"];

$dbconnect=mysqli_connect($host,$dbuser,$pass,$db);

if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}
$query = "UPDATE `Transactions_table` SET `redeemedOrNot` = '1' WHERE `Transactions_table`.`id_transaction` = '$tranId'";


if (!mysqli_query($dbconnect, $query)) {
  die('An error occurred=  '. mysqli_error($dbconnect));
} else {
  echo "Success.";
}

mysqli_close($dbconnect);

?>
