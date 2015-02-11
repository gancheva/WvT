<?php
require_once("curl.php");
class Register
{
    public function __construct()
    { 
        
    }
    
    public function register()
    {
        //TODO: escape chars security not provided in POC
        $email          = $_POST['register-email'];
        $password       = $_POST['register-password'];
        $passwordRepeat = $_POST['register-passwordRepeat'];
        $name           = $_POST['register-name'];
        $url            = $GLOBALS['serverURL']."reg";
        
        // check if password matches
        if ($password === $passwordRepeat)
        {
            // put variables in an array
            $array = array("naam" => $name, "e-mail" => $email, "password" => $password);
            // guzzleLibrary used for curl request to API server
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
                header("location:shoppingList.php?register=success");
            }
            else
            {
                header("location:registration.php?register=failed&result=".$response->message);
            }
        }
        else
        {
            header("location:registration.php?register=failed&result=Password doesn't match");
        }
        
        

    }
}
?>
