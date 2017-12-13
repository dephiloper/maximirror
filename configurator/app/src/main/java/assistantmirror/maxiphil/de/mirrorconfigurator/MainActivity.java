package assistantmirror.maxiphil.de.mirrorconfigurator;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import assistantmirror.maxiphil.de.mirrorconfigurator.config.Config;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Config config = null;

    // Todo use databinding for easier workflow --> https://medium.com/google-developers/android-data-binding-list-tricks-ef3d5630555e
    // Todo add button to read all edittexts and checkboxed and put them back into the Config class to push them to the server
    // Todo improve listview performance http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForUpdates();
        listView = findViewById(R.id.config_entry_list);
        final Context appContext = this;


        StringRequest request = new StringRequest(Request.Method.GET, "https://raw.githubusercontent.com/dephiloper/logger/develop/src/main/resources/config.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                config = Config.jsonToConfig(response);
                try {
                    listView.setAdapter(new ConfigListAdapter(appContext, config.generateConfigItems()));
                } catch (IllegalAccessException e) {
                    System.err.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.errorVolleyGET), Toast.LENGTH_LONG);
                toast.show();
            }
        });

        Volley.newRequestQueue(this).add(request);
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
}