<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";
$username = $_POST["user"];

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if($conn){
        $q="SELECT * FROM Fav_Bars_table WHERE fk_id_User LIKE '$username'";
        $result= mysqli_query($conn,$q);
        if(mysqli_num_rows($result) >0){
                while ($row = mysqli_fetch_assoc($result)){
                        $favBarId= $row["id_fav_bar"];
                        $barName= $row["fk_name_restaurant"];
                        $barAddress= $row["fk_address_restaurant"];
                        $restaurantId= $row["fk_id_restaurant"];
                        $results['Fav bars: '][] = array('FavBarId' => $favBarId, 'BarName' => $barName, 'BarAddress' => $barAddress, 'RestaurantId' => $restaurantID);
                }
                        echo json_encode($results);
        }else{
                echo json_encode("No Favorite bars creates");
        }
}else{
        echo "not connected";
}
mysqli_close($conn);
?>