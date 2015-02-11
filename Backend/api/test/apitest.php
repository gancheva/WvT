<?php
//test gegevens

$naam = "Sjakie";
$email = "sjakie@gmail.com";
$password = "test";
$productid = "5";

// test om te registreren
$fields = array('naam' => urlencode($naam), 'e-mail' => urlencode($email), 'password' => urlencode($password));
$url = "reg";
echo nl2br("register test \t\t\t");
sleep(1);
testPost($url, $fields);
sleep(1);

// test om in te loggen
$url = "login";
echo "\nlogin test \t\t\t";
sleep(1);
testPost($url, $fields);
sleep(1);

// test om een product op te vragen
$url = "getproduct";
$fields = array ('product_id' => urlencode($productid));
echo "\nget a product test \t\t";
sleep(1);
testPost($url, $fields);
sleep(1);

// test een lijstje aan te maken
$url = "create";
$fields = array('e-mail' => urlencode($email), 'password' => urlencode($password), 'naam' => urlencode($naam));
echo "\ncreate a list test\t\t";
sleep(1);
testPost($url, $fields);
sleep(1);

// test om de lijstjes op te halen
$url = "getlists";
$fields = array('e-mail' => urlencode($email), 'password' => urlencode($password));
echo "\nget all shoppinglists test \t";
sleep(1);
testPost($url, $fields);
sleep(1);

// test om de lijst te deleten
$url = "delete";
$fields = array('e-mail' => urlencode($email), 'password' => urlencode($password), 'naam' => urlencode($naam));
echo "\n delete shopping list test \t";
sleep(1);
testPost($url, $fields);
sleep(1);

// test om alle producten per categorie te krijgen
$url = "getall";
$fields = array('e-mail' => urlencode($email), 'password' => urlencode($password), 'categorie_id' => urlencode("1"));
echo "\n get all products from categorie";
sleep(1);
testPost($url, $fields);
sleep(1);

// test om alle producten te krijgen
$url = "getallproducts";
$fields = array('e-mail' => urlencode($email), 'password' => urlencode($password), 'naam' => urlencode($naam));
echo "\n all products test \t\t";
sleep(1);
testPost($url, $fields);
sleep(1);

// test om alle categorieen te krijgen
$url = "getcat";
$fields = array('e-mail' => urlencode($email), 'password' => urlencode($password));
echo "\n allcategories test \t\t";
sleep(1);
testPost($url, $fields);
sleep(1);

// test om het totaal bedrag te krijgen 
$url = "gettotal";
$fields = array('boodschappenlijst_id' => urlencode("49"));
echo "\n totaalprijs test \t\t\t";
sleep(1);
testPost($url, $fields);
sleep(1);

// test om een product toe te voegen
$url = "addproduct";
$fields = array('e-mail' => urlencode($email), 'password' => urlencode($password), 'boodschappenlijst_id' => urlencode("49"), 'product_id' => urlencode("2"), 'aantal' => urlencode("1"));
echo "\n add product test \t\t\t";
sleep(1);
testPost($url, $fields);
sleep(1);

function testPost($url, $fields){
$fields_string = null;
$url = "http://dibbit.nl/wvt/api/".$url;

//url-ify the data for the POST
foreach($fields as $key=>$value) { $fields_string .= $key.'='.$value.'&'; }
rtrim($fields_string, '&');

//open connection
$ch = curl_init();

//set the url, number of POST vars, POST data
curl_setopt($ch,CURLOPT_URL, $url);
curl_setopt($ch,CURLOPT_POST, count($fields));
curl_setopt($ch,CURLOPT_POSTFIELDS, $fields_string);

//execute post
$result = curl_exec($ch);

//close connection
curl_close($ch);
}

?>