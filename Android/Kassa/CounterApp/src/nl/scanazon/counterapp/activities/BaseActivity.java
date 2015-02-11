package nl.scanazon.counterapp.activities;

import nl.scanazon.counterapp.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * Base class for all activities to present them with a base spiceManager to use in network requests 
 */
public abstract class BaseActivity extends ActionBarActivity {
	
	protected SpiceManager spiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
	  super.onStart();
	  spiceManager.start(this);
	}

	@Override
	protected void onStop() {
	  spiceManager.shouldStop();
	  super.onStop();
	}

}
