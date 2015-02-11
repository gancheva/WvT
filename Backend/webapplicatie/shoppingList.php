<?php 
session_start();
session_name("WvdT");
include("config.php");
require_once("functions/login.php");
require_once("functions/shoppingList.php");

require_once 'vendor/autoload.php';
// Check if the user is logged in
$login = new Login();
if (!$login->isLoggedIn())
{
    header("location:index.php");
} 

$shoppingList = new ShoppingList();
// check if new boodschappenlijst is created
if (isset($_POST['new-boodschappenlijst-name']) && !empty($_POST['new-boodschappenlijst-name']))
{
    // call method to create new boodschappenlijst
    $shoppingList->newShoppingList();
    
}
// get the shopping list from the users and categories of products
$shopLists  = $shoppingList->getShoppingList();
$categories = $shoppingList->getCategories();

// get products on shopping list if a shopping list is selected
$productsFromShopList = null;
$shopListId = null;
if (isset($_GET['shoppingListId']))
{
    $shopListId = $_GET['shoppingListId'];
    
    $productsFromShopList   = $shoppingList->getProductsFromShoppingList($shopListId);
    
}

// if category is selected get products with a category otherwise get all products
if (isset($_GET['categoryID']))
{
    $categoryID = $_GET['categoryID'];
    $products = $shoppingList->getProductsWithCategories($categoryID);
}
else
{
    $categoryID = null;
    $products   = $shoppingList->getProducts();
}

// set the searchTerm used 
if (isset($_GET['search']))
{
    $searchTerm = $_GET['search'];
}
else
{
    $searchTerm = null;
}

// call the add product to shoppinglist
if (isset($_POST['addNewProduct']) && $_POST['addNewProduct'] === "true")
{
    $shoppingList->addProduct();
}

