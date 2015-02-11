<?php 
session_start();
session_name("WvdT");
include("config.php");
require_once("functions/login.php");

require_once 'vendor/autoload.php';

$login = new Login();
if ($login->isLoggedIn())
{
    header("location:shoppingList.php");
} 

if (isset($_POST['login-password']) && isset($_POST['login-email']))
{
    $login->login();
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

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
      


    <div class="container">
        <div class="submitarea">
            
            <form id="login" method="post" action="index.php" class="form-signin" name="login" role="form">
              <h2 class="form-signin-heading">Please sign in</h2>
               <?php 
               // show alert message
                if (isseT($_GET['login']) && $_GET['login'] === "failed")
                {
                    echo '<div class="alert alert-danger">'.$_GET['result'].'</div>';
                }
                ?>
              <input type="text" id="emailAddress" name="login-email" class="form-control" placeholder="Email address" required autofocus>
              <input type="password" id="password" name="login-password" class="form-control" placeholder="Password" required>  
              <input value="Sign in!" class="btn btn-lg btn-primary btn-block" id="signIn" type="submit">
                <br/> <br/>
               Not yet registered? <a href='registration.php'>Sign up!</a> 
            </form>
       </div>
    </div> <!-- /container -->

  </body>
</html>
