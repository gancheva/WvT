package nl.scanazon.scanapp.activities;

import java.util.ArrayList;

import nl.scanazon.scanapp.R;
import nl.scanazon.scanapp.model.ShoppingList;
import nl.scanazon.scanapp.model.ShoppingLists;
import nl.scanazon.scanapp.networking.RequestHelper;
import nl.scanazon.scanapp.networking.ShoppingListsRequest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Handles a shopping list and shows these on the screen to be picked by the user
 */
public class ShoppingListPickerActivity extends BaseActivity {

	private String lastRequestCacheKey;
	private ShoppingLists shoppingLists;
	private ListView shoppingListView;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list_picker);
		shoppingListView = (ListView) findViewById(R.id.shoppinglist);
		
		SharedPreferences prefs = getSharedPreferences("nl.scanazon.scanapp", Context.MODE_PRIVATE);
		performRequest(prefs.getString("e-mail", ""), prefs.getString("password", ""));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_list_picker, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("nl.scanazon.scanapp", Context.MODE_PRIVATE);
		performRequest(prefs.getString("e-mail", ""), prefs.getString("password", ""));
	}
	
	/**
	 * Updates the listview
	 * @param lists
	 */
	private void updateListView(ShoppingLists lists) {
		this.shoppingLists = lists;
		
		ArrayList<String> listNames = new ArrayList<String>();
		for(int i = 0; i < lists.size(); i++) {
			listNames.add(lists.get(i).getBoodschappennaam());
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listNames);
		shoppingListView.setAdapter(adapter);
		shoppingListView.setOnItemClickListener(new ShoppingListsClickListener());
		
		adapter.notifyDataSetChanged();
	}
	
	/**
	 *  When a list is clicked open that shoppinglistactivity
	 */
	private class ShoppingListsClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ShoppingList list = shoppingLists.get(position);
			if(list.getProducten() != null) {
				Intent i = new Intent(ShoppingListPickerActivity.this , ShoppingListActivity.class);
				i.putExtra("nl.scanazon.scanapp.intent.productlist", list);
				startActivity(i);
			}
			else {
				Toast.makeText(getApplicationContext(), "De gekozen lijst bevat geen producten", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	/**
	 * Perform a request to get all shopping lists
	 * @param email
	 * @param password
	 */
	private void performRequest(String email, String password) {

		ShoppingListsRequest request = new ShoppingListsRequest(email, password);
		lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND, new ShoppingListsRequestListener());
	}
	
	/**
	 * Listens for a shoppinglist result and handles the shoppinglists object
	 */
	private class ShoppingListsRequestListener implements RequestListener<ShoppingLists> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
		}
	
		@Override
		public void onRequestSuccess(ShoppingLists lists) {
			if (lists.getMessage() == null) {
				updateListView(lists);
			} else {
				Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
			}
		}
	}

}
