package com.example.smartcart.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.smartcart.R;
import com.example.smartcart.data.CallBack;
import com.example.smartcart.data.ProductDatabase;
import com.example.smartcart.helpers.Product;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

public class AddNewProductPopUp extends DialogFragment {
    private View PopupView;

    private ProductDatabase productDatabase;

    private Boolean AllowScan;
    private int ConfirmSubmit;

    private ImageButton DismissPopup;
    private EditText ProductIdEditText;
    private ImageButton IdScanningCameraVisibility;
    private LinearLayout BarcodeScannerLayout;
    private SurfaceView BarcodeScanner;
    private ImageButton AllowScanButton;
    private EditText ProductNameEditText;
    private EditText ProductPriceEditText;
    private MaterialButton SubmitProductButton;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;

    private String barcodeData;

    public interface OnPopupStopListener {
        void onPopupStopped();
    }

    private OnPopupStopListener stopListener;

    public void setOnPopupStopListener(OnPopupStopListener listener) {
        this.stopListener = listener;
    }

    @Override
    public void onDismiss(@NonNull android.content.DialogInterface dialog) {
        super.onDismiss(dialog);
        if (stopListener != null) {
            stopListener.onPopupStopped();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        PopupView  = inflater.inflate(R.layout.popup_add_new_product,null);

        builder.setView(PopupView);
        builder.setTitle("New Product");

        productDatabase = new ProductDatabase();

        AllowScan = false;
        ConfirmSubmit = 0;

        SetupIds();
        SetupListeners();

        return builder.create();
    }

    public void SetupIds(){
        DismissPopup = PopupView.findViewById(R.id.DismissNewProductPopUp);
        ProductIdEditText = PopupView.findViewById(R.id.NewProductIdEditText);
        IdScanningCameraVisibility = PopupView.findViewById(R.id.NewProductIdImageButton);
        BarcodeScannerLayout = PopupView.findViewById(R.id.NewProductScannerLinearLayout);
        BarcodeScanner = PopupView.findViewById(R.id.ScanNewProductIdCamera);
        AllowScanButton = PopupView.findViewById(R.id.AllowScanNewProductId);
        ProductNameEditText = PopupView.findViewById(R.id.NewProductName);
        ProductPriceEditText = PopupView.findViewById(R.id.NewProductPrice);
        SubmitProductButton = PopupView.findViewById(R.id.SubmitNewProduct);
    }

    public void SetupListeners(){
        DismissPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        IdScanningCameraVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BarcodeScannerLayout.getVisibility() == View.VISIBLE){

                    BarcodeScannerLayout.setVisibility(View.GONE);
                }
                else{
                    BarcodeScannerLayout.setVisibility(View.VISIBLE);
                    initialiseDetectorsAndSources();
                }
            }
        });

        AllowScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowScan = true;
                AllowScanButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    AllowScanButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));


                    AllowScan = false;
                }, 1000);
            }
        });

        SubmitProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productId = ProductIdEditText.getText().toString().trim();
                String productName = ProductNameEditText.getText().toString().trim();
                String productPrice = ProductPriceEditText.getText().toString().trim();

                if(productId != "" || productName != "" || productPrice !=""){
                    if(ConfirmSubmit == 0){
                        ConfirmSubmit = 1;
                        Toast.makeText(PopupView.getContext() , "press again to confirm" , Toast.LENGTH_SHORT).show();
                        new Handler(Looper.getMainLooper()).postDelayed(() ->{
                            ConfirmSubmit = 0;
                        } , 5000);
                    }
                    else{
                        productDatabase.IsExist(productId, new CallBack<Boolean>() {
                            @Override
                            public void onCallBack(Boolean value) {
                                if(value){
                                    Toast.makeText(PopupView.getContext() , "product already exists" , Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Product product = new Product(productName , productId , productPrice);
                                    productDatabase.addProduct(product);
                                    dismiss();
                                }
                            }
                        });

                    }

                }else {
                    Toast.makeText(PopupView.getContext() , "some values are empty" , Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void initialiseDetectorsAndSources() {


        barcodeDetector = new BarcodeDetector.Builder(PopupView.getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(PopupView.getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        BarcodeScanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            PopupView.getContext(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(BarcodeScanner.getHolder());
                    }

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


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Log.d("CameraStatus" , "To prevent memory leaks barcode scanner has been stopped");
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeData = barcodes.valueAt(0).displayValue;
                    if(AllowScan){
                        ProductIdEditText.setText(barcodeData);
                    }
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
        if (barcodeDetector != null) {
            barcodeDetector.release();
            barcodeDetector = null;
        }
    }
}


