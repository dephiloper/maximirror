package assistantmirror.maxiphil.de.mirrorconfigurator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.LayoutDirection;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    TextView textView;
    private Response.Listener listener;
    private Response.ErrorListener errorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForUpdates();
        textView = findViewById(R.id.textView1);

        Log.i("main", "started service.");
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jObj = parseToJsonObject(response);
                Log.i("FFFFFFFFF",jObj.toString());
                createTextViewsByKeys(jObj);
                createInputsForUser();
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        requestServer();
    }

    private void createInputsForUser() {
        //not working currently
        Button postButton = new Button(this.findViewById(android.R.id.content).getContext());
        postButton.setText(R.string.postButtonText);
    }

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
        //JsonObject testJSONObj = readTestJSON();
        Set<Map.Entry<String, JsonElement>> entrySet = jObj.entrySet();
        for (Map.Entry<String, JsonElement> entry: entrySet) {
            final TextView newKeyTextView = new TextView(this);
            newKeyTextView.setWidth(valueLayoutParams.getMarginStart() - keyLayoutParams.getMarginStart() + 150);
            final EditText newValueTextView = new EditText(this);

            newKeyTextView.setText(entry.getKey());

            if (entry.getValue() instanceof JsonArray){
                newValueTextView.setText(getStringFromIntricatedJObj((JsonArray) entry.getValue()));
            }else {
                newValueTextView.setText(entry.getValue().toString());
            }

            //strange magical stuff
            newKeyTextView.post(new Runnable() {
                @Override
                public void run() {
                    keyLayout.setMinimumHeight(valueLayout.getHeight());
                    newKeyTextView.setY(newValueTextView.getY());
                }
            });

            newKeyTextView.setTextSize(14);
            newKeyTextView.setTextColor(Color.BLACK);
            newValueTextView.setTextSize(14);
            newValueTextView.setTextColor(Color.BLACK);

            keyLayout.addView(newKeyTextView, keyLayoutParams);
            valueLayout.addView(newValueTextView, valueLayoutParams);
        }
        Log.i("Views", "successfully created.");
    }

    private String getStringFromIntricatedJObj(JsonArray jsonArray) {
        StringBuilder valueArray = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++){
            JsonObject jobj = (JsonObject) jsonArray.get(i);
            Set<Map.Entry<String, JsonElement>> entrySet = jobj.entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet){
                Log.i("jarray", "is read");
                valueArray.append(entry.getKey()).append(":\n");
                valueArray.append("  ");
                valueArray.append(entry.getValue()).append("\n");
            }
        }
        return valueArray.toString();
    }

    private JsonObject readTestJSON() {
        StringBuilder lol = new StringBuilder();
        InputStream in = getResources().openRawResource(R.raw.test);
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = buf.readLine()) != null) {
                lol.append(line);
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
        return parseToJsonObject(lol.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
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

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }
}
