package com.example.detectionapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class Detection extends AppCompatActivity {

    private static final String TAG = "Detection";
    private ImageView rawImageView;
    private ImageView detectedImageView;
    private final int cameraRequest = 1888;

    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        handleActivityResult(data);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detection_activity);

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV initialization failed!");
        }

        rawImageView = findViewById(R.id.rawImage);
        detectedImageView = findViewById(R.id.detectedImage);

        Button photoButton = findViewById(R.id.openCamera);
        photoButton.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            someActivityResultLauncher.launch(cameraIntent);
        });
    }

    private void handleActivityResult(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        if (photo != null) {
            rawImageView.setImageBitmap(photo);
            Mat mat = new Mat();
            Utils.bitmapToMat(photo, mat);
            detectObjects(mat.getNativeObjAddr());
        }
    }

    private void detectObjects(long matAddress) {

    }
}
