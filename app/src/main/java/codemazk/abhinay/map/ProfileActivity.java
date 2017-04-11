package codemazk.abhinay.map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView name,mob,email,addre;
    Button park,view;
        String id,nam,emai,mobile,carname,carnumber,date,time;

    private SQLiteDatabase dataBase;

    GPSTracker gps;
    double longitude,latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
         id = userDetails.getString("id", "");
         nam = userDetails.getString("name", "");
         emai = userDetails.getString("email", "");
         mobile = userDetails.getString("mobile", "");

        gps = new GPSTracker(ProfileActivity.this);
        location();
        name=(TextView)findViewById(R.id.name);
        mob=(TextView)findViewById(R.id.mobile);
        email=(TextView)findViewById(R.id.email);

        park=(Button) findViewById(R.id.park);
        view=(Button) findViewById(R.id.Getpark);
        view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent n=new Intent(ProfileActivity.this,listActivity.class);
        startActivity(n);
    }
});
        name.setText(nam);
        mob.setText(mobile);
        email.setText(emai);


        park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location();
                AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                final EditText edittext = new EditText(ProfileActivity.this);
                alert.setTitle("Car Details");
                alert.setMessage("Enter Your Car Name");


                alert.setView(edittext);

                alert.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                      //  Editable YouEditTextValue = edittext.getText();
                        //OR
                         carname= edittext.getText().toString();

                            if(carname.isEmpty()){
                                Toast.makeText(getApplicationContext(),"Enter the car name",Toast.LENGTH_LONG).show();
                            }else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                                final EditText edittext = new EditText(ProfileActivity.this);
                                alert.setTitle("Car Details");
                                alert.setMessage("Enter Your car Number (eg : KL-11-CA-1111 or KL11CA1111 )");


                                alert.setView(edittext);

                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //What ever you want to do with the value
                                        // Editable YouEditTextValue = edittext.getText();
                                        //OR
                                        Calendar c = Calendar.getInstance();
                                        String carnuberpatern = "(([A-Za-z]){2,3}(|-)(?:[0-9]){1,2}(|-)(?:[A-Za-z]){2}(|-)([0-9]){1,4})|(([A-Za-z]){2,3}(|-)([0-9]){1,4})";
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        date = df.format(c.getTime());
                                        Log.e("day", date);
                                        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                                        Log.e("time", tf.format(c.getTime()).toString());
                                        time = tf.format(c.getTime()).toString();


                                        carnumber = edittext.getText().toString().trim();
                                        if (carnumber.isEmpty()) {
                                            Toast.makeText(getApplicationContext(), "Enter the car number", Toast.LENGTH_LONG).show();
                                        } else if (!carnumber.matches(carnuberpatern)) {
                                            Toast.makeText(getApplicationContext(), "Please Enter the valid car number"+carnumber, Toast.LENGTH_LONG).show();
                                        } else {
                                            new psrk().execute();
                                        }
                                    }
                                });


                                alert.show();
                            }
                    }
                });



                alert.show();
            }
        });



    }
        void location(){
            // check if GPS enabled
            if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                     longitude   = gps.getLongitude();

                     // \n is for new line
          //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
             }

        }
    public class psrk extends AsyncTask<String, String, String> {

        String Json;
        String Results;
        ProgressDialog pg;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            readfromserver jsonParser = new readfromserver();
            List<NameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new BasicNameValuePair("rid", id.toString()));
           nameValuePairs.add(new BasicNameValuePair("carname", carname));
           nameValuePairs.add(new BasicNameValuePair("carnumber",carnumber));
            nameValuePairs.add(new BasicNameValuePair("lati",String.valueOf(latitude)));
            nameValuePairs.add(new BasicNameValuePair("long",String.valueOf(longitude)));
            nameValuePairs.add(new BasicNameValuePair("time",time));
            nameValuePairs.add(new BasicNameValuePair("date",date));



            Json = jsonParser.makeServiceCall("http://project.codemazk.com/scms/park/park.php", readfromserver.GET, nameValuePairs);
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            pg = new ProgressDialog(ProfileActivity.this);
            pg.setTitle("Please Wait");
            pg.setMessage("parking...");
            pg.setCancelable(false);
            pg.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (Json != null) {
                // try {
                Log.e("json",Json);
                if(Json.contains("successfully parked")){
                    Toast.makeText(getApplicationContext(), "successfully parked", Toast.LENGTH_SHORT).show();
                }else if(Json.contains("car already exist or parked")){
                    Toast.makeText(getApplicationContext(), "car already added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                //  JSONObject jsonObj = new JSONObject(Json);
                //  Results = jsonObj.getString("userid");


             /*   } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            pg.dismiss();
            super.onPostExecute(result);
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
