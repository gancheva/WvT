package nl.scanazon.scanapp.adapters;

import java.util.List;

import nl.scanazon.scanapp.R;
import nl.scanazon.scanapp.model.Product;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProductListAdapter extends ArrayAdapter<Product> {

	Context mContext;
	int layoutResourceId;
    List<Product> productList = null;

    public ProductListAdapter(Context mContext, int layoutResourceId, List<Product> productList) {
    	super(mContext, layoutResourceId, productList);  	 
    	this.layoutResourceId = layoutResourceId;
    	this.mContext = mContext;
    	this.productList = productList;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      LayoutInflater inflater = (LayoutInflater) mContext
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View rowView = inflater.inflate(R.layout.list_item, parent, false);
      TextView titleView = (TextView) rowView.findViewById(R.id.list_title);
      titleView.setText(productList.get(position).getNaam());
      TextView amountView = (TextView) rowView.findViewById(R.id.list_amount);
      amountView.setText(Integer.toString(productList.get(position).getAantal()));

      return rowView;
    }

}
