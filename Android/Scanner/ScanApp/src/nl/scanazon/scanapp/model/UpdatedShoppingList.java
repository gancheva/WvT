package nl.scanazon.scanapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UpdatedShoppingList extends ShoppingList {
	
	private String email;
	private String password;
	
	public UpdatedShoppingList() {
		
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public static final Parcelable.Creator<UpdatedShoppingList> CREATOR = new Creator<UpdatedShoppingList>() {
		 
	    @Override
	    public UpdatedShoppingList createFromParcel(Parcel source) {
	       return new UpdatedShoppingList(source);
	    }
	 
	   @Override
	   public UpdatedShoppingList[] newArray(int size) {
	      return new UpdatedShoppingList[0];
	   }
	};

	public UpdatedShoppingList(Parcel in) {
		super(in);
		setEmail(in.readString());
		setPassword(in.readString());
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeString(email);
		out.writeString(password);
	}
}
