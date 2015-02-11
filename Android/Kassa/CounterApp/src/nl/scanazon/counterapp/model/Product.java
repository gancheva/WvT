package nl.scanazon.counterapp.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {
	
	private static final long serialVersionUID = -7077297009850970291L;
	
	private int product_id;
	private String naam;
	private float prijs;
	private int aantal;
	private float subtotaal;
	private float gewichtingram;
	
	public int getProduct_id() {
		return product_id;
	}
	
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	
	public String getNaam() {
		return naam;
	}
	
	public void setNaam(String naam) {
		this.naam = naam;
	}
	
	public float getPrijs() {
		return prijs;
	}
	
	public void setPrijs(float prijs) {
		this.prijs = prijs;
	}
	
	public int getAantal() {
		return aantal;
	}
	
	public void setAantal(int aantal) {
		this.aantal = aantal;
	}

	public float getGewichtingram() {
		return gewichtingram;
	}

	public void setGewichtingram(float gewichtingram) {
		this.gewichtingram = gewichtingram;
	}

	public float getSubtotaal() {
		return subtotaal;
	}

	public void setSubtotaal(float subtotaal) {
		this.subtotaal = subtotaal;
	}
	
	
}
