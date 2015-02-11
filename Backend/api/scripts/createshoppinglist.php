<?php
function createShoppingList() {
// check of er voldoende info mee is gezonden
	if(empty($_POST["e-mail"]) or empty($_POST["password"]) or empty($_POST["naam"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
	$user = getUser($_POST["e-mail"], $_POST["password"]);
	$result = SendQuery("INSERT INTO `boodschappenlijst` (`gebruiker_id`, `naam`) VALUES (".$user.", '".$_POST["naam"]."')");
	// als het aanmaken gelukt is het nieuwe gegenereerde boodschappenlijstje
	if($result){
		$result = SendQuery("SELECT boodschappenlijst_id FROM boodschappenlijst WHERE `naam` = '".$_POST["naam"]."' AND gebruiker_id = ".$user);
		echo json_encode(mysql_fetch_assoc($result));
			
	}
}
?>
