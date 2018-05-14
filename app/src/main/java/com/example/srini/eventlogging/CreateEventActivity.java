package com.example.srini.eventlogging;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    public JSONObject jo = null;
    public JSONArray ja = null;
    private final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Start up the Location Service
        startService(new Intent(this, MyService.class));
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                99);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_activity);

        final EditText first = findViewById(R.id.editText);
        final EditText second = findViewById(R.id.editText3);
        Button b = findViewById(R.id.button2);

        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Calendar cal = Calendar.getInstance();
        final String date = df.format(cal.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        final String time = timeFormat.format(cal.getTime());


        // Read the file


        try{
            File f = new File(getFilesDir(), "file.ser");
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream o = new ObjectInputStream(fi);
            // Notice here that we are de-serializing a String object (instead of
            // a JSONObject object) and passing the String to the JSONObject’s
            // constructor. That’s because String is serializable and
            // JSONObject is not. To convert a JSONObject back to a String, simply
            // call the JSONObject’s toString method.
            String j = null;
            try{
                j = (String) o.readObject();
            }
            catch(ClassNotFoundException c){
                c.printStackTrace();
            }
            try {
                jo = new JSONObject(j);
                ja = jo.getJSONArray("data");
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        catch(IOException e){
            // Here, initialize a new JSONObject
            jo = new JSONObject();
            ja = new JSONArray();
            try{
                jo.put("data", ja);
            }
            catch(JSONException j){
                j.printStackTrace();
            }
        }

        b.setOnClickListener(new Button.OnClickListener(){
            @SuppressLint("MissingPermission")
            public void onClick(View v){
                String firstText = first.getText().toString();
                String secondText = second.getText().toString();

                JSONObject temp = new JSONObject();
                try {
                    temp.put("title", firstText);
                    temp.put("event", secondText);
                    temp.put("time", time);
                    temp.put("date", date);
                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        List<String> providers = lm.getProviders(true);
                        Location l = lm.getLastKnownLocation(providers.get(0));
                        temp.put("gps", l.getLatitude()+ ", "+ l.getLongitude());
                }
                catch(JSONException j){
                    j.printStackTrace();
                }

                ja.put(temp);

                // write the file
                try{
                    File f = new File(getFilesDir(), "file.ser");
                    FileOutputStream fo = new FileOutputStream(f);
                    ObjectOutputStream o = new ObjectOutputStream(fo);
                    String j = jo.toString();
                    o.writeObject(j);
                    o.close();
                    fo.close();
                }
                catch(IOException e){

                }

                //pop the activity off the stack
                Intent i = new Intent(CreateEventActivity.this, OpeningActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }
}
