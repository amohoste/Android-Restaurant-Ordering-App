package com.example.aggoetey.myapplication.qrscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.aggoetey.myapplication.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRScannerActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        SurfaceView cameraView = (SurfaceView) findViewById(R.id.qr_surface_view);
        int height = getResources().getDimensionPixelSize(R.dimen.qr_scanner_height);
        int width = getResources().getDimensionPixelSize(R.dimen.qr_scanner_width);
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(height, width)
                .build();
        barcodeDetector.setProcessor(new MenuBarcodeProcessor(this));
        this.setUpCameraView(cameraSource, cameraView);
    }


    private void setUpCameraView(final CameraSource cameraSource, final SurfaceView cameraView) {
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (SecurityException e) {
                    Log.e("QRScanner", "Camera permission wasn't granted");
                    e.printStackTrace();
                } catch (IOException e) {
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
