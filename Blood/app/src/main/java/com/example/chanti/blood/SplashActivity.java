package com.example.chanti.blood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Ravi-PC on 27-02-2016.
 **/
public class SplashActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        Intent r = null;
        switch (v.getId()){
            case R.id.register:
                r = new Intent(SplashActivity.this, RegistrationActivity.class);
                break;
            case R.id.login:
                r = new Intent(SplashActivity.this, LoginActivity.class);
                break;
            default:
                break;
        }
        if(null != r){
            startActivity(r);
            finish();
        }
    }
}
