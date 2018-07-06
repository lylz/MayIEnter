package com.squad.mayienter.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.squad.mayienter.R;
import com.squad.mayienter.interfaces.ScannerFragmentInteraction;

import org.json.JSONException;
import org.json.JSONObject;

public class ScannerFragmentActivity extends AppCompatActivity
            implements ScannerFragmentInteraction {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_fragment);
        setupToolbar();
        queue = Volley.newRequestQueue(this);
    }

    public void setupToolbar() {
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void handleScannerResults(Result rawResult) {
        String url = getString(R.string.api_endpoint);
        JSONObject body = new JSONObject();

        try {
            body.put("data", rawResult.getText());
        } catch (JSONException ex) {
            Log.e(getClass().getSimpleName(), "Error while trying to build a body for a request.");
            ex.printStackTrace();
        }

        JsonRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO: Do something more.
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                        finish();
                    }
                }
        );

        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
