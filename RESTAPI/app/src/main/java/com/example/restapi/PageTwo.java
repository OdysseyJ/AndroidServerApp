package com.example.restapi;


import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageTwo extends Fragment {


    public PageTwo() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    GridLayoutManager layoutManager;

    ArrayList<Item> list = new ArrayList<Item>() {{
        add(new Item("image1",R.drawable.image1));
        add(new Item("image2",R.drawable.image2));
        add(new Item("image3",R.drawable.image3));
        add(new Item("image4",R.drawable.image4));
        add(new Item("image5",R.drawable.image5));
        add(new Item("image6",R.drawable.image6));
        add(new Item("image7",R.drawable.image7));
        add(new Item("image8",R.drawable.image8));
        add(new Item("image9",R.drawable.image9));
        add(new Item("image10",R.drawable.image10));
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragment_two = inflater.inflate(R.layout.fragment_page_two, container, false);

        RecyclerView recyclerView = (RecyclerView) fragment_two.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);
        recyclerView.setAdapter(adapter) ;

        //new JSONPostTask().execute("http://143.248.36.59:8080/api/gallery/add");
        new JSONGetTask().execute("http://143.248.36.59:8080/api/gallery/all");

        return fragment_two;
    }
    //new JSONTask().execute("http://143.248.36.59:8080/api/gallery/all");로 쓴다.
    public class JSONGetTask extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("user_id", "androidTest");

                jsonObject.accumulate("name", "yun");

                HttpURLConnection con = null;

                BufferedReader reader = null;

                try{

                    URL url = new URL(urls[0]);//url을 가져온다.

                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("GET");

                    con.connect();//연결 수행

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

            return null;

        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            System.out.println(result);

        }

    }

    //new JSONTask().execute("http://143.248.36.59:8080/api/gallery/add");로 쓴다. (바디필요)
    public class JSONPostTask extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {

            try {
                HttpURLConnection con = null;

                BufferedReader reader = null;

                try{

                   // URL url = new URL("http://192.168.25.16:8080/api/gallery/add");

                    URL url = new URL(urls[0]);//url을 가져온다.

                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");

                    con.setDoOutput(true);

                    con.setDoInput(true);

                    con.setDefaultUseCaches(false);

                    con.setRequestProperty("Content-Type","application/json");

                    con.connect();//연결 수행

                    // 이미지타입
                    JSONObject temp = convertImage();

                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(temp.toString());
                    wr.flush();

                    StringBuilder sb = new StringBuilder();
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        //Stream을 처리해줘야 하는 귀찮음이 있음.
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(con.getInputStream(), "utf-8"));
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        br.close();
                        System.out.println("" + sb.toString());
                    } else {
                        System.out.println(con.getResponseMessage());
                    }

                    // 요청 파라미터 출력
                    // - 파라미터는 쿼리 문자열의 형식으로 지정 (ex) 이름=값&이름=값 형식&...
                    // - 파라미터의 값으로 한국어 등을 송신하는 경우는 URL 인코딩을 해야 함.
//                    try (OutputStream out = conn.getOutputStream()) {
//                        out.write("id=javaking".getBytes());
//                        out.write("&".getBytes());
//                        out.write(("name=" + URLEncoder.encode("자바킹","UTF-8")).getBytes());
//                    }
//[출처] [Java] HttpURLConnection 클래스 - URL 요청후 응답받기 ( GET방식, POST방식 )|작성자 자바킹

                    // 응답 내용(BODY)구하기기
//                    ty (InputStream in = con.getInputStream();
//                         ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//                        byte[] buf = new byte[1024 * 8];
//                        int length = 0;
//                        while ((length = in.read(buf)) != -1) {
//                            out.write(buf, 0, length);
//                        }
//                        System.out.println(new String(out.toByteArray(), "UTF-8"));
//                    }

                    return "";
                    //아래는 예외처리 부분이다.

                } catch (MalformedURLException e){

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

            return null;

        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

//            tvData.setText(result);

        }

    }
    public JSONObject convertImage(){
        Drawable temp = getResources().getDrawable(R.drawable.image1);
        Bitmap bitmap = ((BitmapDrawable)temp).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        String str = Base64.encodeToString(bitmapdata,0);

        JSONObject sObj = new JSONObject();
        try {
            sObj.put("name","test");
            sObj.put("photo",str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sObj;
    }
}
