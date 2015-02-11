<?php

function registerUser() {
	if(empty($_POST["e-mail"]) or empty($_POST["password"]) or empty($_POST["naam"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;	
	}
	
	$email = $_POST["e-mail"];
	$pass = $_POST["password"];
	$naam = $_POST["naam"];
	
	Connect();
	$result = SendQuery("SELECT gebruiker_id FROM gebruikers WHERE email = '".$email."'");
	if(mysql_fetch_row($result))
	{
		$error = array('message' => 'EMAIL IN USE');
		echo json_encode($error);
		return;
	}
	
	if(SendQuery("INSERT INTO  `gebruikers` (`gebruiker_id` , `naam` , `email` , `wachtwoord`) VALUES (NULL , '".$naam."',  '".$email."',  '".$pass."')")){
		$error = array('message' => 'ACCEPTED');
		echo json_encode($error);
		return;
	}
					
}
?>
