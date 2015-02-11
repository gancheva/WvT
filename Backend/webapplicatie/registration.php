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

require_once("functions/register.php");
$register = new Register();
if (isset($_POST['register-password']) && isset($_POST['register-passwordRepeat'])&& isset($_POST['register-name']) && isset($_POST['register-email']))
{
    $register->register();
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
      
      <form class="form-signin" role="form" action="registration.php" method="post">
          
        <h2 class="form-signin-heading">Please register</h2>
        <?php 
        // show alert message
      if (isseT($_GET['register']) && $_GET['register'] === "failed")
      {
          echo '<div class="alert alert-danger">'.$_GET['result'].'</div>';
      }
      ?>
        <input type="text" class="form-control" name="register-email" placeholder="Email address" required autofocus>
        <br/>
        <input type="text" class="form-control" name="register-name" placeholder="Name" required>
        <br/>
        <input type="password" class="form-control" name="register-password" placeholder="Password" required>
        <input type="password" class="form-control" name="register-passwordRepeat" placeholder="Repeat password" required> 
        
        <button class="btn btn-lg btn-primary btn-block" type="submit">Register</button>
      </form>
     
    </div> <!-- /container -->

    <script type="application/dart" src="web/js/signin.dart"></script>
    <script src="packages/browser/dart.js"></script>
  </body>
</html>
