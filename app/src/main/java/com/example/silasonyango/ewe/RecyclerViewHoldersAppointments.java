package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SilasOnyango on 9/9/2017.
 */
public class RecyclerViewHoldersAppointments extends RecyclerView.ViewHolder implements View.OnClickListener {

    AlertDialog.Builder builder;
    android.app.AlertDialog alertDialog,alertDialog2;
    public TextView Product,Period,checkBook;
    public Context mContext;

    NetworkImageView largeImage;
    public String UserId,BookedId,LargeImageUrl;
    public ImageLoader imageLoader, imageLoader1;
    public Context bContext;
    public NetworkImageView ProfilePhoto;
    TextView tvName,tvCity,tvPostedDate;
    LayoutInflater inflater, inflater1;

    public RecyclerViewHoldersAppointments(View itemView) {
        super(itemView);
        mContext = DonorActivity.mContext;
        this.UserId = Landing.UserId;
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        inflater1 = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        itemView.setOnClickListener(this);

        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvCity = (TextView) itemView.findViewById(R.id.tv_city);
        tvPostedDate = (TextView) itemView.findViewById(R.id.tv_donation_date);

    }

    @Override
    public void onClick(View view) {
        View v = inflater.inflate(R.layout.my_donation_info,null);



        TextView tvFullName=(TextView)v.findViewById(R.id.tv_full_names);
        TextView tvGender=(TextView)v.findViewById(R.id.tv_gender);
        TextView tvBloodGroup=(TextView)v.findViewById(R.id.tv_blood_group);
        TextView tvPhoneNumber=(TextView)v.findViewById(R.id.tv_phone_number);
        TextView tvemail=(TextView)v.findViewById(R.id.tv_email);
        TextView tvCounty=(TextView)v.findViewById(R.id.tv_county);
        TextView tvCity=(TextView)v.findViewById(R.id.tv_city);
        TextView tvResidentialAddress=(TextView)v.findViewById(R.id.tv_residential_address);
        TextView tvDonationDate=(TextView)v.findViewById(R.id.tv_donation_date);



        checkBook=(TextView) v.findViewById(R.id.check_book);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fontawesome-webfont.ttf");

        checkBook.setTypeface(font);




        bContext=view.getContext();

        boolean net= isNetworkConnected();
        if(net!=true)
        {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
        else{ getLargeImage(view.getContext(),tvFullName,tvGender,tvBloodGroup,tvPhoneNumber,tvemail,tvCounty,tvCity,tvResidentialAddress,tvDonationDate,v);
            previewUpload(v);}


        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void previewUpload(View v) {
        builder = new AlertDialog.Builder(bContext);

        builder.setView(v);
        final AlertDialog alertDialog1 = builder.create();
        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");


        alertDialog1.show();
        checkBook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean net= isNetworkConnected();
                if(net!=true)
                {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
                else{View p = inflater.inflate(R.layout.tags_list, null);

                    inflateList(p);}

            }
        });


    }



    public void getLargeImage(final Context context, final TextView tvFullName, final TextView tvGender, final TextView tvBloodGroup, final TextView tvPhoneNumber, final TextView tvemail,final TextView tvCounty,final TextView tvCity,final TextView tvResidentialAddress,final TextView tvDonationDate, final View v){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(context, "Please wait...","Fetching data...",false,false);
        loading.setCancelable(true);
        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_large_image, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array= object.getJSONArray("result");


                    inflateLargeImage(context,v,array,tvFullName,tvGender,tvBloodGroup,tvPhoneNumber,tvemail,tvCounty,tvCity,tvResidentialAddress,tvDonationDate);


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
                stringMap.put("BookedId",BookedId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }



    public void inflateLargeImage(Context context,View v,JSONArray jsonArray, TextView tvFullName, TextView tvGender, TextView tvBloodGroup, TextView tvPhoneNumber, TextView tvemail,TextView tvCounty,TextView tvCity,TextView tvResidentialAddress,TextView tvDonationDate) {
        //Looping through all the elements of json array
        String ProductMake= new String();
        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);



                tvFullName.setText(obj.getString("FullNames"));
                tvGender.setText(obj.getString("Gender"));
                tvBloodGroup.setText(obj.getString("BloodGroup"));
                tvPhoneNumber.setText(obj.getString("PhoneNumber"));
                tvemail.setText(obj.getString("email"));

                tvCounty.setText(obj.getString("County"));
                tvCity.setText(obj.getString("City"));
                tvResidentialAddress.setText(obj.getString("ResidentialAddress"));
                tvDonationDate.setText(obj.getString("BookedTime"));

                //renderLargeImage(context,v);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(bContext);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}

    private void bookProduct(Context mContext){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(mContext, "Please wait...","Fetching data...",false,false);
        loading.setCancelable(true);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.tag_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);

                //Displaying our grid

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
                stringMap.put("BookedId",BookedId);
                stringMap.put("id",UserId);

                //Log.d("pupu", "pupu:"+ProductId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(mContext).add(stringRequest);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) bContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    private void inflateList(final View p){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(bContext, "Please wait...","Fetching data...",false,false);
        loading.setCancelable(true);
        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.get_tags, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("tata", s);

                //Displaying our grid
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray= object.getJSONArray("result");


                    final ArrayList<String> list = new ArrayList<String>();

                    for(int i = 0; i<jsonArray.length(); i++){

                        //Creating a json object of the current index
                        JSONObject obj = null;
                        try {
                            //getting json object from current index
                            obj = jsonArray.getJSONObject(i);

                            list.add(obj.getString("email"));
                            //getting image url and title from json object
                          /*  images.add(obj.getString(TAG_IMAGE_URL));
                            ProductTypes.add(obj.getString(TAG_PRODUCT_TYPE));
                            ProductMakes.add(obj.getString(TAG_PRODUCT_MAKE));
                            ProductColours.add(obj.getString(TAG_PRODUCT_COLOUR));
                            Prices.add(obj.getString(TAG_PRICE));
                            OtherDescriptions.add(obj.getString(TAG_OTHER_DESCRIPTIONS));
                            ids.add(obj.getString(TAG_ID));
                            ProductIds.add(obj.getString(TAG_PRODUCT_ID));
                            Periods.add(obj.getString(TAG_PERIOD));*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(list.size()==0)
                    {
                        showMessage("No tags yet","We may not have anyone that has shown interest in this post yet");
                    }
                    else {popPage(p,list);}



                    //populateSearch(array);

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

                // String dodo="80";
                params.put("id",UserId);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(bContext);
        //Adding our request to the queue
        requestQueue.add(stringRequest);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void popPage(View v,ArrayList<String> list) {
        android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(bContext);
        builder2.setView(v);
        final ListView listview = (ListView) v.findViewById(R.id.listview);
        // final ArrayList<String> list = new ArrayList<String>();

        /*list.add("Amy");
        list.add("Bobby");
        list.add("Silas");*/




        final StableArrayAdapter adapter = new StableArrayAdapter(bContext,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                String val= ((TextView) view).getText().toString();
                boolean net= isNetworkConnected();
                if(net!=true)
                {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
                else{Intent o =new Intent(bContext,ProfActivity.class);
                    o.putExtra("TaggerEmail",val);
                    bContext.startActivity(o);}



            }

        });


        builder2.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");



        alertDialog2 = builder2.create();
        alertDialog2.show();



    }




    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }





}