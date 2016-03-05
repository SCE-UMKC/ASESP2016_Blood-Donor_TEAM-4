package com.example.chanti.blood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Ravi-PC on 27-02-2016.
 **/
public class SplashActivity extends Activity {

    Button login, register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    public void onClickRegister(View v)
    {
        register = (Button) findViewById(R.id.register);
        if (v.getId() == R.id.register)
        {
            Intent r = new Intent(SplashActivity.this, RegistrationActivity.class);
            startActivity(r);
        }
    }

}
