<?php
function getProduct() {
// input checken
	if(empty($_POST["product_id"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
	
	$productid = $_POST["product_id"];
	$product = null;
	Connect();
	// een product query
	$result = SendQuery("SELECT * FROM producten WHERE product_id = ".$productid);
	if($result){
		$product = mysql_fetch_assoc($result);
	}
	
	echo json_encode($product);
}
?>
