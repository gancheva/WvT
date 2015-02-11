package nl.scanazon.scanapp.activities;

import nl.scanazon.scanapp.R;
import nl.scanazon.scanapp.adapters.ProductListAdapter;
import nl.scanazon.scanapp.adapters.SeparatedListAdapter;
import nl.scanazon.scanapp.model.Message;
import nl.scanazon.scanapp.model.Product;
import nl.scanazon.scanapp.model.ProductList;
import nl.scanazon.scanapp.model.ShoppingList;
import nl.scanazon.scanapp.networking.ProductInfoRequest;
import nl.scanazon.scanapp.networking.RequestHelper;
import nl.scanazon.scanapp.networking.UpdateShoppingListRequest;
import nl.scanazon.scanapp.scanner.ScanActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	
	private String lastRequestCacheKey;
	private boolean noFinish = false;
	
	/**
	 * The result code for closing a view which can be set on return of an intent
	 */
	public final static int RESULT_CLOSING = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);
		productListView = (ListView) findViewById(R.id.productlist);
		
		Intent intent = getIntent();
		shoppingList = intent.getParcelableExtra("nl.scanazon.scanapp.intent.productlist");
		unscannedProductList = shoppingList.getProducten();
		scannedProductList = new ProductList();
		
		setupActionBar();
		updateProductList();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setTitle(shoppingList.getBoodschappennaam());
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
		addScannedProduct(product);
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
			product.setAantal(1);
			scannedProductList.add(product);
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
		startActivityForResult(i, 1);
	}
	
	/**
	 * Start the payment by setting boolean nofinish to true to allow 
	 * @param view
	 */
	public void startPayment(View view) {
		noFinish = true;
		checkScannedProducts();
	}
	
	@Override
	public void onBackPressed() {
		checkScannedProducts();
	}
	
	/**
	 * Checks the scanned product list and starts the requist if it isnt zero.
	 * If noFinish is set a toast is presented. Else the view will Finish()
	 */
	private void checkScannedProducts() {
		if(scannedProductList.size() > 0) {
			performRequest(shoppingList);
		}
		else {
			if(noFinish) {
				Toast.makeText(getApplicationContext(), "Geen producten om af te rekenen", Toast.LENGTH_LONG).show();
			}
			else {
				finish();
			}
		}
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
	 * Performs a request with the given shoppinglist to update a shoppinglist
	 * @param shoppingList
	 */
	private void performRequest(ShoppingList shoppingList) {
		SharedPreferences prefs = getSharedPreferences("nl.scanazon.scanapp", Context.MODE_PRIVATE);
		String email = prefs.getString("e-mail", "");
		String password = prefs.getString("password", "");
		
		UpdateShoppingListRequest request = new UpdateShoppingListRequest(shoppingList, email, password);
		lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND, new UpdateShoppingListRequestListener());
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
	 * Receives if the list update was successful or not
	 */
	private class UpdateShoppingListRequestListener implements RequestListener<Message> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
		}
	
		@Override
		public void onRequestSuccess(Message status) {
			if (status.getMessage() != null) {
				String response = status.getMessage();
				if(response.equals(RequestHelper.ACCEPTED)) {
					Toast.makeText(getApplicationContext(), RequestHelper.LISTSAVED, Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(getApplicationContext(), RequestHelper.LISTERROR, Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
			}
			
			if(noFinish) {
				noFinish = false;
				Intent i = new Intent(ShoppingListActivity.this, QRActivity.class);
				i.putExtra("SHOPPINGLISTID", Integer.toString(shoppingList.getBoodschappenlijst_id()));
				startActivityForResult(i, 2);
			}
			else {
				finish();
			}
		}
	}

}
