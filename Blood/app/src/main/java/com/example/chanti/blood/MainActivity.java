package com.example.chanti.blood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    String cityTxt;
    String bloodGroupTxt;
    ImageButton searchBtn;
    ArrayList<String> mobileNumberList = new ArrayList<>();
    final ArrayList<HashMap<String, String>> donorsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
//        SimpleAdapter adaptor = new SimpleAdapter(this, donorsList, R.layout.activity_listview, new String[]{"userName", "address", "blood_group"}, new int[]{R.id.donorName, R.id.donorAddress, R.id.donorBloodGroup});
        ListView listView = (ListView) findViewById(R.id.bloodDonorsList);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        if (item.getItemId() == R.id.action_logout) {
            Intent s = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(s);
        }

        if(item.getItemId() == R.id.action_profile_view) {
            Intent p = new Intent(MainActivity.this, ProfileViewActivity.class);
            startActivity(p);
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchDonors(View v) {
        cityTxt = ((EditText) findViewById(R.id.city)).getText().toString();
        bloodGroupTxt = ((Spinner) findViewById(R.id.spinnerBloodGroup)).getSelectedItem().toString();
        searchBtn = (ImageButton) findViewById(R.id.search);

        if (v.getId() == R.id.search) {
            final Firebase ref = new Firebase("https://bloodmanagement.firebaseio.com/Users");
            final Query donorQuery = ref.orderByChild("city").equalTo(cityTxt.toLowerCase().trim());
            //Query donor = donorList.orderByChild("blood_group").equalTo(bloodGroupTxt);
            donorQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot blood : dataSnapshot.getChildren()) {
                        String name;
                        if (blood.child("blood_group").getValue().equals(bloodGroupTxt)) {
                            HashMap<String, String> m = new HashMap<>();

                            name = blood.child("first_name").getValue().toString() + " " + blood.child("last_name").getValue().toString();
                            m.put("userName", name);
                            m.put("address", blood.child("address").getValue().toString());
                            m.put("blood_group", blood.child("blood_group").getValue().toString());
                            mobileNumberList.add(blood.child("mobile").getValue().toString());
                            donorsList.add(m);
                        }
                    }
                    Log.d("data", donorsList.toString());

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }

    public class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return donorsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.activity_listview, parent,false);
                holder = new ViewHolder();
                holder.donorName = (TextView) convertView.findViewById(R.id.donorName);
                holder.donorAddress = (TextView) convertView.findViewById(R.id.donorAddress);
                holder.donorBloodGroup = (TextView) convertView.findViewById(R.id.donorBloodGroup);
                holder.callButton = (ImageButton) convertView.findViewById(R.id.call);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            HashMap<String,String> map = donorsList.get(position);
            holder.donorName.setText(map.get("userName"));
            holder.donorAddress.setText(map.get("address"));
            holder.donorBloodGroup.setText(map.get("blood_group"));
            holder.callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /** call function goes here
                     * use map to get the current position data
                     **/
                    Intent callDonor = new Intent(Intent.ACTION_DIAL);
                    callDonor.setData(Uri.parse("tel:" + mobileNumberList.get(position)));
                    startActivity(callDonor);
                }
            });
            return convertView;
        }

        class ViewHolder{
            TextView donorName,donorAddress,donorBloodGroup;
            ImageButton callButton;
        }

    }
}
