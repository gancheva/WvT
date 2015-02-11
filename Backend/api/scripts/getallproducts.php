<?php
function getAllProducts() {
	$producten = null;
	Connect();
	// alle producten query en in een json array stoppen
	$result = SendQuery("SELECT product_id, naam, gewichtingram, prijs FROM producten");
	while($row = mysql_fetch_assoc($result)){
		$producten[] = array("product_id" => $row['product_id'], "naam" => $row['naam'], "gewichtingram" => $row['gewichtingram'], "prijs" => $row['prijs']);
	}
	
	echo json_encode($producten);
}
?>
