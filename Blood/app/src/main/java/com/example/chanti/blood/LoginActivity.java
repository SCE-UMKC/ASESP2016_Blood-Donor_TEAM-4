package com.example.chanti.blood;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * Created by chanti on 05-Mar-16.
 */
public class LoginActivity extends Activity {
    String loginTxt, passwordTxt;
    Button loginBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Firebase.setAndroidContext(this);
    }

    public void onClickLogin(View v) {
        loginBt = (Button) findViewById(R.id.loginBtn);
        if (v.getId() == R.id.loginBtn) {
            loginTxt = ((EditText) findViewById(R.id.editUserName)).getText().toString();
            passwordTxt = ((EditText) findViewById(R.id.editPassword)).getText().toString();
            final Firebase ref = new Firebase("https://bloodmanagement.firebaseio.com/Users");
            Log.d("username", loginTxt);
            Query user = ref.orderByChild("username").equalTo(loginTxt);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(loginTxt)){
                        Object q = dataSnapshot.child(loginTxt).child("password").getChildren();
                        Log.d("da", q.toString());
                    }
                    String data = dataSnapshot.child(loginTxt).toString();

                    Log.d("data", data.toString());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
}
