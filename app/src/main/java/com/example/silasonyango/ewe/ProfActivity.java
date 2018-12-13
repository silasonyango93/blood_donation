package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfActivity extends AppCompatActivity {
    String UserId;
    TextView tvFullNames,tvGender,tvBloodGroup,tvPhoneNumber,tvCounty,tvCity,tvResidentialAddress;
    NetworkImageView LargeImageView;
    public String LargeImageUrl,TaggerEmail;
    public ImageLoader imageLoader,imageLoader1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TaggerEmail=getIntent().getStringExtra("TaggerEmail");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvFullNames=(TextView)findViewById(R.id.tv_full_names);
        tvGender=(TextView)findViewById(R.id.tv_gender);
        tvBloodGroup=(TextView)findViewById(R.id.tv_blood_group);
        tvPhoneNumber=(TextView)findViewById(R.id.tv_phone_number);
        tvCounty=(TextView)findViewById(R.id.tv_county);
        tvCity=(TextView)findViewById(R.id.tv_city);
        tvResidentialAddress=(TextView)findViewById(R.id.tv_residential_address);

        LargeImageView=(NetworkImageView)findViewById(R.id.large_render);

        boolean net= isNetworkConnected();
        if(net!=true)
        {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
        else{getLargeImage(LargeImageView,tvFullNames,tvGender,tvBloodGroup,tvPhoneNumber,tvCounty,tvCity,tvResidentialAddress);}


    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}


    private void getLargeImage(final NetworkImageView largeimageview, final TextView fullnames, final TextView gender, final TextView bloodgroup, final TextView phonenumber, final TextView county, final TextView city, final TextView residentialaddress){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);
        loading.setCancelable(true);
        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_tagger_profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array= object.getJSONArray("result");
                    inflateLargeImage(array,largeimageview,fullnames,gender,bloodgroup,phonenumber,county,city,residentialaddress);

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("email",TaggerEmail);

                return stringMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void inflateLargeImage(JSONArray jsonArray, NetworkImageView largeimageview, TextView fullnames, TextView gender, TextView bloodgroup, TextView phonenumber, TextView county, TextView city, TextView residentialaddress){
        //Looping through all the elements of json array

        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);

                LargeImageUrl= obj.getString("url");
                fullnames.setText(obj.getString("FullNames"));
                gender.setText(obj.getString("Gender"));
                bloodgroup.setText(obj.getString("BloodGroup"));
                phonenumber.setText(obj.getString("PhoneNumber"));
                county.setText(obj.getString("County"));
                city.setText(obj.getString("City"));
                residentialaddress.setText(obj.getString("ResidentialAddress"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // showMessage("url",ImageUrl);
        imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();
        imageLoader.get(LargeImageUrl, ImageLoader.getImageListener(largeimageview, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        largeimageview.setImageUrl(LargeImageUrl,imageLoader);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
