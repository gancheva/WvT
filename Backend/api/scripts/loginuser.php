<?php
function loginUser() {
	if(empty($_POST["e-mail"]) or empty($_POST["password"])){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		return;	
	}
	
	$email = $_POST["e-mail"];
	$pass = $_POST["password"];
	authUser($email, $pass);
		
}

function getUser($email, $password)
{
	Connect();
	if($row = mysql_fetch_assoc(SendQuery("SELECT gebruiker_id FROM gebruikers WHERE email = '". $email ."' AND wachtwoord = '". $password."'")))
	return($row['gebruiker_id']);
}

function authUser($email, $pass) {
	Connect();
	$result = SendQuery("SELECT wachtwoord FROM gebruikers WHERE email = '".$email."'");
	if(mysql_num_rows($result) > 0)
	{
		$row = mysql_fetch_assoc($result);
		if($pass === $row['wachtwoord']){
			$msg = array('message' => 'ACCEPTED');
			echo json_encode($msg);
			return;
		}
                else {
                    // incorrect password or email
                    $msg = array('message' => 'Incorrect password or email');
                    echo json_encode($msg);
                    return;
                }
	} else {
		$msg = array('message' => 'EMAIL NOT FOUND');
		echo json_encode($msg);
		return;
	}	
}


?>
