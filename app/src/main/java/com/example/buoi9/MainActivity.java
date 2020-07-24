package com.example.buoi9;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public String link = "https://vnexpress.net/rss/tin-moi-nhat.rss";

    public List<MyItem> myItems;

    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayList<String> arrayLink = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    Intent intent;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myItems = new ArrayList<>();
        listView = findViewById(R.id.lv);
        intent = new Intent(this, DetailNewsActivity.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = arrayLink.get(position);
                intent.putExtra("linkURL", link);
                startActivity(intent);
            }
        });

    }

    public void getData(View view) {

        AsyncTask asyncTask = new AsyncTask() {

            //hàm sử lý luồng
            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    URL url = new URL(link);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = httpURLConnection.getInputStream();

                    XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                    xmlPullParserFactory.setNamespaceAware(false);

                    XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                    xmlPullParser.setInput(inputStream,"utf-8");

                    int eventType = xmlPullParser.getEventType();
                    MyItem myItem = null;
                    String text = "";
                    while (eventType != xmlPullParser.END_DOCUMENT){
                        String tag = xmlPullParser.getName();
                        switch (eventType){
                            case XmlPullParser.START_TAG:
                            if (tag.equalsIgnoreCase("item")){
                                myItem = new MyItem();
                            }
                            break;
                            case XmlPullParser.TEXT:
                                text = xmlPullParser.getText();
                                break;
                            case XmlPullParser.END_TAG:
                                if (myItem != null){
                                    if (tag.equalsIgnoreCase("title")){
                                        myItem.title = text;
                                    } else if (tag.equalsIgnoreCase("description")){
                                        myItem.description = text;
                                    } else if (tag.equalsIgnoreCase("pubDate")){
                                        myItem.pubDate = text;
                                    } else if (tag.equalsIgnoreCase("link")) {
                                        myItem.link = text;
                                    } else if (tag.equalsIgnoreCase("item")){
                                        myItems.add(myItem);
                                        arrayList.add(myItem.title+"\n"+myItem.pubDate);
                                        arrayLink.add(myItem.link);
                                    }
                                }
                                break;
                        }
                        eventType = xmlPullParser.next();

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("loi", e.getMessage());

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                return null;
            }
            //hàm gọi hiển thị dữ liệu sau khi kết thúc luồng
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Toast.makeText(MainActivity.this, myItems.size() + "",Toast.LENGTH_SHORT).show();
//                arrayAdapter.notifyDataSetChanged();
                arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(arrayAdapter);
            }
        };
        asyncTask.execute();
    }

}