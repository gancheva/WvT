<?php
function getAllCategoryProducts() {
// input gegevens checken
	if(empty($_POST["e-mail"]) or empty($_POST["password"]) or empty($_POST["categorie_id"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
	
	$email = $_POST["e-mail"];
	$pass = $_POST["password"];
	$categorie_id = $_POST["categorie_id"];
	$producten = null;
	Connect();
	// lijst opbouwen om te laten zien op de website, product_id, naam, gewicht in gram worden gebundeld door een join
	$result = SendQuery("SELECT producten.product_id, producten.naam, producten.gewichtingram, producten.prijs FROM productcategorie JOIN producten ON productcategorie.product_id = producten.product_id WHERE productcategorie.categorie_id = ".$categorie_id);
	while($row = mysql_fetch_assoc($result)){
		$producten[] = array("product_id" => $row['product_id'], "naam" => $row['naam'], "gewichtingram" => $row['gewichtingram'], "prijs" => $row['prijs']);
	}
	
	echo json_encode($producten);
}
?>
