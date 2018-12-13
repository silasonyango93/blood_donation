package com.example.silasonyango.ewe;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SilasOnyango on 8/24/2017.
 */
public class UpdateProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText FullNames,BloodGroup,PhoneNumber,City,ResidentialAddress;
    Button Submit,Cancel;
    DatabaseHelper myDb;
    String UserId,UserName,Addr,Genderstring,Countystring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        myDb = new DatabaseHelper(this);
        viewSpecificData();
        FullNames=(EditText)findViewById(R.id.et_full_names);
        BloodGroup=(EditText)findViewById(R.id.et_blood_group);
        PhoneNumber=(EditText)findViewById(R.id.et_phone_number);
        City=(EditText)findViewById(R.id.et_city);
        ResidentialAddress=(EditText)findViewById(R.id.et_residential_address);

        Submit=(Button)findViewById(R.id.bt_submit);
        Cancel=(Button)findViewById(R.id.bt_cancel);



        // GENDER SPINNER**************************************************************************************
        Spinner gender_spinner = (Spinner) findViewById(R.id.gender_spinner);

        // Spinner click listener
        gender_spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        final List<String> gender = new ArrayList<String>();
        gender.add("Select gender");
        gender.add("Male");
        gender.add("Female");



        // Creating adapter for spinner
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);

        // Drop down layout style - list view with radio button
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender_spinner.setAdapter(genderAdapter);


        //COUMTY SPINNER*******************************************************************************************************

        // Spinner element
        Spinner county_spinner = (Spinner) findViewById(R.id.county_spinner);

        // Spinner click listener
        county_spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        final List<String> county = new ArrayList<String>();
        county.add("Select county");
        county.add("Nairobi");
        county.add("Kisumu");
        county.add("Mombasa");
        county.add("Kiambu");
        county.add("Nakuru");
        county.add("Homa Bay");
        county.add("Uasin Gishu");
        county.add("Kakamega");
        county.add("Siaya");
        county.add("Kisii");
        county.add("Tharaka Nithi");
        county.add("Nyeri");
        county.add("Embu");
        county.add("Narok");
        county.add("Machakos");


        // Creating adapter for spinner
        ArrayAdapter<String> countyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, county);

        // Drop down layout style - list view with radio button
        countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        county_spinner.setAdapter(countyAdapter);




        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strFullNames = FullNames.getText().toString().trim();
                String strBloodGroup = BloodGroup.getText().toString().trim();
                String strPhoneNumber =  PhoneNumber.getText().toString().trim();
                String strCity = City.getText().toString().trim();
                String strResidentialAddress = ResidentialAddress.getText().toString().trim();
                String Gender_comparison="Select gender";
                String County_comparison="Select county";
                if(Genderstring.equals(Gender_comparison))
                {
                    Toast.makeText(getApplicationContext(),
                            "Kindly select your gender!", Toast.LENGTH_LONG)
                            .show();
                }
                else if(Countystring.equals(County_comparison))
                {
                    Toast.makeText(getApplicationContext(),
                            "Kindly select county!", Toast.LENGTH_LONG)
                            .show();
                }

                else if (strFullNames.isEmpty() && strBloodGroup.isEmpty() && strPhoneNumber.isEmpty()&& strCity.isEmpty()&& strResidentialAddress.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Kindly fill every field on the form!", Toast.LENGTH_LONG)
                            .show();
                    // registerUser(name, email, password);
                } else {


                    Intent intent= new Intent(UpdateProfile.this,Landing.class);
                    upload(strFullNames,Genderstring,strBloodGroup,strPhoneNumber,Countystring,strCity,strResidentialAddress);
                    intent.putExtra("access","signin");
                    startActivity(intent);
                }


            }



        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        Genderstring = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + Genderstring, Toast.LENGTH_LONG).show();

        // On selecting a spinner item
        Countystring = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + Countystring, Toast.LENGTH_LONG).show();



    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(getBaseContext(),"Nothing selected", Toast.LENGTH_LONG).show();
    }

    private void upload(final String fullnames, final String gender, final String bloodgroup, final String phonenumber, final String county, final String city, final String residentialaddress){
        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);

        //Creating a json array request to get the json from our api

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.update_profile_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                loading.dismiss();
                Log.d("responce", s);

                //Displaying our grid
              /*  try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array= object.getJSONArray("result");


                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
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
                stringMap.put("FullNames",fullnames);
                stringMap.put("Gender",gender);
                stringMap.put("BloodGroup",bloodgroup);
                stringMap.put("PhoneNumber",phonenumber);
                stringMap.put("County",county);
                stringMap.put("City",city);
                stringMap.put("ResidentialAddress",residentialaddress);

                return stringMap;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
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
}
