import 'dart:html';

void main() {
  document.getElementById("signIn").onClick.listen((e) => signIn(e));
}


void signIn(Event e) {
  
  
  var email = document.getElementById("emailAddress");
  var password = document.getElementById("password");
  if (email.value != "" && password.value != "")
  {
     e.preventDefault();
     String answer = sendData();
     window.alert(answer);
     if (answer == "ACCEPTED") {
       // login success
     }
     else {
       // login failed
     }
  }
  
    // check if fields arent empty
  
    // Ajax Call

}

String sendData() {
  window.alert("test");
  var url = "http://127.0.0.1:80/WvdT/rest/login/";
  window.alert("hoi");
  // call the web server asynchronously
  var request = HttpRequest.getString(url).then(onDataLoaded);
}

void onDataLoaded(String responseText) {
  window.alert("ondataLoaded");
  var jsonString = responseText;
  print(jsonString);
  document.body.append(new HeadingElement.h1()..appendText(jsonString));
}

/*
String sendData(String email, String password) {
  HttpRequest request = new HttpRequest(); // create a new XHR
  
  // add an event handler that is called when the request finishes
  request.onReadyStateChange.listen((_) {
    if (request.readyState == HttpRequest.OPENED) {
      document.body.append(new HeadingElement.h1()..appendText("hoiu"));
    }
    if (request.readyState == HttpRequest.DONE) {
      // data saved OK.
      document.body.append(new HeadingElement.h1()..appendText(request.responseText));
      return request.responseText; // output the response from the server
    }
  });

  // POST the data to the server
  var url = "http://127.0.0.1/WvdT/rest/login/";
  request.open('post', url);
  String jsonData = '{"email":"'+email+'", "password":"'+password+'"}'; // etc...
  request.send(); // perform the async POST

}

*/