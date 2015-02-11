<?php
require_once 'curl.php';
class ShoppingList
{
    private $myusername;
    private $mypassword;
    
    public function __construct()
    { 
        
    }
    
    /**
     * get the shopping lists from the logged in user
     * @return response
     */
    public function getShoppingList()
    {
        //TODO: escape chars security issues not handled in POC
        $email    = $_SESSION['email'];
        $password = $_SESSION['password'];
        $url = $GLOBALS['serverURL']."getlists";
        
        $array = array("e-mail" => $email, "password" => $password);
        $curl = new curl();
        $response = $curl->guzzleApiCall($url, $array);
        return $response;
    }
    
    /**
     * create a new shopping list
     * @return response
     */
    public function newShoppingList()
    {
        //TODO: escape chars
        $email    = $_SESSION['email'];
        $password = $_SESSION['password'];
        $name     = $_POST['new-boodschappenlijst-name'];
        
        $url = $GLOBALS['serverURL']."create";
        
        $array = array("e-mail" => $email, "password" => $password, "naam" => $name);
        $curl = new curl();
        $response = $curl->guzzleApiCall($url, $array);
        
        return $response;
    }
    
    /**
     * get all categories
     * @return response
     */
    public function getCategories()
    {
        $url = $GLOBALS['serverURL']."getcat";
        
        $array = null;
        $curl = new curl();
        $response = $curl->guzzleApiCall($url, $array);
        
        return $response;
    }
    
    /**
     * Get all the products on a shoppinglist
     * @param type $shoplistID
     * @return response
     */
    public function getProductsFromShoppingList($shoplistID)
    {
        //TODO: escape chars
        $email    = $_SESSION['email'];
        $password = $_SESSION['password'];
        
        $url = $GLOBALS['serverURL']."getlist";
        $array = array("e-mail" => $email, "password" => $password, "shoppinglist_id" => $shoplistID);
        $curl = new curl();
        $response = $curl->guzzleApiCall($url, $array);
        
        return $response;

    }
    
    /**
     * Get all the products
     * @return response
     */
    public function getProducts()
    {
        $url = $GLOBALS['serverURL']."getallproducts";
        $array = null;
        $curl = new curl();
        $response = $curl->guzzleApiCall($url, $array);
        
        return $response;
    }
    
    /**
     * Get all products linked to a category
     * @param type $categoryID
     * @return response
     */  
    public function getProductsWithCategories($categoryID)
    {
        //TODO: escape chars
        $email    = $_SESSION['email'];
        $password = $_SESSION['password'];
        //TODO: change api url
        $url = $GLOBALS['serverURL']."getall";
        $array = array("e-mail" => $email, "password" => $password, "categorie_id" => $categoryID);
        $curl = new curl();
        $response = $curl->guzzleApiCall($url, $array);
        return $response;
    }
    
    /**
     * Add a product to the current shoppinglist
     */
    public function addProduct()
    {
        //TODO: escape chars
        $email    = $_SESSION['email'];
        $password = $_SESSION['password'];
        $boodschappenlijstID = $_POST['boodschappenlijst_id'];
        $productID = $_POST['product_id'];
        $aantal = $_POST['aantal'];
        //TODO: change api url
        $url = $GLOBALS['serverURL']."addproduct";
        $array = array("e-mail" => $email, "password" => $password, "boodschappenlijst_id" => $boodschappenlijstID, "product_id" => $productID, "aantal" => $aantal);
        
        $curl = new curl();
        $response = $curl->guzzleApiCall($url, $array);
       
        header("location:".$_SERVER['REQUEST_URI']);
    }
}
?>
