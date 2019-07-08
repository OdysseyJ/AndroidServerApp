package com.example.restapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class LandmarkRecognition extends AppCompatActivity {

    private static final int REQUEST_TAKE_ALBUM = 3333;
    Uri photoURI;
    Bitmap tempbitmap;

    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    Button btnDetect;
    AlertDialog waitingDialog;
    Button btnGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landmark_recognition);
        cameraView = (CameraView)findViewById(R.id.camera_view);
        graphicOverlay = (GraphicOverlay)findViewById(R.id.graphic_overlay);
        btnDetect = (Button)findViewById(R.id.btn_detect);
        btnGallery = (Button)findViewById(R.id.btn_gallery);

        waitingDialog = new SpotsDialog.Builder().setContext(this).setMessage("Please wait").setCancelable(false).build();

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_TAKE_ALBUM);
            }
        });

        btnDetect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();

                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
                recognizeLandmarksCloud(image);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        cameraView.stop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_TAKE_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    if(data.getData() != null){
                        try {
                            photoURI = data.getData();
                            try {
                                tempbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                tempbitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();

                                // Intent 생성해서 넘기기
                                Intent intent = new Intent(this,LandmarkRecognitionShow.class);
                                intent.putExtra("photo", byteArray);
                                startActivity(intent);

                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            // URI 받아왔으면 이거가지고 비트맵 뽑아서 바잍으로바꿔서 넘긴다
                            // 받아서 ~ 바잇->비트맵->firebasevisionimage 변경후
                            // recognizeLandmarks함 해주고 쭉 아래로 해주면 될거가튼데?
                            // 이후 리턴 버튼 누르면 finish();

                        }catch (Exception e){
                            Log.e("TAKE_ALBUM_SINGLE ERROR", e.toString());
                        }
                    }
                }
                break;
        }
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
        }
        waitingDialog.dismiss();
        Toast.makeText(getApplicationContext(),String.format("Detect %d landmark in image",count),Toast.LENGTH_SHORT).show();
    }
}