package com.example.chanti.blood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String cityTxt;
    String bloodGroupTxt;
    ImageButton searchBtn;
    ArrayList<String> mobileNumberList = new ArrayList<String>();
    final ArrayList<HashMap<String, String>> donorsList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        SimpleAdapter adaptor = new SimpleAdapter(this, donorsList, R.layout.activity_listview, new String[]{"userName", "address", "blood_group"}, new int[]{R.id.donorName, R.id.donorAddress, R.id.donorBloodGroup});
        ListView listView = (ListView) findViewById(R.id.bloodDonorsList);
        listView.setAdapter(adaptor);
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

        return super.onOptionsItemSelected(item);
    }

    public void searchDonors(View v) {
        cityTxt = ((EditText) findViewById(R.id.city)).getText().toString();
        bloodGroupTxt = ((Spinner) findViewById(R.id.spinnerBloodGroup)).getSelectedItem().toString();
        searchBtn = (ImageButton) findViewById(R.id.search);

        if (v.getId() == R.id.search) {
            final Firebase ref = new Firebase("https://bloodmanagement.firebaseio.com/Users");
            final Query donorQuery = ref.orderByChild("address").equalTo(cityTxt);
            //Query donor = donorList.orderByChild("blood_group").equalTo(bloodGroupTxt);
            donorQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot blood : dataSnapshot.getChildren()) {
                        String name = null;
                        if (blood.child("blood_group").getValue().equals(bloodGroupTxt)) {
                            HashMap<String, String> m = new HashMap<String, String>();

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


    public void onClickCall(ListView l, View v,int position, long id ) {
        String number = mobileNumberList.get(position);
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }
}
