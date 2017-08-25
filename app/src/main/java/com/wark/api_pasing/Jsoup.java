package com.wark.api_pasing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by pc on 2017-08-25.
 */

public class Jsoup extends AppCompatActivity{
TextView textView;
String  address;
String h2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = (TextView) findViewById(R.id.after_text);

        address="https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+ textView.getText().toString();

        try {
            Document doc = org.jsoup.Jsoup.connect(address).header("User-Agent","Chrome/19.0.1.84.52").get();
            h2 = doc.select("h2").eq(2).text();
            Log.e("h2",h2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
