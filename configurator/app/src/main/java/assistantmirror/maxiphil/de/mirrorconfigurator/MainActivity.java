package assistantmirror.maxiphil.de.mirrorconfigurator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionMenu;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

import assistantmirror.maxiphil.de.mirrorconfigurator.config.Config;
import assistantmirror.maxiphil.de.mirrorconfigurator.config.ConfigItem;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Config config = null;
    FloatingActionMenu fab;
    private RequestQueue requestQueue;
    String serverURL;
    static Context context;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        serverURL = getServerIPFromSharedPrefs();
        setContentView(R.layout.activity_main);
        checkForUpdates();
        listView = findViewById(R.id.config_entry_list);
        fab = findViewById(R.id.fab_menu);
        fab.showMenu(false);
        getCurrentRequestQueue().add(getStringRequestGET("config"));
    }

    private String getServerIPFromSharedPrefs() {
        return sharedPreferences.getString("serverIP", "Treffen sich zwei, einer kommt.");
    }

    private StringRequest getStringRequestGET(final String parameterString) {
        return new StringRequest(Request.Method.GET, serverURL+parameterString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (parameterString.equals("config")) {
                    config = Config.jsonToConfig(response);
                    try {
                        listView.setAdapter(new ConfigListAdapter(getApplicationContext(), config.generateConfigItems()));
                    } catch (IllegalAccessException e) {
                        System.err.println(e.getMessage());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), String.format("Verbindungsaufbau zu '%s' nicht möglich.",getServerIPFromSharedPrefs()), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private RequestQueue getCurrentRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
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

    public void postButtonPressed(View view) {
        fab.close(true);
        List<ConfigItem> items = ((ConfigListAdapter)listView.getAdapter()).getItems();
        System.out.println(items);
        Config config = new Config();
        config = config.generateConfigFromItemList(items);
        final String jsonString = Config.configToJson(config);
        StringRequest postJsonObjectRequest = new StringRequest(Request.Method.POST, serverURL+"updateconfig", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("post", "successfully");
                Toast.makeText(MainActivity.context, "Config erfolgreich geupdated", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("post", "no success");
                Toast.makeText(MainActivity.context, "Config konnte nicht geupdated werden", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "text/plain; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    if (jsonString == null){
                        VolleyLog.v("warning", "empty body in request");
                        return null;
                    }else{
                        return jsonString.getBytes("utf-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.e("unsupported encoding while trying to create body");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null){
                    responseString = String.valueOf(response.statusCode);
                }else{
                    return null;
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        getCurrentRequestQueue().add(postJsonObjectRequest);
    }

    public void reloadButtonPressed(View view) {
        fab.close(true);
        getCurrentRequestQueue().add(getStringRequestGET("config"));
    }

    public void startServerButtonPressed(View view) {
        fab.close(true);
        getCurrentRequestQueue().add(getStringRequestGET("start"));
    }

    public void stopServerButtonPressed(View view) {
        fab.close(true);
        getCurrentRequestQueue().add(getStringRequestGET("stop"));
    }

    public void updateNewMirrorSoftware(View view) {
        fab.close(true);
        getCurrentRequestQueue().add(getStringRequestGET("upgrade"));
    }

    public void resetConfigOnServer(View view) {
        fab.close(true);
        getCurrentRequestQueue().add(getStringRequestGET("reset"));
    }

    public void setUpIPForServer(View view) {
        EditText ipEditText = findViewById(R.id.textEditIP);
        String ip = ipEditText.getText().toString();
        initializeServerIP(ip);
        writeToFile(ip);
    }

    private void initializeServerIP(String serverURL){
        this.serverURL = serverURL;
    }

    private void writeToFile(String ip) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("serverIP", ip);
        editor.apply();
    }
}