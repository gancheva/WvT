package nl.scanazon.counterapp.scanner;

import java.util.Random;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
/* Import ZBar Class files */
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import nl.scanazon.counterapp.R;
import nl.scanazon.counterapp.activities.InvoiceActivity;
import nl.scanazon.counterapp.activities.ShoppingListActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 *
 */
public class ScanActivity extends Activity
{
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    Button scanButton;

    ImageScanner scanner;

    private boolean barcodeScanned = true;
    private boolean previewing = false;
    private boolean productScanner = false;

    static {
        System.loadLibrary("iconv");
    } 

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        boolean hasScanExtra = getIntent().hasExtra("PRODUCTSCAN");
        if(hasScanExtra) {
        	productScanner = getIntent().getBooleanExtra("PRODUCTSCAN", false);
        }
        
        setContentView(R.layout.activity_scanner);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanButton = (Button)findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (barcodeScanned) {
                        barcodeScanned = false;
                        mCamera.setPreviewCallback(previewCb);
                        mCamera.startPreview();
                        previewing = true;
                        mCamera.autoFocus(autoFocusCB);
                    }
                }
            });
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
    
    private void processCode(String listId) {
    	Random rand = new Random();
    	int rescanNeeded = 1;//rand.nextInt(5) + 1;
    	
    	Intent intent;
    	if(rescanNeeded == 1) {
    		intent = new Intent(this, ShoppingListActivity.class);
    	}
    	else {
    		intent = new Intent(this, InvoiceActivity.class);
    	}
        intent.putExtra("LISTID", listId);
        startActivity(intent);
    }

    private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
        };

    PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);
                
                if (result != 0) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                    	if(productScanner) {
                    		barcodeScanned = true;
                            Intent intent=new Intent();
        			        intent.putExtra("BARCODE", sym.getData());
        			        
        			        setResult(1, intent);
        			        
        			        finish();
                    	}
                    	else {
                    		barcodeScanned = true;
                        	processCode(sym.getData());
                    	}
    			        
                    }
                }
            }
        };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };
}

