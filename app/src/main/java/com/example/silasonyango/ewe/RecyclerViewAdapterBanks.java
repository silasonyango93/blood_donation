package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by SilasOnyango on 9/9/2017.
 */
public class RecyclerViewAdapterBanks extends RecyclerView.Adapter<RecyclerViewHolderBank> {
    private Context context;
    //Imageloader to load images
    private ImageLoader imageLoader,imageLoader2;
    public NetworkImageView networkImageView;

    private ArrayList<String> BloodBankNames;
    private ArrayList<String> Locations;
    private ArrayList<String> BloodBankIds;
    private ArrayList<String> Emails;

    public RecyclerViewAdapterBanks(Context context, ArrayList<String> BloodBankNames, ArrayList<String> Locations, ArrayList<String>  BloodBankIds,  ArrayList<String> Emails) {
        // this.itemList = itemList;




        this.context = context;
        this.BloodBankNames = BloodBankNames;
        this.Locations = Locations;
        this.BloodBankIds = BloodBankIds;
        this.Emails = Emails;




    }

    @Override
    public RecyclerViewHolderBank onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(context).inflate(R.layout.bank_card,null);
        RecyclerViewHolderBank rcv = new RecyclerViewHolderBank(layoutView);

        return rcv;
    }



    @Override
    public void onBindViewHolder(RecyclerViewHolderBank holder, int position) {
        holder.tvBloodBankName.setText(BloodBankNames.get(position));
        holder.tvLocation.setText(Locations.get(position));
        holder.tvEmail.setText(Emails.get(position));
        holder.BloodBankId=(BloodBankIds.get(position));



    }

    @Override
    public int getItemCount() {
        int s;
        s=this.BloodBankNames.size();


        Log.d("sisi", String.valueOf(s));

        return this.BloodBankNames.size();
    }

 /*   @Override
    public Object getItem(int position) {
        return images.get(position);
    }*/

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}
}



