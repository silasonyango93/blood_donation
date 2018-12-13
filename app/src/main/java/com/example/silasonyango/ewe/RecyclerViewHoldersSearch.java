package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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

/**
 * Created by SilasOnyango on 9/9/2017.
 */
public class RecyclerViewHoldersSearch extends RecyclerView.ViewHolder implements View.OnClickListener {

    AlertDialog.Builder builder;

    public Context mContext;
    CheckBox checkBook, checkBook1;
    NetworkImageView largeImage;
    public String UserId,BookedId,LargeImageUrl;
    public ImageLoader imageLoader, imageLoader1;
    public Context bContext;
    public NetworkImageView ProfilePhoto;
    TextView tvName,tvCity,tvPostedDate;
    LayoutInflater inflater, inflater1;

    public RecyclerViewHoldersSearch(View itemView) {
        super(itemView);
        mContext = SearchResults.mcontext;
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
       inflater1 = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        itemView.setOnClickListener(this);

        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvCity = (TextView) itemView.findViewById(R.id.tv_city);
        tvPostedDate = (TextView) itemView.findViewById(R.id.tv_donation_date);

    }

    @Override
    public void onClick(View view) {
        View v = inflater.inflate(R.layout.donor_detailed_info,null);



        TextView tvFullName=(TextView)v.findViewById(R.id.tv_full_names);
        TextView tvGender=(TextView)v.findViewById(R.id.tv_gender);
        TextView tvBloodGroup=(TextView)v.findViewById(R.id.tv_blood_group);
        TextView tvPhoneNumber=(TextView)v.findViewById(R.id.tv_phone_number);
        TextView tvemail=(TextView)v.findViewById(R.id.tv_email);
        TextView tvCounty=(TextView)v.findViewById(R.id.tv_county);
        TextView tvCity=(TextView)v.findViewById(R.id.tv_city);
        TextView tvResidentialAddress=(TextView)v.findViewById(R.id.tv_residential_address);
        TextView tvDonationDate=(TextView)v.findViewById(R.id.tv_donation_date);



        checkBook=(CheckBox)v.findViewById(R.id.check_book);




        bContext=view.getContext();

        getLargeImage(view.getContext(),tvFullName,tvGender,tvBloodGroup,tvPhoneNumber,tvemail,tvCounty,tvCity,tvResidentialAddress,tvDonationDate,v);
        renderLargeImage(v);
        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void renderLargeImage(View v) {
        builder = new AlertDialog.Builder(bContext);

        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        checkBook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    bookProduct(bContext);

                    alertDialog.cancel();

                }

            }
        });

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");


        alertDialog.show();
    }



    public void getLargeImage(final Context context, final TextView tvFullName, final TextView tvGender, final TextView tvBloodGroup, final TextView tvPhoneNumber, final TextView tvemail,final TextView tvCounty,final TextView tvCity,final TextView tvResidentialAddress,final TextView tvDonationDate, final View v){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(context, "Please wait...","Fetching data...",false,false);

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


}