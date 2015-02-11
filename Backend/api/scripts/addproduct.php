<?php
function addProduct() {
// check of er voldoende info mee is gezonden
	if(empty($_POST["e-mail"]) or empty($_POST["password"]) or empty($_POST["boodschappenlijst_id"]) or empty($_POST["product_id"]) or !isset($_POST["aantal"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;
	}
        Connect();
	$user = getUser($_POST["e-mail"], $_POST["password"]);
        $result = SendQuery("SELECT * FROM `boodschappenlijstregel` WHERE `boodschappenlijst_id` = '".$_POST['boodschappenlijst_id']."' AND `product_id` = '".$_POST['product_id']."'");
        
        if(mysql_num_rows($result) === 1 && intval($_POST['aantal']) > 0)
        {
            $result = SendQuery("UPDATE `boodschappenlijstregel` SET `aantal` = '".$_POST['aantal']."' WHERE `boodschappenlijst_id` = '".$_POST['boodschappenlijst_id']."' AND `product_id` = '".$_POST['product_id']."'");
            $msg = array('ACCEPTED');
		echo json_encode($msg);
		return;
        }
        else if (mysql_num_rows($result) === 1)
        {
            $result = SendQuery("DELETE FROM `boodschappenlijstregel` WHERE `boodschappenlijst_id` = '".$_POST['boodschappenlijst_id']."' AND `product_id` = '".$_POST['product_id']."'");
        }
        else
        {
            $result = SendQuery("INSERT INTO `boodschappenlijstregel` (`boodschappenlijst_id`, `product_id`, `aantal`) VALUES (".$_POST["boodschappenlijst_id"].", '".$_POST["product_id"]."', '".$_POST["aantal"]."')");
        }
	// als het aanmaken gelukt is het nieuwe gegenereerde boodschappenlijstje
	if($result){
		$msg = array('message' => 'ACCEPTED');
		echo json_encode($msg);
		return;
			
	}
        else {
           $msg = array('message' => 'DENIED');
		echo json_encode($msg);
		return; 
        }
}
?>