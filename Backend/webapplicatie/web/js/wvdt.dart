import 'dart:html';

void main() {
  if (!checkLogin()) {
    window.location.href = "signin.html";
  }
}

bool checkLogin() {
  return false;
}
