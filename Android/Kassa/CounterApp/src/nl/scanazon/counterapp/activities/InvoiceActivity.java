package nl.scanazon.counterapp.activities;

import java.text.DecimalFormat;

import nl.scanazon.counterapp.R;
import nl.scanazon.counterapp.adapters.InvoiceListAdapter;
import nl.scanazon.counterapp.model.Invoice;
import nl.scanazon.counterapp.model.Message;
import nl.scanazon.counterapp.networking.DeleteListRequest;
import nl.scanazon.counterapp.networking.InvoiceRequest;
import nl.scanazon.counterapp.networking.RequestHelper;
import nl.scanazon.counterapp.scanner.ScanActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Handles an invoicelist and allows the user to pay for their products
 */
public class InvoiceActivity extends BaseActivity {

	private Invoice invoice;
	private String listid;
	private String lastRequestCacheKey;
	private ListView productListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoice);
		
		productListView = (ListView) findViewById(R.id.productlist_invoice);
		
		listid = getIntent().getStringExtra("LISTID");
		performRequest(listid);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invoice, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, ScanActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
	    startActivity(intent);
	}
	
	public void finishPayment(View view) {
		performDeleteRequest(listid);
	}
	
	/**
	 * Shows a dialog confirming that the payment has finished
	 */
	private void showFinishedDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
		// set title
		alertDialogBuilder.setTitle("Succes");
 
		// set dialog message
		alertDialogBuilder.setMessage("Uw betaling is afgerond. Een fijne dag verder!");
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				onBackPressed();
			}
		  });
 
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
 
		// show it
		alertDialog.show();
	}

	/**
	 * Performs a request for an invoice
	 * @param listId
	 */
	private void performRequest(String listId) {

		InvoiceRequest request = new InvoiceRequest(listId);
		lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND, new InvoiceRequestListener());
	}
	
	/**
	 * Receives an invoice object and handles it to show it in the invoiceview
	 */
	private class InvoiceRequestListener implements RequestListener<Invoice> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
		}
	
		@Override
		public void onRequestSuccess(Invoice in) {
			if (in.getMessage() == null) {
				invoice = in;
				InvoiceListAdapter invoiceAdapter = new InvoiceListAdapter(InvoiceActivity.this, R.layout.list_item_invoice, invoice.getProducten());
				productListView.setAdapter(invoiceAdapter);
				invoiceAdapter.notifyDataSetChanged();
				TextView textView = (TextView) findViewById(R.id.textiew_total);
				DecimalFormat form = new DecimalFormat("0.00");
				textView.setText("Totaal:  € " + form.format(invoice.getTotaal()));
			} else {
				Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/**
	 * Starts a delete request
	 * @param listId
	 */
	private void performDeleteRequest(String listId) {

		DeleteListRequest request = new DeleteListRequest(listId);
		lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND, new DeleteListRequestListener());
	}
	
	/**
	 * Receives a message and check if the result was accepted
	 */
	private class DeleteListRequestListener implements RequestListener<Message> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
		}
	
		@Override
		public void onRequestSuccess(Message status) {
			if (status.getMessage() != null) {
				String response = status.getMessage();
				if(response.equals(RequestHelper.ACCEPTED)) {
					showFinishedDialog();
				}
				else {
					Toast.makeText(getApplicationContext(), "Kon betaling niet afronden", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), RequestHelper.NETWORKERROR, Toast.LENGTH_LONG).show();
			}
		}
	}
}
