<?php
function getInvoice() {
// input checken
	if(empty($_POST["boodschappenlijst_id"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
	
	$lijstid = $_POST["boodschappenlijst_id"];
	$producten = null;	
	$totaal = null;
	Connect();
	// producten die op de boodschappenlijst staan
	$result = SendQuery("SELECT product_id, aantal FROM boodschappenlijstregel WHERE boodschappenlijst_id = ".$lijstid);
	while($row = mysql_fetch_assoc($result)){
		$producten[] = array('product_id' =>$row['product_id'], 'aantal' => $row['aantal']); 
	}
	// voor elk product, het subtotaal en totaal prijs optellen en in de array meegeven
	for($i = 0; $i < sizeof($producten); $i++){
		$subtotaal = null;
		$productprijs = null;
		$productprijs = mysql_result(SendQuery("SELECT prijs FROM producten WHERE product_id = ". $producten[$i]['product_id']), 0);
		$productnaam = mysql_result(SendQuery("SELECT naam FROM producten WHERE product_id = ". $producten[$i]['product_id']), 0);
		$subtotaal = $productprijs * $producten[$i]['aantal'];
		$totaal += $subtotaal;
		$producten[$i] = array('product_id' => $producten[$i]['product_id'], 'naam' => $productnaam, 'aantal' => $producten[$i]['aantal'], 'prijs' => $productprijs, 'subtotaal' => $subtotaal);
	}	
	
	$gebruiker_id = mysql_result(SendQuery("SELECT gebruiker_id FROM boodschappenlijst WHERE boodschappenlijst_id = ".$lijstid), 0);
	// het totaal resultaat, subtotalen per product en totaal zijn toegevoegd 
	$json = array('gebruiker_id' => $gebruiker_id, 'producten' => $producten, 'totaal' => $totaal);
	echo json_encode($json);
}
?>
