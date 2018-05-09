package com.example.aggoetey.myapplication.qrscanner.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.qrscanner.MenuBarcodeProcessor;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRScannerActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CAMERA = 50;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        SurfaceView cameraView = (SurfaceView) findViewById(R.id.qr_surface_view);
        EditText codeTxt = findViewById(R.id.qr_code_edit);
        AppCompatButton confirmBtn = findViewById(R.id.qr_code_edit_btn);
        MenuBarcodeProcessor processor = new MenuBarcodeProcessor(this, codeTxt);

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
                    Toast.makeText(this, "Exiting QR Scanner",  Toast.LENGTH_SHORT).show();
                    AsyncTask.execute(() -> cameraSource.release());
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean haveCameraPermission(){
        return ActivityCompat.checkSelfPermission(QRScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void startCameraSource(final CameraSource cameraSource, final SurfaceView cameraView){
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    AsyncTask.execute(() ->{
                        try {
                            cameraSource.start(cameraView.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } );
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
    private void setUpCameraView(final CameraSource cameraSource, final SurfaceView cameraView) {
        if(this.haveCameraPermission()){
            this.startCameraSource(cameraSource, cameraView);
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CAMERA){
            if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                this.recreate();
            }else{

                findViewById(R.id.qr_scanner_title).setVisibility(View.INVISIBLE);
                findViewById(R.id.qr_scanner_permission_title).setVisibility(View.VISIBLE);

            }
        }
    }
}
