package com.example.silasonyango.ewe;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hp on 3/2/2016.
 */
public class Landing extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Handler mHandler;
    final Handler handler = new Handler();

    public int Year,Month,Day,Hour,Minute,Seconds;
    AlertDialog alertDialog;
    Button btContinueBook,btCancelBook,btYesEighteen,btNoEighteen,btNoDonation,btYesDonation,btMale,btFemale,btYesMenstruation,btNoMenstruation;
    LayoutInflater inflater;
    private static final String TAG = "MyFirebaseInsIDService";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String ImageUrl;
    String access;
    DatabaseHelper myDb;
    NavigationView navigationView;
    DrawerLayout drawer;
   // String DATA_URL="http://192.168.43.118/PhotoUpload/getProfPic.php";
    NetworkImageView profPic;
    //Bitmap to get image from gallery
    private Bitmap bitmap;

    public static String UserId,UserName,Addr;
    //Uri to store the image uri
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;
    TextView profText,tvAddrr;
    public static Context mContext;
    //Imageloader to load images
    public ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        this.mContext = getBaseContext();
        myDb = new DatabaseHelper(this);
        viewSpecificData();

        mHandler= new Handler();

       boolean net= isNetworkConnected();
        if(net!=true)
        {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
        else{getProfPic();}

        inflater=this.getLayoutInflater();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        NavigationView navigationView1=(NavigationView) findViewById(R.id.nav_view);
        View v=navigationView1.getHeaderView(0);
        profText=(TextView) v.findViewById(R.id.name);
        profPic=(NetworkImageView) v.findViewById(R.id.ProfPic) ;
        tvAddrr=(TextView) v.findViewById(R.id.adrr);

        DrawerLayout drawer2=(DrawerLayout) findViewById(R.id.drawer_layout);
        drawer2.openDrawer(Gravity.LEFT);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean net= isNetworkConnected();
                if(net!=true)
                {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
                else {

                    View v = inflater.inflate(R.layout.book_preview, null);

                    btContinueBook = (Button) v.findViewById(R.id.btn_continue);
                    btCancelBook = (Button) v.findViewById(R.id.btn_cancel_book);
                    btContinueBook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.cancel();

                            boolean net = isNetworkConnected();
                            if (net != true) {
                                showMessage("No internet connectivity", "Check your internet connectivity and try again.");
                            } else {
                                prepEighteen();
                            }
                            //getDate();

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
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        access=getIntent().getStringExtra("access");

        String comp="signin";

        if(access.equals(comp))
        {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "New Token: " + refreshedToken);
            sendToken(refreshedToken);
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Donors(), "DONORS");
        adapter.addFragment(new Booked(), "TAGGED");
        adapter.addFragment(new ProfileView(), "PROFILE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           // myDb.logOut();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent i= new Intent(Landing.this,BloodBanks.class);
                startActivity(i);}

        } else if (id == R.id.nav_update_profile) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent i= new Intent(Landing.this,UpdateProfile.class);
                startActivity(i);}

        } else if (id == R.id.logout) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent o =new Intent(Landing.this,SignIn.class);
                startActivity(o);}

        } else if (id == R.id.a) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent o =new Intent(Landing.this,SearchResults.class);
                o.putExtra("BloodGroup","A+");
                startActivity(o);}

        }else if (id == R.id.aminus) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent o =new Intent(Landing.this,SearchResults.class);
                o.putExtra("BloodGroup","A-");
                startActivity(o);}

        } else if (id == R.id.ab) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent o =new Intent(Landing.this,SearchResults.class);
                o.putExtra("BloodGroup","AB+");
                startActivity(o);}

        } else if (id == R.id.abminus) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent o =new Intent(Landing.this,SearchResults.class);
                o.putExtra("BloodGroup","AB-");
                startActivity(o);}

        } else if (id == R.id.b) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent o =new Intent(Landing.this,SearchResults.class);
                o.putExtra("BloodGroup","B+");
                startActivity(o);}

        } else if (id == R.id.bminus) {
            Intent o =new Intent(Landing.this,SearchResults.class);
            o.putExtra("BloodGroup","B-");
            startActivity(o);
        }else if (id == R.id.o) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent o =new Intent(Landing.this,SearchResults.class);
                o.putExtra("BloodGroup","O+");
                startActivity(o);}

        }else if (id == R.id.ominus) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent o =new Intent(Landing.this,SearchResults.class);
                o.putExtra("BloodGroup","O-");
                startActivity(o);}

        }else if (id == R.id.nav_donor) {
            boolean net= isNetworkConnected();
            if(net!=true)
            {showMessage("No internet connectivity", "Check your internet connectivity and try again.");}
            else{Intent o =new Intent(Landing.this,DonorActivity.class);

                startActivity(o);}

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getProfPic(){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.Prof_Pic_Url, new Response.Listener<String>() {
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("id",UserId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(getBaseContext()).add(stringRequest);
    }



    private void showGrid(JSONArray jsonArray){
        //Looping through all the elements of json array

        for(int i = 0; i<jsonArray.length(); i++){
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);

                ImageUrl= obj.getString("url");
                profText.setText(UserName);
                tvAddrr.setText(Addr);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

       // showMessage("url",ImageUrl);
        imageLoader = CustomVolleyRequest.getInstance(getBaseContext()).getImageLoader();
        imageLoader.get(ImageUrl, ImageLoader.getImageListener(profPic, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        profPic.setImageUrl(ImageUrl,imageLoader);
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void myCustomErrorMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap, 600, 500, true);
                //  imageView.setImageBitmap(bitmap);
                getStringImage(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public void uploadMultipart() {
        //getting name for the image
        //String id = editText.getText().toString().trim();
       /* String carmake = car_make.getText().toString().trim();
        String carmodel = car_model.getText().toString().trim();
        String enginecapacity = engine_capacity.getText().toString().trim();
        String enginecc = engine_cc.getText().toString().trim();
        String drivetype = drive_type.getText().toString().trim();
        String drivesetup = drive_setup.getText().toString().trim();
        String fueltype = fuel_type.getText().toString().trim();
        String color = colour.getText().toString().trim();*/

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId,Config.change_prof_pic_url)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("id",UserId)
                    //.addParameter("name",name)//Adding text parameter to the request
                  /*  .addParameter("car_make",carmake)
                    .addParameter("car_model",carmodel)
                    .addParameter("engine_capacity",enginecapacity)
                    .addParameter("engine_cc",enginecc)
                    .addParameter("drive_type",drivetype)
                    .addParameter("drive_setup",drivesetup)
                    .addParameter("fuel_type",fueltype)
                    .addParameter("colour",color)*/
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload


        }

        catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

        finish();
        startActivity(getIntent());

    }

    public void viewSpecificData() {

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
            buffer.append("Email : " + res.getString(3) + "\n");
            buffer.append("Key : " + res.getString(4) + "\n");
           // buffer.append("Address : " + res.getString(5) + "\n\n");

            UserId=res.getString(1);
            UserName=res.getString(2);
            Addr=res.getString(3);

            //Addrr.setText(Addr);
        }

        //Show all data


        //showMessage("Data", buffer.toString());

        //Addrr.setText(Addr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        // builder.setView(R.layout.activity_main);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void previewUpload(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setView(v);

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog = builder.create();
        alertDialog.show();





    }

    public void getDate()
    {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        //txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        Year=year;
                        Month=(monthOfYear + 1);
                        Day=dayOfMonth;
                        getTime();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void getTime()
    {


            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                           // txtTime.setText(hourOfDay + ":" + minute);

                            Hour=hourOfDay;
                            Minute=minute;
                            Seconds=50;

                           // Toast.makeText(Landing.this,Year + "-" + (Month + 1) + "-" + Day+ " "+Hour + ":" + Minute+ ":" +Seconds, Toast.LENGTH_LONG).show();
                            String time=Year + "-" + (Month + 1) + "-" + Day+ " "+Hour + ":" + Minute+ ":" +Seconds;
                            book(time);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

    }


        private void book(final String time){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.book_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Toast.makeText(Landing.this,"You succesfully booked an appointment.You shall be notified.", Toast.LENGTH_LONG).show();
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
                stringMap.put("id",UserId);
                stringMap.put("BookedTime",time);

                return stringMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {

        final String image = getStringImage(bitmap);
        class UploadImage extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Landing.this, "Please wait...", "uploading", false, false);
                Log.d("tati","Hii"+image);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                finish();
                startActivity(getIntent());
                Toast.makeText(Landing.this, s, Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> param = new HashMap<String, String>();

                param.put("image", image);
                param.put("id", UserId);
                Log.d("tate","Hii"+image);
                Log.d("tamu","Hii"+UserId);

                String result = rh.sendPostRequest(Config.change_prof_pic_url, param);
                return result;


            }
        }
        UploadImage u = new UploadImage();
        u.execute();
    }

   public void prepEighteen()
   {
       View v= inflater.inflate(R.layout.eighteen, null);

       btYesEighteen = (Button) v.findViewById(R.id.btn_yes_eighteen);
       btNoEighteen = (Button) v.findViewById(R.id.btn_no_eighteen);
       btYesEighteen.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               alertDialog.cancel();
               prepLastDonation();
           }
       });

       btNoEighteen.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               alertDialog.cancel();

               showMessage("Sorry","You may not be allowed to donate until you are at least 18 years of age.");
           }
       });
       eighteenPop(v);
   }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void eighteenPop(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setView(v);

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog = builder.create();
        alertDialog.show();





    }

    public void prepLastDonation()
    {
        View v= inflater.inflate(R.layout.last_donation, null);

        btYesDonation = (Button) v.findViewById(R.id.btn_yes_donation);
        btNoDonation = (Button) v.findViewById(R.id.btn_no_donation);
        btYesDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                showMessage("Kindly","You may not be allowed to donate if you already recently donated within the last 3 months.");
            }
        });

        btNoDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                prepGender();

            }
        });
        donationPop(v);
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void donationPop(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setView(v);

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog = builder.create();
        alertDialog.show();





    }


    public void prepGender()
    {
        View v= inflater.inflate(R.layout.gender, null);

        btMale = (Button) v.findViewById(R.id.btn_male);
        btFemale = (Button) v.findViewById(R.id.btn_female);
        btMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                for(int i=0;i<2;i++) {
                    Toast.makeText(getBaseContext(), "You have been cleared to be ready for donation.You may proceed with the appointment booking.", Toast.LENGTH_LONG).show();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDate();
                    }
                }, 4000);

            }
        });

        btFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                prepMenstrual();

            }
        });
        genderPop(v);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void genderPop(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setView(v);

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog = builder.create();
        alertDialog.show();





    }

    public void prepMenstrual()
    {
        View v= inflater.inflate(R.layout.menstruation, null);

        btYesMenstruation = (Button) v.findViewById(R.id.btn_yes_menstrual);
        btNoMenstruation = (Button) v.findViewById(R.id.btn_no_menstrual);
        btYesMenstruation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                showMessage("Kindly","You may not be allowed to donate while still undergoing your menstrual period.");
            }
        });

        btNoMenstruation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                for(int i=0;i<2;i++) {
                    Toast.makeText(getBaseContext(), "You have been cleared to be ready for donation.You may proceed with the appointment booking.", Toast.LENGTH_LONG).show();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDate();
                    }
                }, 4000);



            }
        });
        menstrualPop(v);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void menstrualPop(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setView(v);

        builder.setCancelable(true);
        // builder.setTitle("Submit verification code");

        //editText.setText("test label");
        alertDialog = builder.create();
        alertDialog.show();





    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void sendToken(final String refreshedToken){
        //Showing a progress dialog while our app fetches the data from url


        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.update_token, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


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



                stringMap.put("token_value",refreshedToken);
                stringMap.put("UserId",UserId);


                //Log.d("pupu", "pupu:"+ProductId);

                return stringMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }







    //===============================================================================================








}
