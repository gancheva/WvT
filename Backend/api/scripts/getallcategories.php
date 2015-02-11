<?php
function getAllCategories() {
	// query om alle categorieen op te halen, met de namen 
	Connect();
	$categorieen = null;
	$result = sendQuery("SELECT categorie_id, naam FROM categorieen");
	while($row = mysql_fetch_assoc($result)){
		$categorieen[$row['categorie_id']] = $row['naam'];
	}
	
	echo json_encode($categorieen);
}
?>
