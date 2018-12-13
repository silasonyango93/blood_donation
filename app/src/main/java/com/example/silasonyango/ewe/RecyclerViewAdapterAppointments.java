package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by SilasOnyango on 9/9/2017.
 */
public class RecyclerViewAdapterAppointments extends RecyclerView.Adapter<RecyclerViewHoldersAppointments> {
    private Context context;
    //Imageloader to load images
    private ImageLoader imageLoader,imageLoader2;
    public NetworkImageView networkImageView;

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

    public RecyclerViewAdapterAppointments(Context context, ArrayList<String> FullNames, ArrayList<String> Genders, ArrayList<String> BloodGroups, ArrayList<String> PhoneNumbers, ArrayList<String> Counties, ArrayList<String> Cities, ArrayList<String>  ids, ArrayList<String> ResidentialAddresses, ArrayList<String> BookedTimes, ArrayList<String> emails, ArrayList<String> Periods) {
        // this.itemList = itemList;
        this.context = context;


        this.FullNames = FullNames;
        this.Genders = Genders;
        this.BloodGroups = BloodGroups;
        this.PhoneNumbers = PhoneNumbers;
        this.Counties = Counties;
        this.Cities = Cities;
        this.ids = ids;
        this.ResidentialAddresses = ResidentialAddresses;
        this.BookedTimes = BookedTimes;
        this.emails = emails;

        this.Periods = Periods;



    }

    @Override
    public RecyclerViewHoldersAppointments onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(context).inflate(R.layout.donors_card,null);
        RecyclerViewHoldersAppointments rcv = new RecyclerViewHoldersAppointments(layoutView);

        return rcv;
    }



    @Override
    public void onBindViewHolder(RecyclerViewHoldersAppointments holder, int position) {
        holder.tvName.setText(FullNames.get(position));
        holder.tvCity.setText(Cities.get(position));
        holder.tvPostedDate.setText(Periods.get(position));
        holder.BookedId=(ids.get(position));



    }

    @Override
    public int getItemCount() {

        return this.FullNames.size();
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



