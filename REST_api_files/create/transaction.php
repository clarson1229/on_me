<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";

$sender = $_POST["sender"];
$reciever = $_POST["reciever"];
$amount = $_POST["amount"];
$message = $_POST["message"];
$restRecieverId = $_POST["restId"];
$restRecieverName = $_POST["restName"];

$dt = date("Y-m-d H:i:s");

$dbconnect=mysqli_connect($host,$dbuser,$pass,$db);

if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}
$query = "INSERT INTO Transactions_table (id_transaction, fk_recipient_restaurant, fk_sender_users, amount_transaction, message_transaction, date_time_transaction, fk_id_recipient_user)
  VALUES (NULL, '$restRecieverId', '$sender', '$amount', '$message', '$dt', '$reciever')";

if (!mysqli_query($dbconnect, $query)) {
  die('An error occurred=  '. mysqli_error($dbconnect));
} else {
  echo "Success.";
}

mysqli_close($dbconnect);

?>
