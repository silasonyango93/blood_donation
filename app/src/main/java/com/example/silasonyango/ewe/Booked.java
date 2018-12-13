package com.example.silasonyango.ewe;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class Booked extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rView;
    DatabaseHelper myDb;
    public static Context context;
    String UserId;

    private GridLayoutManager lLayout;

    private ArrayList<String> FullNames;
    private ArrayList<String> Genders;
    private ArrayList<String> BloodGroups;
    private ArrayList<String> PhoneNumbers;
    private ArrayList<String> Counties;
    private ArrayList<String> Cities;
    private ArrayList<String> ids;
    private ArrayList<String> ResidentialAddresses;
    private ArrayList<String> BookedTimes;
    private ArrayList<String> emails;

    private ArrayList<String> Periods;

    //Tag values to read from json
    public static final String TAG_FULL_NAME = "FullNames";
    public static final String TAG_GENDER = "Gender";
    public static final String TAG_BLOODGROUP = "BloodGroup";
    public static final String TAG_PHONE_NUMBER = "PhoneNumber";
    public static final String TAG_COUNTY = "County";
    public static final String TAG_CITY = "City";
    public static final String TAG_ID = "BookedId";
    public static final String TAG_RESIDENTIAL_ADDRESS = "ResidentialAddress";
    public static final String TAG_BOOKED_TIME = "BookedTime";
    public static final String TAG_EMAIL = "email";

    public static final String TAG_PERIOD = "Period";

    public Booked() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_booked, container, false);
        myDb = new DatabaseHelper(getContext());
        getSQLiteData();
        boolean net= isNetworkConnected();
        if(net!=true)
        {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
        else{getData();}


        FullNames = new ArrayList<>();
        Genders = new ArrayList<>();
        BloodGroups = new ArrayList<>();
        PhoneNumbers = new ArrayList<>();
        Counties= new ArrayList<>();
        Cities = new ArrayList<>();
        ids = new ArrayList<>();
        ResidentialAddresses = new ArrayList<>();
        BookedTimes = new ArrayList<>();
        emails = new ArrayList<>();

        Periods = new ArrayList<>();

        lLayout = new GridLayoutManager(getActivity(), 1);

        rView = (RecyclerView) v.findViewById(R.id.recycler_view1);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        context=getContext();


        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipe_container);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                FullNames.clear();
                Genders.clear();
                BloodGroups.clear();
                PhoneNumbers.clear();
                Counties.clear();
                Cities.clear();
                ids.clear();
                ResidentialAddresses.clear();
                Periods.clear();
                BookedTimes.clear();
                emails.clear();

                Periods.clear();


                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;

    }
    private void getData(){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Please wait...","Fetching data...",false,false);
        loading.setCancelable(true);
        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_my_tags, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);

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
                params.put("id",UserId);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                FullNames.add(obj.getString(TAG_FULL_NAME));
                Genders.add(obj.getString(TAG_GENDER));
                BloodGroups.add(obj.getString(TAG_BLOODGROUP));
                PhoneNumbers.add(obj.getString(TAG_PHONE_NUMBER));
                Counties.add(obj.getString(TAG_COUNTY));
                Cities.add(obj.getString(TAG_CITY));
                ids.add(obj.getString(TAG_ID));
                ResidentialAddresses.add(obj.getString(TAG_RESIDENTIAL_ADDRESS));
                BookedTimes.add(obj.getString(TAG_BOOKED_TIME));
                emails.add(obj.getString(TAG_EMAIL));

                Periods.add(obj.getString(TAG_PERIOD));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("Errorr", this.images.get(0));
        RecyclerViewAdapterThree rcAdapter = new RecyclerViewAdapterThree(getActivity(),FullNames,Genders,BloodGroups,PhoneNumbers,Counties,Cities,ids,ResidentialAddresses,BookedTimes,emails,Periods);
        rView.setAdapter(rcAdapter);
        //Creating GridViewAdapter Object
      /*  GridViewAdapter gridViewAdapter = new GridViewAdapter(this,images,names);

        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);*/

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}


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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}

