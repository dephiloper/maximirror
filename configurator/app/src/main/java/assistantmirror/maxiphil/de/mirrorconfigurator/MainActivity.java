package assistantmirror.maxiphil.de.mirrorconfigurator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

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
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        requestServer();

     //   createTextViewsByKeys(jObj);
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
        //textView.setText(jObj.toString());
        Set<Map.Entry<String, JsonElement>> entrySet = jObj.entrySet();
        for (Map.Entry<String, JsonElement> entry: entrySet) {
            textView.setText(entry.getKey());
        }

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
