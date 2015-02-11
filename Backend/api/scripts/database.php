<?php
function Connect()
{	// database login credentials
	$databaseName="dibbit_wvt";
	$databaseHost="dibbit.nl";
    $databaseUser="dibbit_wvt";
	$databasePassword="wvt";
	
	// Connect
	$connect = mysql_connect($databaseHost,$databaseUser,$databasePassword);
	
		//check connection and select database
		if($connect){
			$selectDB = mysql_select_db($databaseName, $connect);
				if($selectDB){return true;}
				else{return false;}
		}
		else{
			return false;
}
		}
		// hulpklasse om queries te zenden en fouten af te handelen en uit te printen
function SendQuery($query){
	$result = mysql_query($query);
	if(!$result){
		$error = array('message' => 'DENIED');
		echo json_encode($error);
		die('No result: '. mysql_error());
		return;
		}
		else{
		return $result;
		}
	}
?>
