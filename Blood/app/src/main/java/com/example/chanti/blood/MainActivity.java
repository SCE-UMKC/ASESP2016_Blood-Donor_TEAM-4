package com.example.chanti.blood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    String cityTxt;
    String bloodGroupTxt;
    ImageButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

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

    public void searchDonors(View v){
        cityTxt = ((EditText) findViewById(R.id.city)).getText().toString();
        bloodGroupTxt = ((Spinner) findViewById(R.id.spinnerBloodGroup)).getSelectedItem().toString();
        searchBtn = (ImageButton) findViewById(R.id.search);

        if(v.getId() == R.id.search)
        {
            final Firebase ref = new Firebase("https://bloodmanagement.firebaseio.com/Users");
            final Query donorList = ref.orderByChild("address").equalTo(cityTxt);
            Query donor = donorList.orderByChild("blood_group").equalTo(bloodGroupTxt);
            donor.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d("data", dataSnapshot.toString());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
    }
}
