package com.example.restapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ContactDialog extends Activity implements View.OnClickListener {
    // 사진 가져오기
    private static final int REQUEST_TAKE_ALBUM = 3333;
    Uri photoURI;
    Bitmap tempbitmap;

    private Button completeButton;
    private Button exitButton;
    private EditText editName;
    private EditText editPhoneNumber;
    private ImageView editimage;
    private Button profilePhotoButton;
    private int imageSet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contact_dialog);

        //init
        profilePhotoButton = (Button)findViewById(R.id.addProfilePhoto);
        completeButton = (Button)findViewById(R.id.complete_contact);
        exitButton = (Button)findViewById(R.id.exit_contact);

        editName = (EditText)findViewById(R.id.contact_name);
        editPhoneNumber = (EditText)findViewById(R.id.contact_phonenumber);
        editimage = (ImageView)findViewById(R.id.imageView1);



        completeButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        profilePhotoButton.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.complete_contact:
                if (imageSet == 1){
                    String text1 = editName.getText().toString();
                    String text2 = editPhoneNumber.getText().toString();
                    if (text1.equals("")){
                        Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else if(text2.equals("")){
                        Toast.makeText(this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // 서버에 api콜 보냄.
                        new ContactDialog.JSONPostTask().execute("http://143.248.36.59:8080/api/contact/add");
                        Intent intent = new Intent();
                        intent.putExtra("name", text1);
                        intent.putExtra("phonenum", text2);
                        String temp = getBase64String(tempbitmap);
                        intent.putExtra("photo", temp);
                        setResult(RESULT_OK, intent);
                        this.finish();
                    }
                }
                else {
                    Toast.makeText(this, "이미지를 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.exit_contact:
                this.finish();
                break;
            case R.id.addProfilePhoto:
                getAlbum();
        }
    }

    @Override // 갤러리에서 이미지 불러와서 세팅
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_ALBUM:
                imageSet = 0;
                if (resultCode == Activity.RESULT_OK) {
                    if(data.getData() != null){
                        try {
                            photoURI = data.getData();
                            try {
                                tempbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                                editimage.setImageURI(photoURI);
                                imageSet = 1;
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }catch (Exception e){
                            Log.e("TAKE_ALBUM_SINGLE ERROR", e.toString());
                        }
                    }
                }
                break;
        }
    }

    private void getAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
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

                    // 서버 전송을 위해.
                    JSONObject temp = ImageToJson(tempbitmap);

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
        }

    }

    public JSONObject ImageToJson(Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        String str_image = Base64.encodeToString(bitmapdata,0);

        // 넣을 객체 설정!!!!!@!@!
        String name = editName.getText().toString();
        String phonenum = editPhoneNumber.getText().toString();

        JSONObject sObj = new JSONObject();
        try {
            sObj.put("name",name);
            sObj.put("phonenum",phonenum);
            sObj.put("photo",str_image);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sObj;
    }

    public String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

}