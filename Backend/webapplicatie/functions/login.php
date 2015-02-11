<?php
include ("curl.php");
class Login
{
    private $myusername;
    private $mypassword;
    
    public function __construct()
    { 
        
    }
    
    public function login()
    {
        //TODO: escape chars
        $email    = $_POST['login-email'];
        $password = $_POST['login-password'];
        $url = $GLOBALS['serverURL']."login";
        // put variables in an array
        $array = array("e-mail" => $email, "password" => $password);
        // guzzle library used for HTTP request to API server
        $curl = new curl();
        $response = $curl->guzzleApiCall($url, $array);
        
        // look how the server handled the request
        // if accepted go to next page otherwise try to register again
        if ($response->message === "ACCEPTED")
        {
            // go to next page
            // set cookie
            $_SESSION['login'] = true;
            $_SESSION['email'] = $email;
            $_SESSION['password'] = $password;
            header("location:shoppingList.php");
        }
        else
        {
            header("location:index.php?login=failed&result=".$response->message);
        }
        

    }
    
    /**
     * check if session is set
     * @return boolean
     */
    public function isLoggedIn()
    {
        // check the sessie
        if(isset($_SESSION['login'])){
            return true;
        }
        else{
            return false;
        } 
    }
    
    /**
     * destroy session and return to startpage
     */
    public function logout() {
        session_destroy();
        header("location:index.php");
    }
}
?>
