package com.example.restapi;


import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageThree extends Fragment {

    Button facerecognition;
    Button landmarkrecognition;
    Button textrecognition;

    public PageThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragment_three = inflater.inflate(R.layout.fragment_page_three, container, false);
        facerecognition = (Button) fragment_three.findViewById(R.id.facerecognition);
        landmarkrecognition = (Button) fragment_three.findViewById(R.id.landmarkrecognition);
        textrecognition = (Button) fragment_three.findViewById(R.id.textrecognition);
        facerecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FaceRecognition.class);
                startActivityForResult(intent, 1111);
            }
        });
        landmarkrecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LandmarkRecognition.class);
                startActivityForResult(intent, 2222);
            }
        });
        textrecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LandmarkRecognition.class);
                startActivityForResult(intent, 2222);
            }
        });
        return fragment_three;
    }

}
