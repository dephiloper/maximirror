package assistantmirror.maxiphil.de.mirrorconfigurator;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
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
import java.util.Map;

import assistantmirror.maxiphil.de.mirrorconfigurator.config.Config;
import assistantmirror.maxiphil.de.mirrorconfigurator.config.ConfigItem;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Config config = null;
    FloatingActionMenu fab;
    boolean focusedChanged = false;
    private RequestQueue requestQueue;
    String postURL = "http://192.168.1.4:5000/updateconfig";
// Todo implement POST
    // Todo use databinding for easier workflow --> https://medium.com/google-developers/android-data-binding-list-tricks-ef3d5630555e
    // Todo add button to read all edittexts and checkboxed and put them back into the Config class to push them to the server
    // Todo improve listview performance http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForUpdates();
        listView = findViewById(R.id.config_entry_list);
        fab = findViewById(R.id.fab_menu);
        fab.showMenu(false);
        getCurrentRequestQueue().add(getStringRequestGET("config"));
        setupListViewListeners();

    }

    private StringRequest getStringRequestGET(final String parameterString) {
        return new StringRequest(Request.Method.GET, "http://192.168.1.4:5000/"+parameterString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (parameterString.equals("config")){
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
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to load config, please check your Internet Connection to your WiFi.", Toast.LENGTH_LONG);
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

    private void setupListViewListeners() {

        /*listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusedChanged = true;
                if (fab.isShown())
                    fab.hideMenu(false);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (focusedChanged) fab.showMenu(false);
                Log.i("onsrollchanged", "called");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (fab.isShown() && focusedChanged) {
                    fab.hideMenu(false);
                }
            }
        });*/
    }

    private void checkForUpdates() {
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }

    public void postButtonPressed(View view) {
        fab.close(true);
        List<ConfigItem> items = ((ConfigListAdapter)listView.getAdapter()).getItems();
        System.out.println(items);
        Config config = new Config();
        config = config.generateConfigFromItemList(items);
        final String jsonString = Config.configToJson(config);
        //JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        StringRequest postJsonObjectRequest = new StringRequest(Request.Method.POST, postURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("post", "successfully");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("post", "no success");
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
}