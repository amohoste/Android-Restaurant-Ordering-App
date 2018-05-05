package com.example.aggoetey.myapplication.qrscanner.activity;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.qrscanner.MenuBarcodeProcessor;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRScannerActivity extends AppCompatActivity {


    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        SurfaceView cameraView = (SurfaceView) findViewById(R.id.qr_surface_view);
        EditText codeTxt = findViewById(R.id.qr_code_edit);
        AppCompatButton confirmBtn = findViewById(R.id.qr_code_edit_btn);
        MenuBarcodeProcessor processor = new MenuBarcodeProcessor(this);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();
        barcodeDetector.setProcessor(processor);
        this.setUpCameraView(cameraSource, cameraView);
        this.setupManualCodeInput(codeTxt, confirmBtn, processor);
    }


    private void setupManualCodeInput (EditText editText,  AppCompatButton confirmBtn, MenuBarcodeProcessor processor){
        confirmBtn.setOnClickListener((view) -> {

            processor.onRestaurantCode(editText.getText().toString());
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Respond to the action bar's Up/Home button
                if (cameraSource != null) {
                    cameraSource.release();
                    Toast.makeText(this, "Exiting QR Scanner",  Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpCameraView(final CameraSource cameraSource, final SurfaceView cameraView) {
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                cameraSource.start(cameraView.getHolder());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (SecurityException e) {
                    Log.e("QRScanner", "Camera permission wasn't granted");
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

}
