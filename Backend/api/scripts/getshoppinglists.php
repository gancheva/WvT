<?php
function getShoppingLists() {
	//check input
	// deze methode werkt in basis hetzelfde als getshoppinglist, maar dan met meerdere boodschappenlijstjes. Hij doet voor elke boodschappenlijst de zelfde query
	if(empty($_POST["e-mail"]) or empty($_POST["password"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
		
	$user = getUser($_POST["e-mail"], $_POST["password"]);
	$lijsten = null;	
        $namen   = null;
	// boodschappenlijstjes ophalen van een gebruiker
	$result = SendQuery("SELECT boodschappenlijst_id, naam FROM boodschappenlijst WHERE gebruiker_id = ". $user);
	While($row = mysql_fetch_assoc($result))
	{
		$lijsten[] = $row['boodschappenlijst_id'];
                $namen[] = $row['naam'];
	}
	$boodschappenlijstjes = null;
	for($i = 0; $i < sizeof($lijsten); $i++){
		// per boodschappenlijstjes de regels ervan ophalen
		$result = SendQuery("SELECT product_id, aantal FROM boodschappenlijstregel WHERE boodschappenlijst_id = ". $lijsten[$i]);
		$producten = null;		
		$productinfo = null;
		While($row = mysql_fetch_assoc($result)){
			$producten[] = array('product_id' => $row['product_id'], 'aantal' => $row['aantal']);
					
		}
		
		for($b = 0; $b < sizeof($producten); $b++){
		//per regel het product ophalen en de naam prijs en aantal toevoegen
			$result = SendQuery("SELECT naam, prijs FROM producten WHERE product_id = ". $producten[$b]['product_id']);
			$array = mysql_fetch_row($result);
			$productinfo[$b] = array('product_id' => $producten[$b]['product_id'], 'naam' => $array[0], 'prijs' => $array[1], 'aantal' => $producten[$b]['aantal']);
				
		}
		// boodschappenlijstje in een array stoppen en opsturen
		$boodschappenlijstjes[] = array('boodschappenlijst_id' => $lijsten[$i], 'boodschappennaam' => $namen[$i], 'producten' => $productinfo);
	}
	
	echo json_encode($boodschappenlijstjes);
}
?>
