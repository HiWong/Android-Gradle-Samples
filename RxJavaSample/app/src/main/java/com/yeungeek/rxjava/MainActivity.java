package com.yeungeek.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yeungeek.rxjava.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, new MainFragment(), this.toString())
                    .commit();
        }
    }
}
