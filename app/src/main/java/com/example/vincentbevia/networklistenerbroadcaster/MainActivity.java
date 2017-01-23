package com.example.vincentbevia.networklistenerbroadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver networkChangeReceiver;
    private FragmentManager mFragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragmentCurrent = null;
    private final TheFragment blankFragment = new TheFragment();
    private FrameLayout myFragmentLayout;
    private Button FragmentShow, FragmentHide;
    private TextView NetworkStatus, NetworkStatus2;
    private Button UnregisterServiceButton, RegisterServiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentShow = (Button) findViewById(R.id.fragmentGoTOButton);
        FragmentHide = (Button) findViewById(R.id.fragmentHideTOButton);
        NetworkStatus = (TextView) findViewById(R.id.networkStatus);
        NetworkStatus2 = (TextView) findViewById(R.id.networkStatus2);
        myFragmentLayout = (FrameLayout) findViewById(R.id.containerView);
        UnregisterServiceButton = (Button) findViewById(R.id.unregisterServiceButton);
        RegisterServiceButton = (Button) findViewById(R.id.registerServiceButton);
        mFragmentManager = getSupportFragmentManager();
    }

    public void gotoDefaultFragment(View view) {
        FragmentShow.setVisibility(View.GONE);
        FragmentHide.setVisibility(View.VISIBLE);
        myFragmentLayout.setVisibility(View.VISIBLE);
        fragmentTransaction = mFragmentManager.beginTransaction();
        this.fragmentCurrent = this.blankFragment;
        fragmentTransaction.replace(R.id.containerView, this.fragmentCurrent).commit();
    }

    public void gotoActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);

    }

    public void gotoHidetFragment(View view) {
        myFragmentLayout.setVisibility(View.GONE);
        FragmentShow.setVisibility(View.VISIBLE);
        FragmentHide.setVisibility(View.GONE);

        mFragmentManager.beginTransaction().remove(this.blankFragment).commit();
    }

    private final BroadcastReceiver networkMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(getString(R.string.network_status)).equals("CONNECTED TO WIFI")
                    || intent.getStringExtra(getString(R.string.network_status)).equals("CONNECTED TO MOBILE")) {

                //Toast.makeText(context, "Already set", Toast.LENGTH_SHORT).show();
                NetworkStatus.setText(intent.getStringExtra(getString(R.string.network_status)));
                NetworkStatus2.setText("Registered");
                UnregisterServiceButton.setVisibility(View.VISIBLE);
                RegisterServiceButton.setVisibility(View.GONE);

            } else {

                //Refresh web or view here!
                NetworkStatus.setText(intent.getStringExtra(getString(R.string.network_status)));
            }
        }
    };

    public void register(View view) {
        setNetworkChangeReceiver();
        UnregisterServiceButton.setVisibility(View.VISIBLE);
        RegisterServiceButton.setVisibility(View.GONE);
        NetworkStatus2.setText("Registered");

    }

    public void unregister(View view) {
        unRegisterNetworkService();
        UnregisterServiceButton.setVisibility(View.GONE);
        RegisterServiceButton.setVisibility(View.VISIBLE);
        NetworkStatus2.setText("Unregistered");
        NetworkStatus.setText("Unable to read status...");
    }

    @Override
    protected void onResume() {
        super.onResume();

        setNetworkChangeReceiver();
    }

    private void unRegisterNetworkService() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkMessageReceiver);
        Toast.makeText(this, "unregister Activity 1 broadcaster", Toast.LENGTH_SHORT).show();
        NetworkStatus2.setText("Unregistered");
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
    protected void onPause() {
        super.onPause();
        unRegisterNetworkService();
    }
}