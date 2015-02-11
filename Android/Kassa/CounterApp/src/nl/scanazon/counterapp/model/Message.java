package nl.scanazon.counterapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

  private String message;

  public String getMessage() {
      return message;
  }

  public void setMessage(String message) {
      this.message = message;
  }
}
