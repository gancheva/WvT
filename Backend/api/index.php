<?php
// laden van het SLIM routing framework
require 'vendor/autoload.php';
// de vershillende scripts zijn afgesplitst
$INC_DIR = $_SERVER["DOCUMENT_ROOT"]. "/wvt/api/scripts/"; 
include ($INC_DIR."registeruser.php");
include ($INC_DIR."loginuser.php");
include ($INC_DIR."getproduct.php");
include ($INC_DIR."getshoppinglists.php");
include ($INC_DIR."getshoppinglist.php");
include ($INC_DIR."createshoppinglist.php");
include ($INC_DIR."deleteshoppinglist.php");
include ($INC_DIR."updateshoppinglist.php");
include ($INC_DIR."getallcategoryproducts.php");
include ($INC_DIR."getallproducts.php");
include ($INC_DIR."getallcategories.php");
include ($INC_DIR."getinvoice.php");
include ($INC_DIR."database.php");
include ($INC_DIR."addproduct.php");
// start het framework
$app = new \Slim\Slim();
$app->contentType('application/json');
// alle routes met de functies waar SLIM de call naar doorverwijst
$app->post('/reg', 'registerUser');
$app->post('/login', 'loginUser');
$app->post('/getproduct', 'getProduct');
$app->post('/getlists', 'getShoppingLists');
$app->post('/getlist', 'getShoppingList');
$app->post('/getshoppinglist', 'getShoppingListNew');
$app->post('/create', 'createShoppingList');
$app->post('/delete', 'deleteShoppingList');
$app->post('/update', 'updateShoppingList');
$app->post('/getall', 'getAllCategoryProducts');
$app->post('/getallproducts', 'getAllProducts');
$app->post('/getcat', 'getAllCategories');
$app->post('/gettotal', 'getInvoice');
$app->post('/addproduct', 'addProduct');

$app->run();
?>