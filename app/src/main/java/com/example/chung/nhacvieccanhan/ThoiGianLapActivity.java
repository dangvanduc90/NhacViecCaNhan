package com.example.chung.nhacvieccanhan;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.model.TestService;

public class ThoiGianLapActivity extends AppCompatActivity {

    private ServiceConnection serviceConnection;
    private boolean isConnected = false;
    private TestService testService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoi_gian_lap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        connectService();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    private void connectService() {
        Intent intent = new Intent(this, TestService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                TestService.MyBinder myBinder = (TestService.MyBinder) iBinder;
                testService = myBinder.getService();
                isConnected = true;
                int rs = testService.add(3, 4);
                Toast.makeText(ThoiGianLapActivity.this, "" + rs, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                testService = null;
                isConnected = false;
                Toast.makeText(ThoiGianLapActivity.this, "" + isConnected, Toast.LENGTH_SHORT).show();
            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }
}
