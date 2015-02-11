<?php
function updateShoppingList() {
		$json = file_get_contents('php://input');
		if(!isset($json))
		{
				$msg = array('message' => 'DENIED2');
				echo json_encode($msg);
				return;	
		}
		
		//$json = $_POST['json'];
		
		$json = htmlspecialchars_decode($json);
		$json = substr($json, 1, (strlen($json) - 2));
		//boodschappenlijst om te updaten
		$boodschappenlijst = json_decode($json);
		//print_r($boodschappenlijst);
		
		$email = $boodschappenlijst->email;
		$password = $boodschappenlijst->password;
		
		if(!isset($email) or !isset($password)){
				$msg = array('message' => 'DENIED');
				echo json_encode($msg);
				return;
		}
		
		Connect();
		//regels verwijderen van oude boodschappenlijst	
		SendQuery("DELETE FROM boodschappenlijstregel WHERE boodschappenlijst_id = ".$boodschappenlijst->boodschappenlijst_id);
		$resulttest = mysql_fetch_assoc(SendQuery("SELECT regel_id FROM boodschappenlijstregel WHERE boodschappenlijst_id = ".$boodschappenlijst->boodschappenlijst_id));
		if($resulttest)
		{
				$msg = array('message' => 'DENIED');
				echo json_encode($msg);
				return;
		}
			
		for($i = 0; $i < sizeof($boodschappenlijst->producten); $i ++){
			SendQuery("INSERT INTO boodschappenlijstregel (boodschappenlijst_id, product_id, aantal) 
					VALUES (".$boodschappenlijst->boodschappenlijst_id.", ".$boodschappenlijst->producten[$i]->product_id.", ".$boodschappenlijst->producten[$i]->aantal.")"); 
		}
		
		$msg = array('message' => 'ACCEPTED');
				echo json_encode($msg);
				return;
	}	
?>
