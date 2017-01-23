package com.example.vincentbevia.networklistenerbroadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private BroadcastReceiver networkChangeReceiver;
    private TextView NetworkStatus;
    private TextView NetworkStatus2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        NetworkStatus2 = (TextView) findViewById(R.id.networkStatus2);
        NetworkStatus = (TextView)findViewById(R.id.networkStatus);
    }

    private final BroadcastReceiver networkMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(getString(R.string.network_status)).equals("CONNECTED TO WIFI")
                    || intent.getStringExtra(getString(R.string.network_status)).equals("CONNECTED TO MOBILE")) {

                //Toast.makeText(context, "Already set", Toast.LENGTH_SHORT).show();
                NetworkStatus.setText(intent.getStringExtra(getString(R.string.network_status)));

            } else {

                //Refresh web or view here!
                NetworkStatus.setText(intent.getStringExtra(getString(R.string.network_status)));
                NetworkStatus2.setText("Registered");
            }
        }
    };

    public void gotoActivity(View view) {
        finish();
    }

    private void setNetworkChangeReceiver() {
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(
                networkChangeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));

        LocalBroadcastManager.getInstance(this).
                registerReceiver(networkMessageReceiver,
                        new IntentFilter(getString(R.string.get_network_status)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNetworkChangeReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkMessageReceiver);
        Toast.makeText(this, "unregister Activity 2 broadcaster", Toast.LENGTH_SHORT).show();
        NetworkStatus2.setText("Unregistered");
    }
}
