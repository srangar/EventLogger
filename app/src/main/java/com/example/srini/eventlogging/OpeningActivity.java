package com.example.srini.eventlogging;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.io.*;
import java.util.ArrayList;
import android.widget.AdapterView;
import android.util.Log;
import org.json.*;
import android.content.Intent;
import android.content.Context;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;
public class OpeningActivity extends AppCompatActivity {
    public JSONObject jos = null;
    public JSONArray ja = null;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
    }

    protected void onResume(){
        super.onResume();
        final ListView list = findViewById(R.id.sampleListView);

        jos = null;
        try{
            // Reading a file that already exists
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
                jos = new JSONObject(j);
                ja = jos.getJSONArray("data");
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            // Show the list
            final ArrayList<ListData> aList = new ArrayList<ListData>();
            for(int i = 0; i < ja.length(); i++){

                ListData ld = new ListData();
                try {
                    ld.title = ja.getJSONObject(i).getString("title");
                    ld.event = ja.getJSONObject(i).getString("event");
                    ld.time = ja.getJSONObject(i).getString("time");
                    ld.date = ja.getJSONObject(i).getString("date");
                    ld.gps = ja.getJSONObject(i).getString("gps");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                aList.add(ld);
            }

            // Create an array and assign each element to be the title
            // field of each of the ListData objects (from the array list)
            String[] listItems = new String[aList.size()];

            for(int i = 0; i < aList.size(); i++){
                ListData listD = aList.get(i);
                listItems[i] = listD.title;
            }

            // Show the list view with the each list item an element from listItems
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
            list.setAdapter(adapter);

            // Set an OnItemClickListener for each of the list items
            final Context context = this;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ListData selected = aList.get(position);

                    // Create an Intent to reference our new activity, then call startActivity
                    // to transition into the new Activity.
                    Intent detailIntent = new Intent(context, DetailActivity.class);
                    // pass some key value pairs to the next Activity (via the Intent)
                    detailIntent.putExtra("title", selected.title);
                    detailIntent.putExtra("event", selected.event);
                    detailIntent.putExtra("time", selected.time);
                    detailIntent.putExtra("date", selected.date);
                    detailIntent.putExtra("gps", selected.gps);
                    detailIntent.putExtra("poss",""+position);
                    startActivity(detailIntent);
                }

            });
        }
        catch(IOException e){
            // There's no JSON file that exists, so don't
            // show the list. But also don't worry about creating
            // the file just yet, that takes place in AddText.

            //Here, disable the list view
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);

            //show the text view

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            //process your onClick here
            Intent createEventIntent = new Intent(context, CreateEventActivity.class);
            startActivity(createEventIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
