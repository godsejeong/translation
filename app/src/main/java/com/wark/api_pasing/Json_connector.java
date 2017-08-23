package com.wark.api_pasing;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by pc on 2017-08-18.
 */

public class Json_connector extends AppCompatActivity{
    private static String Id = "H_PGTxoOcanHVU_PmIHI";
    private static String Secret = "7LPxEe7szR";

    String json;
    EditText befor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        befor = (EditText) findViewById(R.id.befor_text);


        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {

            }
        };

        new Thread() {
            public void run() {
                try {
                    String text = java.net.URLEncoder.encode("안녕", "UTF-8");

                    String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("X-Naver-Client-Id", Id);
                    con.setRequestProperty("X-Naver-Client-Secret", Secret);
                    // post request
                    String postParams = "source=ko&target=en&text=" + text;
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeUTF(postParams);
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

                    br.close();
                    json = response.toString();
                    Log.e("log",postParams);
                    Log.e("Json",json);
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
}


