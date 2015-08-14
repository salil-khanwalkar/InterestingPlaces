package net.atos.interestingplaces.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import net.atos.interestingplaces.R;

public class SplashActivity extends ActionBarActivity {
    /**
     * Time out for splash screen
     */
    private final static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
        finish();
    }

    public void initViews() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.startActivity(new Intent(
                        SplashActivity.this, PlacesList.class));
            }
        }, SPLASH_TIME_OUT);

    }
}
