<?php
$db = "on_me";
$dbuser = "root";
$pass = "gunni";
$host = "localhost";

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if ($dbconnect->connect_error) {
  die("Database connection failed: " . $dbconnect->connect_error);
}
$q = "SELECT * from Users_table";
if (!mysqli_query($dbconnect, $query)) {
      die('An error occurred=  '. mysqli_error($dbconnect));
} else {
      $result = mysqli_query($conn, $q);
      if (mysqli_num_rows($result) > 0){
      while($row = mysqli_fetch_assoc($result)){
              $userId= $row["id_User"];
              $firstName= $row["first_name_user"];
              $lastName= $row["last_name_user"];
              $results['Users'][] = array('userId' => $userId, 'firstName' => $firstName, 'lastName' => $lastName);
      }
      echo json_encode($results);
   }
}
mysqli_close($conn);
?>
