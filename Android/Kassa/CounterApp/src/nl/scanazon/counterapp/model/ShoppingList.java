package nl.scanazon.counterapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingList extends Message implements Parcelable {
	
	private int boodschappenlijst_id;
	private String boodschappennaam;
	private ProductList producten;
	
	public ShoppingList() {
		
	}
	
	public int getBoodschappenlijst_id() {
		return boodschappenlijst_id;
	}
	
	public void setBoodschappenlijst_id(int boodschappenlijst_id) {
		this.boodschappenlijst_id = boodschappenlijst_id;
	}
	
	public String getBoodschappennaam() {
		return boodschappennaam;
	}
	
	public void setBoodschappennaam(String boodschappennaam) {
		this.boodschappennaam = boodschappennaam;
	}
	
	public ProductList getProducten() {
		return producten;
	}
	
	public void setProducten(ProductList producten) {
		this.producten = producten;
	}
	
	public static final Parcelable.Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
		 
	    @Override
	    public ShoppingList createFromParcel(Parcel source) {
	       return new ShoppingList(source);
	    }
	 
	   @Override
	   public ShoppingList[] newArray(int size) {
	      return new ShoppingList[0];
	   }
	};

	public ShoppingList(Parcel in) {
		setBoodschappenlijst_id(in.readInt());
		setBoodschappennaam(in.readString());
		setProducten((ProductList) in.readSerializable());
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(boodschappenlijst_id);
		out.writeString(boodschappennaam);
		out.writeSerializable(producten);
	}
	
}
