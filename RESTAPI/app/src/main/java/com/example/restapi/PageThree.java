package com.example.restapi;


import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageThree extends Fragment {

    ImageButton facerecognition;
    ImageButton landmarkrecognition;
    ImageButton textrecognition;
    ImageButton onetofiftybutton;
    ImageButton moleholebutton;
    ImageButton minebutton;
    ImageButton touchbutton;

    public PageThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragment_three = inflater.inflate(R.layout.fragment_page_three, container, false);
        facerecognition = (ImageButton) fragment_three.findViewById(R.id.facerecognition);
        landmarkrecognition = (ImageButton) fragment_three.findViewById(R.id.landmarkrecognition);
        textrecognition = (ImageButton) fragment_three.findViewById(R.id.textrecognition);
        onetofiftybutton = (ImageButton) fragment_three.findViewById(R.id.onetofifty);
        moleholebutton = (ImageButton) fragment_three.findViewById(R.id.molehole);
        minebutton = (ImageButton) fragment_three.findViewById(R.id.mine);
        touchbutton = (ImageButton) fragment_three.findViewById(R.id.touch);

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
                Intent intent = new Intent(getActivity(), TextRecognition.class);
                startActivityForResult(intent, 3333);
            }
        });
        onetofiftybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OneToFiftyActivity.class);
                startActivityForResult(intent, 4444);
            }
        });
        moleholebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoleHoleActivity.class);
                startActivityForResult(intent, 5555);
            }
        });
        minebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MineActivity.class);
                startActivityForResult(intent, 6666);
            }
        });
        touchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TouchGameActivity.class);
                startActivityForResult(intent, 7777);
            }
        });

        return fragment_three;
    }

}
