package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HealthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

TextView tvKidney,tvLiver,tvThyroid,tvHeart,tvCancer,tvHiv,tvDiabetes,tvAnemia;
    String UserId;
    DatabaseHelper myDb;

    public HealthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthFragment newInstance(String param1, String param2) {
        HealthFragment fragment = new HealthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_health, container, false);
        myDb = new DatabaseHelper(getActivity());
        getSQLiteData();
        tvKidney=(TextView)v.findViewById(R.id.tv_kidney);
        tvLiver=(TextView)v.findViewById(R.id.tv_liver);
        tvThyroid=(TextView)v.findViewById(R.id.tv_thyroid);
        tvHeart=(TextView)v.findViewById(R.id.tv_heart);
        tvCancer=(TextView)v.findViewById(R.id.tv_cancer);
        tvHiv=(TextView)v.findViewById(R.id.tv_hiv);
        tvDiabetes=(TextView)v.findViewById(R.id.tv_diabetes);
        tvAnemia=(TextView)v.findViewById(R.id.tv_anemia);
        getHealthReport();

        return v;
    }


    public int getHealthReport(){
        final String[] srtpoints = new String[1];
        final int[] p = new int[1];
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_health_report, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("yaya", s);


                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray= object.getJSONArray("result");


                    for(int i = 0; i<jsonArray.length(); i++){
                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = jsonArray.getJSONObject(i);


                            //getting image url and title from json object
                            tvKidney.setText(obj.getString("Kidney"));

                            tvLiver.setText(obj.getString("Liver"));
                            tvThyroid.setText(obj.getString("Thyroid"));
                            tvHeart.setText(obj.getString("Heart"));
                            tvCancer.setText(obj.getString("Cancer"));
                            tvHiv.setText(obj.getString("HIV/AIDS"));
                            tvDiabetes.setText(obj.getString("Diabetes"));
                            tvAnemia.setText(obj.getString("Anemia"));





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }






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
                params.put("id",UserId);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding our request to the queue
        requestQueue.add(stringRequest);

        return p[0];
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}



}
