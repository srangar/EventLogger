package com.example.srini.eventlogging;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*This is the class of the Activity that gets called when the user selects
an item from the list.
 */

public class DetailActivity extends AppCompatActivity {
    private final Context context = this;
    public JSONObject jo = null;
    public JSONArray ja = null;
    @Override
    @TargetApi(19)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String description = i.getStringExtra("event");
        String gps = i.getStringExtra("gps");
        String time = i.getStringExtra("time");
        String date = i.getStringExtra("date");
        final String position = i.getStringExtra("poss");

        TextView t = (TextView) findViewById(R.id.textView15);
        TextView tim = (TextView) findViewById(R.id.textView14);
        TextView ddate = (TextView) findViewById(R.id.textView16);
        TextView g = (TextView) findViewById(R.id.textView17);
        TextView e = (TextView) findViewById(R.id.textView18);

        t.setText(title);
        e.setText(description);
        tim.setText(time);
        g.setText(gps);
        ddate.setText(date);

        Button delete = findViewById(R.id.button);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(context,OpeningActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

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
                ja.remove(Integer.parseInt(position));//deleted
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
                startActivity(i);
            }
        });
    }



    protected void onResume(Bundle savedInstanceState){

    }
}
