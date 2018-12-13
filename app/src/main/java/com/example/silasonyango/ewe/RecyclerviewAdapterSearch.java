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
public class RecyclerviewAdapterSearch extends RecyclerView.Adapter<RecyclerViewHoldersSearch> {
    private Context context;
    //Imageloader to load images
    private ImageLoader imageLoader,imageLoader2;
    public NetworkImageView networkImageView;

    private ArrayList<String> FullNames;
    private ArrayList<String> Cities;
    private ArrayList<String> ids;

    private ArrayList<String> Periods;

    public RecyclerviewAdapterSearch(Context context, ArrayList<String> FullNames, ArrayList<String> Cities, ArrayList<String>  ids, ArrayList<String> Periods) {
        // this.itemList = itemList;




        this.context = context;
        this.FullNames = FullNames;
        this.Cities = Cities;
        this.ids = ids;

        this.Periods = Periods;



    }

    @Override
    public RecyclerViewHoldersSearch onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(context).inflate(R.layout.donors_card,null);
        RecyclerViewHoldersSearch rcv = new RecyclerViewHoldersSearch(layoutView);

        return rcv;
    }



    @Override
    public void onBindViewHolder(RecyclerViewHoldersSearch holder, int position) {
        holder.tvName.setText(FullNames.get(position));
        holder.tvCity.setText(Cities.get(position));
        holder.tvPostedDate.setText(Periods.get(position));
        holder.BookedId=(ids.get(position));



    }

    @Override
    public int getItemCount() {
int s;
        s=this.FullNames.size();


        Log.d("sisi", String.valueOf(s));

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



