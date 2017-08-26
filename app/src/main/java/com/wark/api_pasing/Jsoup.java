package com.wark.api_pasing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by pc on 2017-08-25.
 */

public class Jsoup extends AppCompatActivity{
TextView textView;
String  address_jsoup;
String h2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = (TextView) findViewById(R.id.after_text);

        address_jsoup="https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+ textView.getText().toString();



    }
}
