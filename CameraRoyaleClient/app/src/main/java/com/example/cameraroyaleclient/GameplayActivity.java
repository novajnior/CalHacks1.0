package com.example.cameraroyaleclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GameplayActivity extends AppCompatActivity {

    Bitmap tempbitmap;
    ServerCommunicator communicator;
    public static int killcount;
    TextView urdead;
    TextView watchyourback;
    TextView playersRemaining;
    Handler handler = new Handler();

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            Log.d("Handlers", "Called on main thread");
            update();
            // Repeat this the same runnable code block again another 2 seconds
            // 'this' is referencing the Runnable object
            handler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        communicator = new ServerCommunicator(this);
        killcount = 0;
        handler.post(runnableCode);

        urdead = findViewById(R.id.urdead);
        watchyourback = findViewById(R.id.watchyourback);

        urdead.setVisibility(View.INVISIBLE);
        watchyourback.setVisibility(View.INVISIBLE);

        playersRemaining = findViewById(R.id.remaining);
        playersRemaining.setText("");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableCode);
    }
    public void update() {
        communicator.getStatus(MainActivity.game, MainActivity.playerID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = ServerCommunicator.getJSON(response);
                boolean alive = true;
                boolean winner = false;
                int playersLeft = 0;
                String killfeed = "";
                try {
                    alive = json.getBoolean("alive");
                    playersLeft = json.getInt("playersLeft");
                    killfeed = json.getString("killfeed");
                    winner = json.getBoolean("winner");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!alive) {
                    urdead.setVisibility(View.VISIBLE);
                    watchyourback.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameplayActivity.this);
                    builder.setTitle("You died!")
                            .setMessage("Better luck next time!")
                            .setPositiveButton("oof", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                }
                if(killfeed.length() > 0) {
                    makeToast(killfeed);
                }
                if(winner) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameplayActivity.this);
                    builder.setTitle("You win!")
                            .setMessage("VICTORY ROYALE!")
                            .setPositiveButton("gg", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                }
                playersRemaining.setText("Players remaining: " + playersLeft);
            }
        });
    }
    //deprecated, ZXing broke
    public void openQR(View view) {
        Intent intent = new Intent(this, QrCodeScanner.class);
        startActivityForResult(intent, 69);
    }
    public void openCamera(View view) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent, 420);
        }
    }
    public void makeToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // I copypasted this from another project AFTER setting the QR req code
        // and just realized thatwow I'm predictable with my request codes
        if (requestCode == 69) {
            if (resultCode == RESULT_OK) {
                //deprecated, ZXing doesn't work
                String target = data.getStringExtra("QRResult");
                Log.d("YEET", target);
                makeToast("YOU TARGETED: " + target);
            }
        } else if (requestCode == 420) {
            //for firebase
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                tempbitmap = (Bitmap) extras.get("data");
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(tempbitmap);
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(tempbitmap);
                scanBarcodes(image);
            }

        }
    }

    /**
     * SOURCE: https://firebase.google.com/docs/ml-kit/android/read-barcodes
     * WOOOOOOOOOOOOOOOOOOOOOOOO FIREBASE TO SCAN QR CODES LET'S GO
     * @param image
     */
    private void scanBarcodes(FirebaseVisionImage image) {
        // [START set_detector_options]
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_QR_CODE)
                        .build();
        // [END set_detector_options]

        // [START get_detector]
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector();
        // Or, to specify the formats to recognize:
        // FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
        //        .getVisionBarcodeDetector(options);
        // [END get_detector]

        // [START run_detector]
        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                        // Task completed successfully
                        // [START_EXCLUDE]
                        // [START get_barcodes]
                        if(barcodes.size() == 0) {
                            makeToast("NO TARGETS DETECTED.");
                        }
                        for (FirebaseVisionBarcode barcode: barcodes) {
                            //Rect bounds = barcode.getBoundingBox();
                            //Point[] corners = barcode.getCornerPoints();

                            String rawValue = barcode.getRawValue();
                            makeToast("TARGET: " + rawValue);
                            communicator.killAttempt(MainActivity.game, MainActivity.playerID, rawValue, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject json = ServerCommunicator.getJSON(response);
                                    boolean success = false;
                                    try {
                                        success = json.getBoolean("success");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (success) {
                                        makeToast("KILL CONFIRMED.");
                                        killcount++;
                                    }
                                }
                            });

                            /*
                            int valueType = barcode.getValueType();
                            // See API reference for complete list of supported types
                            switch (valueType) {
                                case FirebaseVisionBarcode.TYPE_WIFI:
                                    String ssid = barcode.getWifi().getSsid();
                                    String password = barcode.getWifi().getPassword();
                                    int type = barcode.getWifi().getEncryptionType();
                                    break;
                                case FirebaseVisionBarcode.TYPE_URL:
                                    String title = barcode.getUrl().getTitle();
                                    String url = barcode.getUrl().getUrl();
                                    break;
                            }
                            */
                        }
                        // [END get_barcodes]
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
        // [END run_detector]
    }

}
