package nl.scanazon.counterapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice extends Message {

	private int gebruiker_id;
	private ProductList producten;
	private float totaal;
	
	public int getGebruiker_id() {
		return gebruiker_id;
	}
	
	public void setGebruiker_id(int gebruiker_id) {
		this.gebruiker_id = gebruiker_id;
	}

	public ProductList getProducten() {
		return producten;
	}

	public void setProducten(ProductList producten) {
		this.producten = producten;
	}

	public float getTotaal() {
		return totaal;
	}

	public void setTotaal(float totaal) {
		this.totaal = totaal;
	}
	
}
