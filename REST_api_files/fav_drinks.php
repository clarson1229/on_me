<?php
$db = "on_me";
$dbuser = "";
$pass = "";
$host = "localhost";
$username = $_POST["user"];

$conn = mysqli_connect($host,$dbuser, $pass, $db);
if($conn){
        $q="SELECT * FROM Drinks_table WHERE fk_id_User LIKE '$username'";
        $result= mysqli_query($conn,$q);
        if(mysqli_num_rows($result) >0){
                while ($row = mysqli_fetch_assoc($result)){
                        $drinkId= $row["id_drinks"];
                        $drinkName= $row["name_drink"];
                        $drinkDescription= $row["description_drink"];
                        
                        $results['Fav Drinks: '][] = array('DrinkId' => $drinkId, 'DrinkName' => $drinkName, 'DrinkDescription' => $drinkDescription);
                }
                        echo json_encode($results);
        }else{
                echo json_encode("No drinks bars created");
        }
}else{
        echo "not connected";
}
mysqli_close($conn);
?>