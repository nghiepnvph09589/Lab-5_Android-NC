package com.example.buoi9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetailNewsActivity extends AppCompatActivity {
    WebView webView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        intent = getIntent();
        String link = intent.getStringExtra("linkURL");
        webView = findViewById(R.id.webview);
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
    }
}