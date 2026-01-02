package com.example.smartcart.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartcart.data.CallBack;
import com.example.smartcart.data.DbUsersHandler;
import com.example.smartcart.data.FireStoreCallBack;
import com.example.smartcart.R;
import com.example.smartcart.data.ProductDatabase;
import com.example.smartcart.modle.ImportedShoppingLists;
import com.example.smartcart.modle.ShoppingList;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BarcodeScannerModel extends AppCompatActivity {

    ProductDatabase database;

    String productName;

    SharedPreferences sharedPreferences;

    private Boolean allowScan;

    private ShoppingList currentShoppingList;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    private ImageButton scanButton;

    private long scanStartTime = 0L;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        setupId();
        setupListeners();

        database = new ProductDatabase();

        allowScan = false;

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String email = sharedPreferences.getString("email", null);
        int numberOfLists = sharedPreferences.getInt("number list" , 0);

        Intent intent = getIntent();
        ImportedShoppingLists importedShoppingLists = ImportedShoppingLists.getInstance();
        currentShoppingList = importedShoppingLists.getListById(intent.getStringExtra("CurrentListId"));


        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
        initialiseDetectorsAndSources();

    }

    public void setupId(){
        surfaceView = findViewById(R.id.camera);
        scanButton = findViewById(R.id.btn_scan);
    }

    public void setupListeners(){
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowScan = true;
                scanStartTime = SystemClock.elapsedRealtime();
                scanButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            }
        });
    }

    private void initialiseDetectorsAndSources() {

//        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
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
                if(SystemClock.elapsedRealtime() - scanStartTime > 4000L){
                    scanButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                    allowScan = false;
                }
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
                    productName = value.get("name").toString();
                    if(currentShoppingList.containsItem(id)){
                        currentShoppingList.markItem(id);
                    }
                    ProductFoundPopup(productName);

                } else {
                    productName = "Product not found";
//
                }
            }
        });
    }

    public void ProductFoundPopup(String productName){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Found");

        View view = getLayoutInflater().inflate(R.layout.popup_foundproduct, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        TextView productNameTextView = view.findViewById(R.id.ProductFoundNameDisplay);
        MaterialButton OkButton = view.findViewById(R.id.buttonOkFoundProduct);
        MaterialButton markButton = view.findViewById(R.id.markItemButton);
        if(currentShoppingList.containsItem(barcodeData)){
            markButton.setVisibility(View.VISIBLE);
        } else {
            markButton.setVisibility(View.GONE);
        }


        productNameTextView.setText(productNameTextView.getText().toString() + productName);

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentShoppingList.markItem(barcodeData);
                dialog.dismiss();
            }
        });


    }


}
