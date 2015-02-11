package nl.scanazon.scanapp.activities;

import nl.scanazon.scanapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * Generates a QR code based on the received shoppinglist id and shows it in a webview
 */
public class QRActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent i = getIntent();
		loadImage(i.getStringExtra("SHOPPINGLISTID"));
	}
	
	/**
	 * Generates and shows a QR code in a webview based on the list ID
	 * @param listID
	 */
	private void loadImage(String listID) {
		String url = "http://chart.apis.google.com/chart?cht=qr&chs=300x300&chl="+listID;
		WebView web = (WebView) findViewById(R.id.qrwebview);
		web.loadUrl(url);
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
		getMenuInflater().inflate(R.menu.qr, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		//Return a result to state that the view has closed
		setResult(ShoppingListActivity.RESULT_CLOSING);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

}
