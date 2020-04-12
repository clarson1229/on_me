<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if($conn){
        $q="SELECT * FROM Restaurants_table";
        $result= mysqli_query($conn,$q);
        if(mysqli_num_rows($result) >0){
                while($row = mysqli_fetch_assoc($result)){
                        $restaurantId= $row["id_restaurant"];
                        $restaurantName= $row["name_restaurant"];
                        $restaurantAddress= $row["address_resturant"];
                        $restaurantPhone= $row["phone_restaurant"];
                        $restaurantHours= $row["hours_restaurant"];
                        $results['Resturants: '][] = array('RestaurantId' => $restaurantId, 'RestaurantName' => $restaurantName, 'restaurantAddress' => $restaurantAddress, 'RestaurantPhone' => $restaurantPhone, 'RestaurantHours' => $restaurantHours);
                }
                        echo json_encode($results);
        }else{
                echo json_encode($results);
        }
}else{
        echo "not connected";
}
mysqli_close($conn);
?>
