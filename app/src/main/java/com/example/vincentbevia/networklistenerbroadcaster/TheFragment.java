package com.example.vincentbevia.networklistenerbroadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TheFragment extends Fragment {

    private BroadcastReceiver networkChangeReceiver;
    private Button fragmentButton;
    private TextView NetworkStatus;

    public TheFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NetworkStatus = (TextView)getActivity().findViewById(R.id.networkStatus);
        fragmentButton = (Button)getActivity().findViewById(R.id.fragmentButton);
        fragmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getActivity(), "I've been click in the fragment ;)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final BroadcastReceiver networkMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("network_status").equals("CONNECTED TO WIFI")
                    || intent.getStringExtra("network_status").equals("CONNECTED TO MOBILE")) {

                //Toast.makeText(context, "Already set", Toast.LENGTH_SHORT).show();
                NetworkStatus.setText(intent.getStringExtra("network_status"));

            } else {

                //Refresh web or view here!
                NetworkStatus.setText(intent.getStringExtra("network_status"));
            }
        }
    };

    private void setNetworkChangeReceiver() {
        networkChangeReceiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(
                networkChangeReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));

        LocalBroadcastManager.getInstance(getActivity()).
                registerReceiver(networkMessageReceiver, new IntentFilter("get-network-status"));
    }

    private void unRegisterNetworkChangeReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(networkMessageReceiver);
        Toast.makeText(getActivity(), "unregister Fragment broadcaster", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setNetworkChangeReceiver();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unRegisterNetworkChangeReceiver();
    }
}
