package com.wark.api_pasing;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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


public class MainActivity extends AppCompatActivity{
    EditText befor;
    TextView after;
    Spinner befor_spinner;
    Spinner after_spinner;
    Button change;
    String Json;
    private static String Id = "H_PGTxoOcanHVU_PmIHI";
    private static String Secret = "7LPxEe7szR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        befor = (EditText) findViewById(R.id.befor_text);
        after = (TextView) findViewById(R.id.after_text);
        befor_spinner = (Spinner) findViewById(R.id.befor_spinner);
        after_spinner = (Spinner) findViewById(R.id.after_spinner);
        change = (Button) findViewById(R.id.change);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                JSONParser parser = new JSONParser();
                JSONObject obj = null;
                try {
                    obj = (JSONObject) parser.parse(Json);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                JSONObject message = (JSONObject) obj.get("message");
                JSONObject result = (JSONObject) message.get("result");

                String tarLangType = (String) result.get("tarLangType");
                String translatedText = (String) result.get("translatedText");

                Log.e("Hi", befor.getText().toString());
                Log.e("Bey", translatedText);
                after.setText(translatedText);
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
                    String postParams = "source=ko&target=en&text=" + text;
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
                    Log.e("log", String.valueOf(br));
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
