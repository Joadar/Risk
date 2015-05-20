package fr.fliizweb.risk.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;


public class StatsActivity extends Activity {

    Context context;

    AQuery aQuery;
    private final String FEED_URL = "http://172.31.1.54/Risk/scores/32";

    public StatsActivity(){
        this.context = getApplicationContext();
    }

    public void startActivity(){
        Intent intent = new Intent(context, StatsActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        aQuery = new AQuery(this.getApplicationContext());
        aQuery.ajax(FEED_URL, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {


                if (json != null) {
                    Log.d("MainActivity", "Json return = " + json);
                    //successful ajax call, show status code and json content
                    //Toast.makeText(aq.getContext(), status.getCode() + ":" + json.toString(), Toast.LENGTH_LONG).show();

                    if (status.getCode() == 200) {
                        JSONArray notesObj;

                       /* try {
                            notesObj = json.getJSONArray("notes");

                            for (int i = 0; i < notesObj.length(); i++) {
                                JSONObject noteObj = (JSONObject) notesObj.get(i);
                                note.createNoteByAPI(Long.valueOf(noteObj.getString("id")).longValue(), noteObj.getString("title"), noteObj.getString("content"));
                                Log.d("MainActivity", "Note " + i + " = " + noteObj.getString("title"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                    }

                } else {

                    //ajax error, show error code
                    //Toast.makeText(aq.getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
