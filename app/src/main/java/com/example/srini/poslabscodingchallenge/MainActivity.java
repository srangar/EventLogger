package com.example.srini.poslabscodingchallenge;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final Context context = this;
    private  ArrayList<Song> songs;
    private final String TAG="TAGGGGG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String json;

        ListView list = findViewById(R.id.listView);
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return;
        }
        // Put json data into a JSONArray
        JSONArray dataList;
        JSONObject jsonO;
        try {
            jsonO = new JSONObject(json);

            dataList = jsonO.getJSONArray("results");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
       songs = new ArrayList<Song>();
       for(int i=0; i<dataList.length();i++)
       {
           try {
               String title = dataList.getJSONObject(i).getString("trackName");
               String pic = dataList.getJSONObject(i).getString("artworkUrl30");
               String artist = dataList.getJSONObject(i).getString("artistName");
               String collection = dataList.getJSONObject(i).getString("collectionName");
               songs.add(new Song(title,artist,collection,pic));


           }catch (JSONException j) {
               j.printStackTrace();
           }
       }

        list.setAdapter(new CustomAdapter(this,songs));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song s = songs.get(position);
                Intent detailIntent = new Intent(context, DetailActivity.class);
                  detailIntent.putExtra("title",s.getTitle());
                 detailIntent.putExtra("artist",s.getArtist());
                 detailIntent.putExtra("collection",s.getCollection());
                detailIntent.putExtra("url",s.getURL());
                 startActivity(detailIntent);
            }

        });


    }
}
