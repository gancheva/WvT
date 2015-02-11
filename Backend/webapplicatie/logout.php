<?php 
session_start();
session_name("WvdT");
include("config.php");
require_once("functions/login.php");
$login = new Login();
if ($login->logout())
{
    header("location:index.php?logout=success");
} 
?>