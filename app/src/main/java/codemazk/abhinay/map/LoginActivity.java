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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView signup;


    private SQLiteDatabase dataBase;
    String emai,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = (EditText) findViewById(R.id.Lemail);
        password = (EditText) findViewById(R.id.Lpassword);
        login = (Button) findViewById(R.id.Login);
        signup = (TextView) findViewById(R.id.signup1);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emai=email.getText().toString();
                pass=password.getText().toString();
                if(emai.isEmpty()||pass.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter the empty fields",Toast.LENGTH_LONG).show();

                }else{
                    new ClsLogin().execute();
                }



            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(LoginActivity.this, RegistraionActivity.class);
                startActivity(n);

            }
        });
    }


    public class ClsLogin extends AsyncTask<String, String, String> {

        String Json;
        String Results;
        ProgressDialog pg;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            readfromserver jsonParser = new readfromserver();
            List<NameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));

            nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));



            Json = jsonParser.makeServiceCall("http://project.codemazk.com/scms/park/login.php", readfromserver.GET, nameValuePairs);
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            pg = new ProgressDialog(LoginActivity.this);
            pg.setTitle("Please Wait");
            pg.setMessage("Authenticating...");
            pg.setCancelable(false);
            pg.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            pg.dismiss();
           try {
                if (Json.contains("[]")){
                    Toast.makeText(getApplication(),"You are not registered User",Toast.LENGTH_LONG).show();
                }
                 else  if (Json != null) {
                       // try {
                       Log.e("json", Json);


                       try {

                           JSONArray jsonObj = new JSONArray(Json);
                           JSONObject jo;
                           for (int i = 0; i < jsonObj.length(); i++) {
                               jo = jsonObj.getJSONObject(i);

                               Log.e("id", jo.getString("_id").toString());
                               SharedPreferences UserDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
                               SharedPreferences.Editor edit = UserDetails.edit();
                               edit.putString("id", jo.getString("_id"));
                               edit.putString("name", jo.getString("_name"));
                               edit.putString("email", jo.getString("_email"));
                               edit.putString("mobile", jo.getString("_mobile"));

                               edit.apply();
                           }


                           Intent n = new Intent(LoginActivity.this, ProfileActivity.class);
                           startActivity(n);
                           finish();

                       } catch (JSONException e) {
                           e.printStackTrace();

                       }


                   } else {
                       Log.e("JSON Data", "Didn't receive any data from server!");
                   }


               super.onPostExecute(result);
           }
           catch (Exception e){

           }
        }


    }

}