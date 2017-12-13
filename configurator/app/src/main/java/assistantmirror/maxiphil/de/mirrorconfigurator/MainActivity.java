package assistantmirror.maxiphil.de.mirrorconfigurator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonObject;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import assistantmirror.maxiphil.de.mirrorconfigurator.config.Config;
import assistantmirror.maxiphil.de.mirrorconfigurator.config.ConfigItem;
import assistantmirror.maxiphil.de.mirrorconfigurator.config.ConfigItemBool;
import assistantmirror.maxiphil.de.mirrorconfigurator.config.ConfigItemString;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    /*RequestQueue requestQueue;
    private Response.Listener listener;
    private Response.ErrorListener errorListener;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForUpdates();




        Config config = Config.jsonToConfig(readTestJSON());
        List<ConfigItem> itemList = null;
        try {
            itemList = config.generateConfigItems();
        } catch (IllegalAccessException e) {
            System.err.println(e.getMessage());
        }

        ConfigListAdapter adapter = new ConfigListAdapter(this, itemList);
        listView = findViewById(R.id.config_entry_list);
        listView.setAdapter(adapter);

/*
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jObj = parseToJsonObject(response);
                Log.i("input JSON:",jObj.toString());
                createTextViewsByKeys(jObj);
            }
        };

        printStructure();

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.errorVolleyGET), Toast.LENGTH_LONG);
                toast.show();
            }
        };

        requestServer();*/
    }
/*
    private void requestServer() {
        String url = "https://api.chucknorris.io/jokes/random";
        requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        requestQueue.add(stringRequest);

        Log.i("requestServer","ready to return response");
    }

    private JsonObject parseToJsonObject(String response) {
        JsonElement jsonElement = new JsonParser().parse(response);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Log.i("parseToJsonObject", "successfully parsed into jsonobject");
        return jsonObject;
    }

    private void createTextViewsByKeys(JsonObject jObj){
        //Layout deklarations stuff
        ScrollView scrollView = new ScrollView(this);
        RelativeLayout.LayoutParams subRelativeLayoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        subRelativeLayoutParams.setMargins(10, 10, 0, 0);

        //Childlayout; is ParentLayout for this contentview
        RelativeLayout subRelativeLayout = new RelativeLayout(this);
        subRelativeLayout.setLayoutParams(subRelativeLayoutParams);
        subRelativeLayout.addView(scrollView);

        //ScrollView needs one Layout, that can contain others; number of its childs must be one
        RelativeLayout placeholderForScrollLayout = new RelativeLayout(this);
        scrollView.addView(placeholderForScrollLayout);

        //caption for config
        TextView captionTextView = new TextView(this);
        captionTextView.setText(R.string.configuration);
        captionTextView.setTextColor(Color.BLACK);
        captionTextView.setTextSize(22);
        captionTextView.setBackgroundColor(Color.WHITE);
        placeholderForScrollLayout.addView(captionTextView);

        //Layout for Keys
        final LinearLayout.LayoutParams keyLayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        keyLayoutParams.setMargins(20, 80, 0, 0);
        final LinearLayout keyLayout = new LinearLayout(this);
        keyLayout.setLayoutParams(keyLayoutParams);
        keyLayout.setOrientation(LinearLayout.VERTICAL);

        //Layout for Values
        LinearLayout.LayoutParams valueLayoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        valueLayoutParams.setMargins(300,60, 0, 0);
        final LinearLayout valueLayout = new LinearLayout(this);
        valueLayout.setLayoutParams(valueLayoutParams);
        valueLayout.setOrientation(LinearLayout.VERTICAL);

        //add both layouts to scrollview
        placeholderForScrollLayout.addView(keyLayout);
        placeholderForScrollLayout.addView(valueLayout);

        setContentView(subRelativeLayout);

        //pass all values in the declared layout
        JsonObject testJSONObj = readTestJSON();
        Set<Map.Entry<String, JsonElement>> entrySet = testJSONObj.entrySet();
        int counter = 0;
        for (Map.Entry<String, JsonElement> entry: entrySet) {
            final TextView newKeyTextView = new TextView(this);
            newKeyTextView.setWidth(valueLayoutParams.getMarginStart() - keyLayoutParams.getMarginStart() + 150);
            final EditText newValueTextView = new EditText(this);

            newKeyTextView.setText(entry.getKey());

            if (entry.getValue() instanceof JsonArray){
                newKeyTextView.setTypeface(null, 1);
//                for (int j = 0; j < ((JsonArray) entry.getValue()).size(); j++){
//                    JsonObject jsonObject = (JsonObject) ((JsonArray) entry.getValue()).get(j);
//                    TextView valueKeyTextView = new TextView(this);
//                    EditText valueValueEditText = new EditText(this);
//                    String[] keyValuePairs = getStringFromIntricatedJObj(jsonObject);
//                    valueKeyTextView.setText(keyValuePairs[0]);
//                    valueValueEditText.setText(keyValuePairs[1]);
//                    keyLayout.addView(valueKeyTextView);
//                    valueLayout.addView(valueValueEditText);
//                }
            }else {
                newValueTextView.setText(entry.getValue().toString());
                valueLayout.addView(newValueTextView, valueLayoutParams);
            }

            //strange magical stuff
            newKeyTextView.post(new Runnable() {
                @Override
                public void run() {
                    keyLayout.setMinimumHeight(valueLayout.getHeight());
                    newKeyTextView.setY(newValueTextView.getY());
                }
            });

            //newKeyTextView.setTag(counter, "dynamicTextViewKey");
            newKeyTextView.setTextSize(14);
            newKeyTextView.setTextColor(Color.BLACK);
            //newValueTextView.setTag(counter, "dynamicEditTextValue");
            newValueTextView.setTextSize(14);
            newValueTextView.setTextColor(Color.BLACK);

            keyLayout.addView(newKeyTextView, keyLayoutParams);
            counter++;
        }
        Log.i("Views", "successfully created.");

        Button postButton = new Button(this);
        postButton.setText(R.string.postButtonText);
        postButton.setX(1000);
        placeholderForScrollLayout.addView(postButton);
    }

    private String[] getStringFromIntricatedJObj(JsonObject jsonObjectFromArray) {
        StringBuilder keyArray = new StringBuilder();
        StringBuilder valueArray = new StringBuilder();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObjectFromArray.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet){
            Log.i("jarray", "is read");
            keyArray.append(entry.getKey());
            valueArray.append(entry.getValue());
        }
        return new String[]{keyArray.toString(), valueArray.toString()};
    }
*/

    private String readTestJSON() {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream in = getResources().openRawResource(R.raw.test);
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = buf.readLine()) != null) {
                stringBuilder.append(line);
            }
            buf.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            Log.e("readTestJSON", "failed to read");
        }
        if (in != null){
            try {
                in.close();
            } catch (IOException e) {
                Log.e("h", "k");
            }
        }
        return stringBuilder.toString();
    }

    private void checkForUpdates() {
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CrashManager.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

  /*  private void printStructure() {
       JsonObject obj = readTestJSON();
       printObject(obj);
    }

    private void printObject(JsonObject obj) {
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet){
            Log.i("Key", entry.getKey());
            if (entry.getValue().isJsonArray())
                for (JsonElement jsonElement : entry.getValue().getAsJsonArray())
                    printObject(jsonElement.getAsJsonObject());
            else
                Log.i("Value", String.valueOf(entry.getValue()));

        }
    }*/
}
