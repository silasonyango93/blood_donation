package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BloodBanks extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rView;
    DatabaseHelper myDb;
    public static Context context,mcontext;
   public static String UserId,BloodGroup;

    private GridLayoutManager lLayout;
    private ArrayList<String> BloodBankNames;
    private ArrayList<String> Locations;
    private ArrayList<String> BloodBNankIds;
    private ArrayList<String> Emails;

    public static final String TAG_BLOOD_BANK_NAME = "BloodBankName";
    public static final String TAG_LOCATION = "Location";
    public static final String TAG_BLOOD_BANK_ID = "BloodBankId";
    public static final String TAG_EMAIL = "Email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_banks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mcontext=BloodBanks.this;



        myDb = new DatabaseHelper(getBaseContext());
        getSQLiteData();

        BloodBankNames = new ArrayList<>();
        Locations = new ArrayList<>();
        BloodBNankIds = new ArrayList<>();
        Emails = new ArrayList<>();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lLayout = new GridLayoutManager(getBaseContext(), 1);

        rView = (RecyclerView) findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                BloodBankNames.clear();
                Locations.clear();
                BloodBNankIds.clear();
                Emails.clear();

                getBloodBanks();

                // getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getBloodBanks();

    }

    public void getSQLiteData() {

        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {
            //Show message
            showMessage("Error", "No data found Silas");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("dbID : " + res.getString(0) + "\n");
            buffer.append("id : " + res.getString(1) + "\n");
            buffer.append("Name : " + res.getString(2) + "\n");
            buffer.append("Email : " + res.getString(3) + "\n\n");

            UserId=res.getString(1);
        }

        //Show all data

        // showMessage("Data", buffer.toString());


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}


    private void getBloodBanks(){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_blood_banks, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("yaya", s);


                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array= object.getJSONArray("result");
                    showGrid(array);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("ggg", volleyError.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                // String BloodGroup="B";
               // params.put("BloodGroup",BloodGroup);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }


    private void showGrid(JSONArray jsonArray){
        //Looping through all the elements of json array

        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);


                //getting image url and title from json object
                BloodBankNames.add(obj.getString(TAG_BLOOD_BANK_NAME));

                Locations.add(obj.getString(TAG_LOCATION));
                BloodBNankIds.add(obj.getString(TAG_BLOOD_BANK_ID));
                Emails.add(obj.getString(TAG_EMAIL));




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("Errorr", this.images.get(0));
        RecyclerViewAdapterBanks rcAdapter = new RecyclerViewAdapterBanks(getBaseContext(),BloodBankNames,Locations,BloodBNankIds,Emails);
        rView.setAdapter(rcAdapter);
        //Creating GridViewAdapter Object
      /*  GridViewAdapter gridViewAdapter = new GridViewAdapter(this,images,names);

        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);*/

    }

}
