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
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SilasOnyango on 9/9/2017.
 */
public class RecyclerViewHolderBank extends RecyclerView.ViewHolder implements View.OnClickListener {

    AlertDialog.Builder builder;
android.app.AlertDialog alertDialog;
    public Context mContext;
    CheckBox checkBook, checkBook1;
    NetworkImageView largeImage;
    public String UserId,BookedId,LargeImageUrl,BloodBankId;
    public ImageLoader imageLoader, imageLoader1;
    public Context bContext;
    public NetworkImageView ProfilePhoto;
    TextView tvBloodBankName,tvLocation,tvEmail;
    Button btContinueBook,btCancelBook,btYesEighteen,btNoEighteen,btNoDonation,btYesDonation,btMale,btFemale,btYesMenstruation,btNoMenstruation;
    LayoutInflater inflater, inflater1;

    public RecyclerViewHolderBank(View itemView) {
        super(itemView);
        UserId=BloodBanks.UserId;
        mContext = BloodBanks.mcontext;
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        inflater1 = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        itemView.setOnClickListener(this);

        tvBloodBankName = (TextView) itemView.findViewById(R.id.tv_bank_name);
        tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
        tvEmail = (TextView) itemView.findViewById(R.id.tv_email);

    }

    @Override
    public void onClick(View view) {


        View v= inflater.inflate(R.layout.request_preview, null);

        btContinueBook = (Button) v.findViewById(R.id.btn_continue);
        btCancelBook = (Button) v.findViewById(R.id.btn_cancel_book);
        btContinueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                makeBloodRequest(mContext);

            }
        });

        btCancelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        previewUpload(v);
    }



    public void makeBloodRequest(final Context context){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(context, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.make_blood_request, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);


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
                stringMap.put("BloodBankId",BloodBankId);
                stringMap.put("id",UserId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }





    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(bContext);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void previewUpload(View v) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);



        builder.setView(v);

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog = builder.create();
        alertDialog.show();





    }


}