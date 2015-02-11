<?php
function getShoppingList() {
	//input check
	if(empty($_POST["e-mail"]) or empty($_POST["password"]) or empty($_POST["shoppinglist_id"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
		
	$user = getUser($_POST["e-mail"], $_POST["password"]);
	$producten = null;	
	// Alle producten van de boodschappenlijst in een array, samen met het id en de naam van het project
	$result = SendQuery("SELECT boodschappenlijst.boodschappenlijst_id, boodschappenlijst.naam AS boodschappenlijst_naam, aantal, producten.naam AS product_naam, prijs, gewichtingram, regel_id FROM boodschappenlijst JOIN boodschappenlijstregel ON boodschappenlijst.boodschappenlijst_id = boodschappenlijstregel.boodschappenlijst_id JOIN producten ON boodschappenlijstregel.product_id = producten.product_id WHERE boodschappenlijst.boodschappenlijst_id = ". $_POST["shoppinglist_id"]);
	while($row = mysql_fetch_assoc($result)) {
		$producten[] = array("boodschappenljst_id" => $row['boodschappenlijst_id'], "boodschappenljst_naam" => $row['boodschappenlijst_naam'], "aantal" => $row['aantal'], "product_naam" => $row['product_naam'], "prijs" => $row['prijs'], "gewichtingram" => $row['gewichtingram'], "regel_id" => $row['regel_id']);   
	}
	
        echo json_encode($producten);
}
 /*      Hieronder is een andere versie van de functie, boven wordt gewerkt met een join, onder wordt een json object gebouwd */
function getShoppingListNew() {
		if(empty($_POST["shoppinglist_id"])){
			$error = array('message' => 'DENIED');
			echo json_encode($error);
			return;
		}
		
		Connect();
		$listId = $_POST["shoppinglist_id"];
		
		$result = SendQuery("SELECT naam FROM boodschappenlijst WHERE boodschappenlijst_id = ". $listId);
		$result = mysql_fetch_assoc($result);
		
		$listName = $result['naam'];
		
		$result = SendQuery("SELECT product_id, aantal FROM boodschappenlijstregel WHERE boodschappenlijst_id = ". $listId);
		$producten = null;		
		$productinfo = null;
		// alle producten in een array zetten
		While($row = mysql_fetch_assoc($result)){
			$producten[] = array('product_id' => $row['product_id'], 'aantal' => $row['aantal']);
					
		}
		// per product de subtotaal prijs en totaal prijs berekenen
		for($b = 0; $b < sizeof($producten); $b++){
			$result = SendQuery("SELECT naam, prijs FROM producten WHERE product_id = ". $producten[$b]['product_id']);
			$array = mysql_fetch_row($result);
			$productinfo[$b] = array('product_id' => $producten[$b]['product_id'], 'naam' => $array[0], 'prijs' => $array[1], 'aantal' => $producten[$b]['aantal']);
				
		}
		$boodschappenlijstje = array('boodschappenlijst_id' => $listId, 'boodschappennaam' => $listName, 'producten' => $productinfo);
		
		echo json_encode($boodschappenlijstje);
}
?>
