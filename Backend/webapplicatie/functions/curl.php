<?php
use Guzzle\Service\Client;

class curl
{
    
    public function guzzleApiCall($url, $data)
    {
        $client = new Client();
        $request = $client->post($url, array(), $data);
        $response = $request->send();
        $body = json_decode($response->getBody(true));
        return $body;
    }
}
?>
