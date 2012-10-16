package com.waitingmyself.record;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class InitActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(InitActivity.this,MainActivity.class));
    }
}