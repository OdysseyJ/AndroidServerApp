package com.example.restapi;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import com.wonderkiln.camerakit.FrameProcessingRunnable;

import java.util.List;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class FaceRecognition extends AppCompatActivity {

    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    Button btnDetect;
    AlertDialog waitingDialog;

    public FaceRecognition() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_face_recognition);
        cameraView = (CameraView)findViewById(R.id.camera_view);
        graphicOverlay = (GraphicOverlay)findViewById(R.id.graphic_overlay);
        btnDetect = (Button)findViewById(R.id.btn_detect);
        waitingDialog = new SpotsDialog.Builder().setContext(this).setMessage("Please wait").setCancelable(false).build();

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

                runFaceDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    private void runFaceDetector(Bitmap bitmap){
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder().build();

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                processFaceResult(firebaseVisionFaces);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processFaceResult(List<FirebaseVisionFace> firebaseVisionFaces) {
        int count = 0;
        for(FirebaseVisionFace face : firebaseVisionFaces){
//            Rect bounds = face.getBoundingBox();
//            //Draw rectangle
//            RectOverlay rect = new RectOverlay(graphicOverlay,bounds);
//            graphicOverlay.add(rect);

          FirebaseVisionFace temp = face;
      FaceGraphic faceGraphic = new FaceGraphic(graphicOverlay);
      graphicOverlay.add(faceGraphic);
     // faceGraphic.updateFace(temp, frameMetadata.getCameraFacing());
            faceGraphic.updateFace(temp, 50);

            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
            // nose available):
            FirebaseVisionPoint leftEyePos = null;
            FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
            if (leftEye != null) {
                leftEyePos = leftEye.getPosition();
            }
            float smileProb = 0;
            // If classification was enabled:
            if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                smileProb = face.getSmilingProbability();
            }
            if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                float rightEyeOpenProb = face.getRightEyeOpenProbability();
            }


            // If face tracking was enabled:
            if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID) {
                int id = face.getTrackingId();
            }


            count++;
        }
        waitingDialog.dismiss();
        Toast.makeText(getApplicationContext(),String.format("Detect %d faces in image",count),Toast.LENGTH_SHORT).show();
    }

}
