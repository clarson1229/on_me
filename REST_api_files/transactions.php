<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";
$username = $_GET["user"];

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if($conn){
        $q = "SELECT * FROM `Transactions_table` INNER JOIN `Restaurants_table` ON Restaurants_table.id_restaurant=Transactions_table.fk_recipient_restaurant WHERE Transactions_table.fk_sender_users='$username' OR Transactions_table.fk_id_recipient_user='$username';";
        $result = mysqli_query($conn,$q);
        if (!mysqli_query($conn, $q)) {
        die('An error occurred=  '. mysqli_error($conn));
        }else{
                while($row = mysqli_fetch_assoc($result)){
                        $transId= $row["id_transaction"];
                        $restaurantId= $row["fk_recipient_restaurant"];
                        $restaurantName=$row["name_restaurant"];
                        $sender=$row["fk_sender_users"];
                        $transAmount= $row["amount_transaction"];
                        $transMessage= $row["message_transaction"];
                        $date= $row["date_time_transaction"];
                        $reciever= $row["fk_id_recipient_user"];
                        $results['Transactions: '][] = array('TransactionId' => $transId, 'RestaurantId' => $restaurantId, '$RestaurantName' => $restaurantName, 'Sender' => $sender, 'TransactionAmount' => $transAmount,'Date' => $date, 'Reciever' => $reciever);
                  }
                        echo json_encode($results);
        }

}else{
        echo "not connected";
}
mysqli_close($conn);
?>
