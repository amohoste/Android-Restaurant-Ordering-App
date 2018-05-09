package com.example.aggoetey.myapplication.qrscanner;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.SparseArray;
import android.widget.EditText;

import com.example.aggoetey.myapplication.R;
import com.example.aggoetey.myapplication.qrscanner.activity.QRScannerActivity;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

import java.security.InvalidParameterException;
import java.util.IllegalFormatException;

/**
 * Created by sitt on 01/05/18.
 */

public class MenuBarcodeProcessor implements Detector.Processor<Barcode> {

    private Activity activity;
    private boolean snackIsShown;
    private EditText editText;
    private Snackbar snackbar;

    public MenuBarcodeProcessor(Activity context, EditText textField) {
        if (context == null) {
            throw new IllegalArgumentException("Given context is null");
        }

        this.activity = context;
        this.editText = textField;
        this.setSnackBar();
    }

    private void setSnackBar() {
        this.snackbar = Snackbar.make(activity.findViewById(R.id.qr_constraint_container), "", Snackbar.LENGTH_INDEFINITE);
        this.snackbar.setAction(R.string.dismiss, v -> snackbar.dismiss());
        this.snackbar.addCallback(new Snackbar.Callback() {
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


    private void showSnackBar(String text) {
        if (!snackIsShown) {
            snackbar.setText(text).show();
        }
    }


    /**
     * This method will be called each time google vision api detects a code
     * the scanner is set to detect.
     * <p>
     * The method is called from a thread separate from the ui-thread.
     *
     * @param detections detected codes from the scanner.
     */

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        SparseArray<Barcode> array = detections.getDetectedItems();
        if(array.size() > 0 ) {
            Barcode code = array.valueAt(0);
            Log.i("QRScanner", code.rawValue);

            switch (code.valueFormat) {
                case Barcode.URL:
                    onScannedBarcode(code);
                    break;
                default:
                    break;
            }


            this.editText.post(() -> this.editText.setText(code.rawValue));
            activity.runOnUiThread(() -> onScannedBarcode(code));

        }
    }

    //TODO implement the function to do something when the given code is invalid
    public void onInvalidCode (Barcode code) {

    }


    /**
     * @param code
     */
    public void onScannedCode(String code) {
        //TODO: check if valid
        Intent data = new Intent();
        data.putExtra(QRScannerActivity.EXTRA_ANSWER_SHOWN, code);
        activity.setResult(Activity.RESULT_OK, data);
    }

    public void onScannedBarcode(Barcode barcode) {
        onScannedCode(barcode.toString());
    }


    @Override
    public void release() {

    }

}
