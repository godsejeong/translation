package com.wark.api_pasing;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    EditText befor;
    TextView after;
    Spinner befor_spinner;
    Spinner after_spinner;
    Button change;
    String Json;
    String source;
    String target;
    private static String Id = "H_PGTxoOcanHVU_PmIHI";
    private static String Secret = "7LPxEe7szR";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        befor = (EditText) findViewById(R.id.befor_text);
        after = (TextView) findViewById(R.id.after_text);
        befor_spinner = (Spinner) findViewById(R.id.befor_spinner);
        after_spinner = (Spinner) findViewById(R.id.after_spinner);
        change = (Button) findViewById(R.id.change);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.select, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        befor_spinner.setAdapter(adapter);
        after_spinner.setAdapter(adapter);

//        Jsoup jsoup = new Jsoup();
//        Log.e("h2",jsoup.h2);

        befor_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            String a;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                a = (String) befor_spinner.getAdapter().getItem(befor_spinner.getSelectedItemPosition());

                switch (a){
                    case "한국어":

                        Toast.makeText(getApplicationContext(),"한국어",Toast.LENGTH_LONG).show();
                        source="ko";
                        break;
                    case "영어":

                        Toast.makeText(getApplicationContext(),"영어",Toast.LENGTH_LONG).show();
                        source="en";
                        break;
                    case "일본어":

                        Toast.makeText(getApplicationContext(),"일본어",Toast.LENGTH_LONG).show();
                        source="ja";
                        break;
                    case "중국어(간체)":

                        Toast.makeText(getApplicationContext(),"중국어(긴체)",Toast.LENGTH_LONG).show();
                        source="zh-CN";
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"언어를 선택해 주세요",Toast.LENGTH_LONG).show();
                        source=null;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        after_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            String a;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    a = (String) after_spinner.getAdapter().getItem(after_spinner.getSelectedItemPosition());


                switch (a){
                    case "한국어" :
                        target="ko";
                        break;
                    case "영어":
                        target="en";
                        break;
                    case "일본어":
                        target="ja";
                        break;
                    case "중국어(간체)":
                        target="zh-CN";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                JSONParser parser = new JSONParser();
                try {
                    JSONObject obj = (JSONObject) parser.parse(Json);
                    JSONObject message = (JSONObject) obj.get("message");
                    JSONObject result = (JSONObject) message.get("result");

                    String translatedText = (String) result.get("translatedText");
                    after.setText(translatedText);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"지원하지 않습니다. 다시 선택해 주세요",Toast.LENGTH_SHORT).show();
                }


            }
        };

        new Thread() {
            public void run() {
                try {
                    String text = URLEncoder.encode(befor.getText().toString(), "UTF-8");

                    String apiURL = "https://openapi.naver.com/v1/language/translate";
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("X-Naver-Client-Id", Id);
                    con.setRequestProperty("X-Naver-Client-Secret", Secret);
                    // post request
                    String postParams = "source="+ source + "&target="+target+"&text=" + text;
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(postParams);
                    wr.flush();
                    wr.close();
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    // 정상 호출
                    if (responseCode == 200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    Json = response.toString();
                    br.close();
                    Log.e("Json",response.toString());
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
            }
        });

    }
}
