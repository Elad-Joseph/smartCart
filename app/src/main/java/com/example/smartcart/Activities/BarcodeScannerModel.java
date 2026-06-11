package com.example.smartcart.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.example.smartcart.data.CallBack;
import com.example.smartcart.R;
import com.example.smartcart.data.ProductDatabase;
import com.example.smartcart.helpers.ImportedShoppingLists;
import com.example.smartcart.helpers.Product;
import com.example.smartcart.helpers.ShoppingList;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.Map;

public class BarcodeScannerModel extends BaseActivity {

    private ProductDatabase database;

    private Product scannedProduct;

    private Boolean allowScan;

    private ShoppingList currentShoppingList;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private ToneGenerator toneGen1;

    private String barcodeData;
    private ImageButton scanButton;
    private ImageButton backToListDisplay;
    private ImageButton addNewProduct;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        setupDoubleBackExit();

        SetupIds();
        SetupListeners();

        database = new ProductDatabase();

        allowScan = false;

        Intent intent = getIntent();
        ImportedShoppingLists importedShoppingLists = ImportedShoppingLists.getInstance();
        currentShoppingList = importedShoppingLists.getListById(intent.getStringExtra("CurrentListId"));

        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
        initialiseDetectorsAndSources();

    }

    @Override
    protected void SetupIds(){
        surfaceView = findViewById(R.id.camera);
        scanButton = findViewById(R.id.btn_scan);
        backToListDisplay = findViewById(R.id.barcodeScannerToListDisplay);
        addNewProduct = findViewById(R.id.addNewProductButton);

    }

    @Override
    protected void SetupListeners(){
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowScan = true;
                scanButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    // Action 1: Change the button color
                    scanButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

                    // Action 2: Update your boolean flag
                    allowScan = false;
                }, 3000);
            }
        });

        backToListDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , ListDisplayModel.class);
                intent.putExtra("CurrentListId" , currentShoppingList.getId());
                startActivity(intent);
                finish();
            }
        });

        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cameraSource != null) {
                    cameraSource.stop();
                }

                AddNewProductPopUp newProductPopUp = new AddNewProductPopUp();
                newProductPopUp.setOnPopupStopListener(() -> {

                    try {
                        if (ActivityCompat.checkSelfPermission(BarcodeScannerModel.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            cameraSource.start(surfaceView.getHolder());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                newProductPopUp.show(getSupportFragmentManager(), "new_product");
            }
        });
    }

    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            BarcodeScannerModel.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(
                                BarcodeScannerModel.this,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA_PERMISSION
                        );
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
                 Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeData = barcodes.valueAt(0).displayValue;
                    getProductFromDatabase(barcodeData);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                }
            }
        });
    }

    public void getProductFromDatabase(String id){
        if(!allowScan){
            return;
        }
        allowScan = false;
        scanButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        database.getProduct(id, new CallBack<Map<String, Object>>() {
            @Override
            public void onCallBack(Map<String, Object> value) {
                if(value != null){
                    scannedProduct = new Product(value.get("name").toString(), value.get("id").toString() , value.get("price").toString());
                    if(currentShoppingList.containsItem(id)){
                        scannedProduct = currentShoppingList.getItemById(id).getProduct();
                    }
                    ProductFoundPopup();

                } else {
                    scannedProduct = new Product("UNKNOWN" , id ,"NULL");
                    ProductFoundPopup();

                }
            }
        });
    }

    public void ProductFoundPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Found");

        View view = getLayoutInflater().inflate(R.layout.popup_foundproduct, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView productNameTextView = view.findViewById(R.id.ProductFoundNameDisplay);
        TextView productIdTextView = view.findViewById(R.id.ProductFoundIdDisplay);
        TextView productPriceTextView = view.findViewById(R.id.ProductFoundPriceDisplay);
        MaterialButton OkButton = view.findViewById(R.id.buttonOkFoundProduct);
        MaterialButton markButton = view.findViewById(R.id.markItemButton);
        if(currentShoppingList.containsItem(barcodeData)){
            markButton.setVisibility(View.VISIBLE);
        } else {
            markButton.setVisibility(View.GONE);
        }



        productNameTextView.setText(productNameTextView.getText().toString() + scannedProduct.getName());
        productIdTextView.setText(productIdTextView.getText().toString() + scannedProduct.getId());
        productPriceTextView.setText(productPriceTextView.getText().toString() + scannedProduct.getPrice());
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if(currentShoppingList.containsItem(scannedProduct.getId())){
            markButton.setVisibility(View.VISIBLE);
        }
        else{
            markButton.setVisibility(View.GONE);
        }
        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentShoppingList.markItem(scannedProduct.getId());
                dialog.dismiss();
            }
        });


    }




}
