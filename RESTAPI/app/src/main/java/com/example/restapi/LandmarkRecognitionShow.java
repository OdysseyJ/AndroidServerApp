package com.example.restapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmarkDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.common.FirebaseVisionLatLng;

import java.util.ArrayList;
import java.util.List;

public class LandmarkRecognitionShow extends AppCompatActivity {

    GraphicOverlay graphicOverlay;
    ImageView imageView;
    TextView landMarksTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_recognition_show);

        imageView = (ImageView) findViewById(R.id.landmarkimage);
        graphicOverlay = (GraphicOverlay)findViewById(R.id.graphic_overlay);
        landMarksTextView = (TextView)findViewById(R.id.landmarkstextview);

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("photo");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        recognizeLandmarksCloud(image);
    }

    private void recognizeLandmarksCloud(FirebaseVisionImage image) {
        // [START set_detector_options_cloud]
        FirebaseVisionCloudDetectorOptions options = new FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(15)
                .build();
        // [END set_detector_options_cloud]

        // [START get_detector_cloud]
        FirebaseVisionCloudLandmarkDetector detector = FirebaseVision.getInstance()
                .getVisionCloudLandmarkDetector(options);
        // Or, to change the default settings:
        // FirebaseVisionCloudLandmarkDetector detector = FirebaseVision.getInstance()
        //         .getVisionCloudLandmarkDetector(options);
        // [END get_detector_cloud]
        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setRotation(FirebaseVisionImageMetadata.ROTATION_0)
                .build();

        // [START run_detector_cloud]
        Task<List<FirebaseVisionCloudLandmark>> result = detector.detectInImage(image)
                .addOnSuccessListener(this,
                        new OnSuccessListener<List<FirebaseVisionCloudLandmark>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionCloudLandmark> firebaseVisionCloudLandmarks) {
                                // Task completed successfully
                                // [START_EXCLUDE]
                                // [START get_landmarks_cloud]
                                for (FirebaseVisionCloudLandmark landmark: firebaseVisionCloudLandmarks) {

                                    Rect bounds = landmark.getBoundingBox();
                                    String landmarkName = landmark.getLandmark();
                                    String entityId = landmark.getEntityId();
                                    float confidence = landmark.getConfidence();

                                    // Multiple locations are possible, e.g., the location of the depicted
                                    // landmark and the location the picture was taken.
                                    for (FirebaseVisionLatLng loc: landmark.getLocations()) {
                                        double latitude = loc.getLatitude();
                                        double longitude = loc.getLongitude();
                                    }
                                }
                                // [END get_landmarks_cloud]
                                // [END_EXCLUDE]
                                processLandmarkResult(firebaseVisionCloudLandmarks);
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        // [END run_detector_cloud]
    }

    private void processLandmarkResult(List<FirebaseVisionCloudLandmark> firebaseVisionCloudLandmarks) {
        int count = 0;
        ArrayList<String> landmarks = new ArrayList<String>();
        for(FirebaseVisionCloudLandmark landmark : firebaseVisionCloudLandmarks){

            graphicOverlay.clear();
            //Draw rectangle
            CloudLandmarkGraphic cloudLandmarkGraphic = new CloudLandmarkGraphic(graphicOverlay);
            graphicOverlay.add(cloudLandmarkGraphic);
            cloudLandmarkGraphic.updateLandmark(landmark);

//            Rect bounds = landmark.getBoundingBox();
//            RectOverlay rect = new RectOverlay(graphicOverlay,bounds);
////            TextGraphic textGraphic = new TextGraphic(graphicOverlay, name);
////            graphicOverlay.add(textGraphic);
//            graphicOverlay.add(rect);
            count++;
            String landmarkName = landmark.getLandmark();
            landmarks.add(landmarkName);
        }
        String finalString = "찾은 랜드마크";
        for (int i = 0; i < landmarks.size(); i++){
            finalString = finalString + Integer.toString(i) + " : " + landmarks.get(i) + "                                                                                                                                                                   ";
        }
        landMarksTextView.setText(finalString);
        Toast.makeText(getApplicationContext(),String.format("Detect %d landmark in image",count),Toast.LENGTH_SHORT).show();
    }
}
