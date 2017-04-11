package codemazk.abhinay.map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class listActivity extends AppCompatActivity {

    String id,iid;
    ListView carList;
    private ArrayList<items> itemObj = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        SharedPreferences userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        id = userDetails.getString("id", "");
        carList=(ListView)findViewById(R.id.car);
        new GetParkedCars().execute();

    }
    public class GetParkedCars extends AsyncTask<String, String, String> {

        String Json;
        String Results;
        ProgressDialog pg;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            readfromserver jsonParser = new readfromserver();
            List<NameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new BasicNameValuePair("rid",id));





            Json = jsonParser.makeServiceCall("http://project.codemazk.com/scms/park/parked.php", readfromserver.GET, nameValuePairs);
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            pg = new ProgressDialog(listActivity.this);
            pg.setTitle("Please Wait");
            pg.setMessage("Authenticating...");
            pg.setCancelable(false);
            pg.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(Json.contains("[]")){
                Log.e("json",Json);
                Toast.makeText(getApplicationContext(),"Your are not a registered user",Toast.LENGTH_LONG).show();
            }else {
                if (Json != null) {
                    // try {
                    Log.e("json", Json);


                    try {

                        JSONArray jsonObj = new JSONArray(Json);
                        JSONObject jo;
                        for (int i = 0; i < jsonObj.length(); i++) {
                            jo = jsonObj.getJSONObject(i);

                            Log.e("id", jo.getString("_id").toString());

                            String id=jo.getString("_id").toString();
                            String carname=jo.getString("_carname").toString();
                            String carnbr=jo.getString("_carnumber").toString();
                            String lati=jo.getString("_latitude").toString();
                            String longi=jo.getString("_logitude").toString();
                            String time=jo.getString("_time").toString();
                            String date=jo.getString("_date").toString();
                            itemObj.add(new items(id.toString(), carname.toString(), carnbr.toString(), lati.toString(), time.toString(), date.toString(), longi.toString()));
                          //  public items(String id,String carname, String carnbr,String lati,String time,String date,String longi){
                        }

                        DisplayAdapter disadpt = new DisplayAdapter(listActivity.this, itemObj);

                        carList.setAdapter(disadpt);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("JSON Data", "Didn't receive any data from server!");
                }
            }
            pg.dismiss();
            super.onPostExecute(result);
        }

    }

    private class DisplayAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;



        ArrayList<items> itemobj = new ArrayList<>();
        items itemobjtemp = null;


        public DisplayAdapter(Activity activity, ArrayList<items> report) {
            this.itemobj = report;
            inflater = (LayoutInflater) activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // TODO Auto-generated constructor stub
        }
        public int getCount() {
            return itemobj.size();
        }

        public items getItem(int position) {
            return itemobj.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Holder mHolder;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.list_items, null);


                mHolder = new Holder();
                //link to widgets
                mHolder.id = (TextView) convertView.findViewById(R.id.id);
                mHolder.carname = (TextView) convertView.findViewById(R.id.carname);
                mHolder.carnumber = (TextView) convertView.findViewById(R.id.carnbr);
                mHolder.lati = (TextView) convertView.findViewById(R.id.lati);
                mHolder.longi = (TextView) convertView.findViewById(R.id.longi);
                mHolder.time = (TextView) convertView.findViewById(R.id.time);
                mHolder.date = (TextView) convertView.findViewById(R.id.date);
                mHolder.view = (Button) convertView.findViewById(R.id.view);
                mHolder.upark = (Button) convertView.findViewById(R.id.unpark);





                items io = itemobj.get(position);

               mHolder.view.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent n=new Intent(listActivity.this,MapActivity.class);
                       n.putExtra("lati", mHolder.lati.getText());
                       n.putExtra("longi", mHolder.longi.getText());
                       n.putExtra("carname", mHolder.carname.getText());
                       n.putExtra("carnbr", mHolder.carnumber.getText());
                       n.putExtra("time", mHolder.time.getText());
                       startActivity(n);
                    Toast.makeText(getApplicationContext(),mHolder.longi.getText(),Toast.LENGTH_LONG).show();
                   }
               });

                mHolder.upark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iid=mHolder.id.getText().toString();
                        carList.setAdapter(null);
                        itemobj.clear();
                       // carList.getAdapter().notifyDataSetChanged();
                       new unpark().execute();
                    }
                });
              /*  mHolder.view.ser(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // int getPosition = (Integer) view.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                        Item io = itemobj.get(position);
                        io.setA(mHolder.am.getSelectedItem().toString());

                        itemobj.set(position, io);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/

                convertView.setTag(mHolder);

            } else
                mHolder = (Holder) convertView.getTag();

            if (itemobj.size() <= 0) {
                mHolder.carname.setText("no_Data");
            } else {
                itemobjtemp = null;
                itemobjtemp = itemobj.get(position);


                mHolder.id.setText(itemobjtemp.getIdd());


                mHolder.carname.setText(itemobjtemp.getCarname());
                mHolder.carnumber.setText(itemobjtemp.getCarnbr());
                mHolder.longi.setText(itemobjtemp.getLongi());
                mHolder.lati.setText(itemobjtemp.getLati());
                mHolder.time.setText(itemobjtemp.getTime());
                mHolder.date.setText(itemobjtemp.getDate());
                Log.e("date",itemobjtemp.getDate());



            }
            return convertView;

        }



        public class Holder {

            TextView id,carname,carnumber,lati,longi,time,date;
            Button view,upark;

        }

    }

    public class unpark extends AsyncTask<String, String, String> {

        String Json;
        String Results;
        ProgressDialog pg;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            readfromserver jsonParser = new readfromserver();
            List<NameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new BasicNameValuePair("rid",id));
            nameValuePairs.add(new BasicNameValuePair("id",iid));





            Json = jsonParser.makeServiceCall("http://project.codemazk.com/scms/park/unpark.php", readfromserver.GET, nameValuePairs);
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            pg = new ProgressDialog(listActivity.this);
            pg.setTitle("Please Wait");
            pg.setMessage("Unparking...");
            pg.setCancelable(false);
            pg.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(Json.contains("[]")){
                Log.e("json",Json);

            }else {
                if (Json != null) {
                    // try {
                    Log.e("json", Json);

                    Toast.makeText(getApplicationContext(),"Unparked Successfully",Toast.LENGTH_LONG).show();
                    new GetParkedCars().execute();

                } else {
                    Log.e("JSON Data", "Didn't receive any data from server!");
                }
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
