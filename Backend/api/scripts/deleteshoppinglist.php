<?php
function deleteShoppingList() {
	// check of alles is ingevuld
	if(empty($_POST["shoppinglist_id"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
	//lijst id uit de post halen
	$list_id = $_POST["shoppinglist_id"];
	
	Connect();
	// regels verwijderen uit de database
	$status = SendQuery("DELETE FROM boodschappenlijstregel WHERE boodschappenlijst_id = ".$list_id);
	if(!$status) {
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
	
	// boodschappenlijst zelf verwijderen
	$status = SendQuery("DELETE FROM boodschappenlijst WHERE boodschappenlijst_id = ".$list_id);
	if($status) {
		$error = array('message' => 'ACCEPTED');
		echo json_encode($error);
		return;
	}
	else {
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
}
?>
