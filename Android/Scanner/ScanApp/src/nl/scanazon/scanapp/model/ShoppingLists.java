package nl.scanazon.scanapp.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingLists extends ArrayList<ShoppingList> {

	private static final long serialVersionUID = 1645331127058965464L;
	
	private String message;

	  public String getMessage() {
	      return message;
	  }

	  public void setMessage(String message) {
	      this.message = message;
	  }

}
