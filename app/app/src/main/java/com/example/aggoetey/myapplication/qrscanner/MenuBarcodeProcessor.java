package com.example.aggoetey.myapplication.qrscanner;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.example.aggoetey.myapplication.R;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by sitt on 01/05/18.
 */

public class MenuBarcodeProcessor implements Detector.Processor<Barcode> {

    private Activity activity;
    private boolean snackIsShown;
    private Snackbar snackbar ;

    public MenuBarcodeProcessor(Activity context) {
        if (context == null) {
            throw new IllegalArgumentException("Given context is null");
        }

        this.activity = context;
        this.setSnackBar();
    }

    private void setSnackBar(){
        this.snackbar =  Snackbar.make(activity.findViewById(R.id.qr_constraint_container),"",Snackbar.LENGTH_INDEFINITE);
        this.snackbar.setAction(R.string.dismiss, v -> snackbar.dismiss());
        this.snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onShown(Snackbar sb) {
                snackIsShown = true;
                super.onShown(sb);
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                snackIsShown = false;
                super.onDismissed(transientBottomBar, event);
            }
        });
    }


    private void showSnackBar (String text){
        if(!snackIsShown){
            snackbar.setText(text).show();
        }
    }


    /**
     * This method will be called each time google vision api detects a code
     * the scanner is set to detect.
     *
     * The method is called from a thread separate from the ui-thread.
     *
     * @param detections detected codes from the scanner.
     */

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        SparseArray<Barcode> array = detections.getDetectedItems();
        for (int i = 0; i < array.size(); i++) {
            Barcode code = array.valueAt(i);

            switch (code.valueFormat) {
                case Barcode.URL:
                    final String message = String.format("Url: %1$s ,  Title: %1$s ", code.url.url, code.url.title);
                    Log.i("QRScanner", message);
                    onScannedBarcode(code);
                    activity.runOnUiThread(() -> showSnackBar(message));
                    break;
                default:
                    Log.i("QRScanner", code.rawValue);
                    activity.runOnUiThread(() -> showSnackBar("Invalid qr code scanned: " + code.rawValue));
                    break;
            }
        }
    }

    //TODO implement to restart main activity with menu of the restaurant loaded.
    public void onRestaurantCode(String code){

    }

    //TODO implement the function to get restaurant data from firebase and react on it
    public void onScannedBarcode (Barcode barcode) {

    }


    @Override
    public void release() {

    }

}