?>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Signin Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="web/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="web/css/wvdt.css" rel="stylesheet">
    <link href="web/css/shoppingList.css" rel="stylesheet">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
  
    <div class="info">
        <?php echo 'Welkom terug '.$_SESSION['email'].', <a href="logout.php">Logout</a>'; ?>
    </div>
  <div class="container">
      <div class="left">
            <div class="shoppingList">
                <form action="shoppingList.php" method="post" class="form-inline" role="form">
                    <div class="form-group">
                      <input type="text" class="form-control" name="new-boodschappenlijst-name" id="naam" placeholder="Naam">
                    </div>
                    <button type="submit" class="btn btn-default">New shoppinglist</button>
                  </form>
                <br/>
              <?php
              if (count($shopLists) > 0) { ?>             
                  
              <select name="exampleselect" onchange="window.location=this.value;">
                  <option disabled selected="selected">Select your shopping list</option>
                  <?php
                  
                    // loop through all the shopping lists
                    foreach($shopLists as $shopList)
                    {
                      if (isset($_GET['shoppingListId']) && $_GET['shoppingListId'] === $shopList->boodschappenlijst_id)
                      {
                        echo '<option selected="selected" value="shoppingList.php?shoppingListId='.$shopList->boodschappenlijst_id.'">'.$shopList->boodschappennaam.'</option>';
                      }
                      else
                      {
                          echo '<option value="shoppingList.php?shoppingListId='.$shopList->boodschappenlijst_id.'">'.$shopList->boodschappennaam.'</option>';
                      }
                    }   
                     
                  ?>
              </select>
              <?php }
              
              if (count($productsFromShopList) > 0)
              {
                  $i = 0;
                  echo '<table>
                        <tr>
                            <th>Naam</th>
                            <th>Gram</th>
                            <th>Aantal</th>
                            <th>Prijs</th>
                         </tr>';
                  $totaalprijs = 0;
                  // loop through all products on shoppinglist
                foreach($productsFromShopList as $product)
                {
                    if ($i === 0)
                    {
                        echo '<h1>'.$product->boodschappenljst_naam.'</h1>';
                    }
                    
                    $prijs = number_format($product->aantal * $product->prijs, 2);
                    $totaalprijs += $prijs;
                    echo '<tr><td width="150px">'.$product->product_naam.'</td><td width="75px">'.$product->gewichtingram.'</td><td width="70px">'.$product->aantal.'</td><td>&euro;'.$prijs.'</td></tr>';
                    $i = 1;
                }
                echo '<tr><td colspan="3" align="right"><strong>Totaal:<strong></td><td>&euro;'.number_format($totaalprijs,2).'</td></tr>';
                echo'</table>';
              
              }
              ?>
            </div>
      </div>
      <div class="right">
        <div class="search">
            
            <form class="navbar-form" action="" method="get" role="search">
                <?php
                if (isset($shopListId))
                {
                    echo '<input type="hidden" name="shoppingListId" value="'.$shopListId.'">';
                }
                if (isset($categoryID))
                {
                    echo '<input type="hidden" name="categoryID" value="'.$categoryID.'">';
                }
                echo '
            <div class="input-group">
                
                <input type="text" class="form-control" placeholder="Search" name="search" id="srch-term" value="'.$searchTerm.'">';
                    
                   ?>
                <div class="input-group-btn">
                    <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                </div>
            </div>
            </form>
        </div>
        <div class="categories">
          <h2>Categorieen</h2>
          <table>
              <tr>
            <?php
            $i = 0;
            
            // loop through all categories
            foreach ($categories as $id => $naam)
            {
                // 4 categories per row
                if ($i === 4)
                {
                    $i = 0;
                    echo "</tr><tr>";
                }
                // for specifiek urls receive the current get headers
                if ($categoryID === null && ($shopListId || $searchTerm != null))
                {
                    echo "<td width='200px'><a href='".$_SERVER['REQUEST_URI']."&categoryID=".$id."'>".$naam."</a></td>";
                    
                }
                else if($categoryID != null)
                {
                    // just replace the categoryID
                    $url = str_replace("categoryID=".$categoryID, "categoryID=".$id, $_SERVER['REQUEST_URI']);
                    echo "<td width='200px'><a href='".$url."'>".$naam."</a></td>";
                }
                else
                {
                    echo "<td width='200px'><a href='?categoryID=".$id."'>".$naam."</a></td>";
                }
                $i ++;
            }
            
            // remove the category selection
            $url = str_replace("categoryID=".$categoryID, "", $_SERVER['REQUEST_URI']);
                  echo "<td width='200px'><a href='".$url."'>Verwijder selectie</a></td>";
                          ?>
              </tr>
          </table>
        </div>
        <div class="producten">
          <h2>Producten</h2>
          <div class="row">
            <tr class="table-row-line">
              <div class="col-xs-3"><strong>Naam</strong></div>
              <div class="col-xs-3"><strong>Gewicht (in gram)</strong></div>
              <div class="col-xs-2"><strong>Prijs</strong></div>
          </div>
            <?php
            if (count($products) > 0)
            {
                
                // loop through all products
                 foreach($products as $product)
                {
                     
                     // check if searchTerm match if so showproduct otherwise dont show
                    if ($searchTerm == null || strpos($product->naam,$_GET['search']) !== false) {
                            $showProduct = true;
                    } else {
                        $showProduct = false;
                        
                    }
                    if ($showProduct)
                    {
                        echo'<div class="row">
                            <form action="" method="post">
                            <input type="hidden" name="product_id" value="'.$product->product_id.'">
                            <input type="hidden" name="addNewProduct" value="true">
                            <input type="hidden" name="boodschappenlijst_id" value="'.$shopListId.'">
                                <div class="col-xs-3">
                                    '.$product->naam.'
                                </div>
                            <div class="col-xs-3">'.$product->gewichtingram.' (in gram)</div>
                            <div class="col-xs-2">&euro;'.number_format($product->prijs,2).'</div>
                        ';
                        if ($shopListId)
                        {
                        echo '
                                   <div class="col-xs-2"> <input type="text" class="form-control" placeholder="Aantal" name="aantal"></div>
                                   <div class="col-xs-2">  <input type="submit" class="btn btn-primary btn-sm" value="Add"></div>';

                        }
                        echo '</form></div>';
                    }
                }
            }
            ?>
        </div>
      </div>
  </div>
    <script type="application/dart" src="web/js/wvdt.dart"></script>
    <script src="packages/browser/dart.js"></script>
  </body>
</html>
