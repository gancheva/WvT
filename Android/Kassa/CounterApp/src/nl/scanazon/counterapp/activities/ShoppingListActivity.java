package nl.scanazon.counterapp.activities;

import nl.scanazon.counterapp.R;
import nl.scanazon.counterapp.adapters.ProductListAdapter;
import nl.scanazon.counterapp.adapters.SeparatedListAdapter;
import nl.scanazon.counterapp.model.Product;
import nl.scanazon.counterapp.model.ProductList;
import nl.scanazon.counterapp.model.ShoppingList;
import nl.scanazon.counterapp.networking.ProductInfoRequest;
import nl.scanazon.counterapp.networking.RequestHelper;
import nl.scanazon.counterapp.networking.ShoppingListRequest;
import nl.scanazon.counterapp.scanner.ScanActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Handles a shoppinglist that shows the users chosen products
 */
public class ShoppingListActivity extends BaseActivity {

	private ShoppingList shoppingList;
	private ProductList unscannedProductList;
	private ProductList scannedProductList;
	private SeparatedListAdapter mergedAdapter;
	private ListView productListView;
	private String listId;
	
	private String lastRequestCacheKey;
	
	/**
	 * The result code for closing a view which can be set on return of an intent
	 */
	public final static int RESULT_CLOSING = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);
		productListView = (ListView) findViewById(R.id.productlist);
		
		listId = getIntent().getStringExtra("LISTID");
		
		performRequest(listId);
		
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Removes the received product object from the unscannedproductlist
	 * @param product
	 */
	private void removeScannedProduct(Product product) {
		for(int i = 0; i < unscannedProductList.size(); i++) {
			Product p = unscannedProductList.get(i);
			if(p.getProduct_id() == product.getProduct_id()) {
				int aantal = p.getAantal();
				if(p.getAantal() <= 1) {
					unscannedProductList.remove(p);
				}
				else {
					p.setAantal(aantal - 1);
				}
			}
		}
		if(unscannedProductList.size() <= 0) {
			checkScannedProducts();
		}
		else {
			addScannedProduct(product);
		}
	}
	
	/**
	 * Adds a scanned product object to the scanned productlist. 
	 * Will add a new entry if it doesn't exist in the productlist yet
	 * @param product
	 */
	private void addScannedProduct(Product product) {
		boolean productFound = false;
		for(int i = 0; i < scannedProductList.size(); i++) {
			Product p = scannedProductList.get(i);
			if(p.getProduct_id() == product.getProduct_id()) {
				int aantal = p.getAantal();
				p.setAantal(aantal + 1);
				productFound = true;
			}
		}
		
		if(!productFound) {
			Toast.makeText(getApplicationContext(), "Product niet op lijst", Toast.LENGTH_LONG).show();
		}
		
		updateProductList();
	}
	
	/**
	 * Builds and merges the adapters to show them in the listview
	 */
	private void updateProductList() {
		mergedAdapter = new SeparatedListAdapter(this);
		
		ProductListAdapter unscannedAdapter = new ProductListAdapter(this, R.layout.list_item, unscannedProductList);
		ProductListAdapter scannedAdapter = new ProductListAdapter(this, R.layout.list_item, scannedProductList);
		
		mergedAdapter.addSection("Openstaand", unscannedAdapter);
		mergedAdapter.addSection("Gescand", scannedAdapter);
		
		productListView.setAdapter(mergedAdapter);
		
		mergedAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Start the scanner activity
	 * @param view
	 */
	public void startScanner(View view) {
		Intent i = new Intent(this, ScanActivity.class);
		i.putExtra("PRODUCTSCAN", true);
		startActivityForResult(i, 1);
	}
	
	/**
	 * Start the payment by setting boolean nofinish to true to allow 
	 * @param view
	 */
	public void startPayment(View view) {
		checkScannedProducts();
	}
	
	/**
	 * Checks the scanned product list and starts the requist if it isnt zero.
	 * If noFinish is set a toast is presented. Else the view will Finish()
	 */
	private void checkScannedProducts() {
		if(unscannedProductList.size() != 0) {
			showUnscannedDialog();
			return;
		}
		
		Intent i = new Intent(this, InvoiceActivity.class);
		i.putExtra("LISTID", listId);
		startActivity(i);
	}
	
	/**
	 * Shows a dialog for the user when products are not scanned
	 */
	private void showUnscannedDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
		// set title
		alertDialogBuilder.setTitle("Ongescande producten");
 
		// set dialog message
		alertDialogBuilder.setMessage("Bevestig alstublieft uw producten of annuleer het proces.");
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Verder",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		  });
		alertDialogBuilder.setNegativeButton("Annuleren",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				Intent intent = new Intent(ShoppingListActivity.this, ScanActivity.class);
			    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
			    startActivity(intent);
			}
		});
 
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
 
		// show it
		alertDialog.show();
	}
	
	@Override
	public void onBackPressed() {
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CLOSING) {
        	finish();
		}
		if(requestCode == 1 && resultCode != Activity.RESULT_CANCELED) {
			int product_id = Integer.parseInt(data.getStringExtra("BARCODE").substring(7, 12));
			performRequest(product_id);
		}
		else {
			Toast.makeText(getApplicationContext(), "Geen product gescand", Toast.LENGTH_LONG).show();
		}

    }
	
	/**
	 * Performs a request with the given product id to get information about the product
	 * @param product_id
	 */
	private void performRequest(int product_id) {

		ProductInfoRequest request = new ProductInfoRequest(product_id);
		lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND, new ProductInfoRequestListener());
	}
	
	/**
	 * Receives the Product object with information about the requested product
	 */
	private class ProductInfoRequestListener implements RequestListener<Product> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
		}
	
		@Override
		public void onRequestSuccess(Product product) {
			if (product != null) {
				removeScannedProduct(product);				
			} else {
				Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/**
	 * Performs a request with the given shoppinglist to update a shoppinglist
	 * @param shoppingList
	 */
	private void performRequest(String listId) {

		ShoppingListRequest request = new ShoppingListRequest(listId);
		lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND, new ShoppingListRequestListener());
	}
	
	/**
	 * Receives if the list update was successful or not
	 */
	private class ShoppingListRequestListener implements RequestListener<ShoppingList> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
		}
	
		@Override
		public void onRequestSuccess(ShoppingList list) {
			if (list.getMessage() == null) {
				shoppingList = list;
				unscannedProductList = shoppingList.getProducten();
				scannedProductList = new ProductList();
				updateProductList();
				
				getActionBar().setTitle(shoppingList.getBoodschappennaam());
			} else {
				Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
			}
		}
	}
}
