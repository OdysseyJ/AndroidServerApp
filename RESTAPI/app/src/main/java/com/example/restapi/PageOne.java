package com.example.restapi;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageOne extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<PhoneBookInfo> phoneBook_array = new ArrayList<PhoneBookInfo>();
    private Button addButton1;

    RecyclerAdapter_PhoneBook adapter;

    public PageOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment_one = inflater.inflate(R.layout.fragment_page_one, container, false); // 뜻 ??
        mRecyclerView = fragment_one.findViewById(R.id.phoneBook_list);
        mLayoutManager = new LinearLayoutManager(getActivity()); // this??
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerAdapter_PhoneBook(R.layout.recyclerview_phonebook);
        mRecyclerView.setAdapter(adapter);

        addButton1 = (Button) fragment_one.findViewById(R.id.addbutton1);
        addButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.addbutton1:
                        Intent intent = new Intent(getActivity(), ContactDialog.class);
                        Log.v("태그", "ContactDialog 로 가는 intent 생성");
                        startActivityForResult(intent, 1111);
                        break;
                }
            }
        });

        Log.v("태그", "new JSONGetTask 시작");
        new JSONGetTask().execute("http://143.248.36.59:8080/api/contact/all");
        Log.v("태그", "new JSONGetTask 끝");
        // Inflate the layout for this fragment
        return fragment_one;
    }


    public class JSONGetTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            Log.v("태그", "JSONGetTask doInBackground 시작");
            try {
                Log.v("태그", "try문 1");
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
//                jsonObject.accumulate("user_id", "androidTest");
//                jsonObject.accumulate("name", "yun");
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try{
                    Log.v("태그", "try문 2");
                    URL url = new URL(urls[0]);//url을 가져온다.

                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("GET");
                    Log.v("태그", "Check Point 1");
                    con.connect();//연결 수행
                    Log.v("태그", "Check Point 2");
                    InputStream stream = con.getInputStream();
                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();
                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";
                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.

                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String… urls) 니까
                    return buffer.toString();
                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e){
                    Log.v("태그", "connect 에러");
                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.v("태그", "JSONGetTask doInBackground 끝");
            return null;
        }


        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            Log.v("태그", "JSONGetTask onPostExecute");
            super.onPostExecute(result);

            try {
                JSONTokener root = new JSONTokener(result);

                JSONArray temp = (JSONArray) root.nextValue();
                for (int i = 0; i < temp.length(); i++) {
                    System.out.println(temp.getJSONObject(i));
                    JSONObject jsonObject1 = temp.getJSONObject(i);
                    String value1 = jsonObject1.getString("name");
                    String value2 = jsonObject1.getString("phonenum");
                    String value3 = jsonObject1.getString("photo");
                    byte[] bytearr = Base64.decode(value3,0);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytearr, 0, bytearr.length);
                    PhoneBookInfo custom = new PhoneBookInfo(value1, value2, bitmap);
                    phoneBook_array.add(custom);
                    adapter.addItem(custom);
                    Log.v("태그", "초기 연락처 불러오기");
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1111:
                if (resultCode == Activity.RESULT_OK) {
                    String name = data.getStringExtra("name");
                    String phonenum = data.getStringExtra("phonenum");
                    String str_photo = data.getStringExtra("photo");
                    byte[] decodedByteArray = Base64.decode(str_photo, Base64.NO_WRAP);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                    PhoneBookInfo custom = new PhoneBookInfo(name, phonenum, decodedBitmap);
                    phoneBook_array.add(custom);
                    adapter.addItem(custom);
                    adapter.notifyDataSetChanged();
                    Log.v("태그", "해당 연락처 등록 완료");
                }
                break;


        }
    }
}