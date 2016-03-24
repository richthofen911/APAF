package net.callofdroidy.apaf;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pubnub.api.Pubnub;

import net.callofdroidy.apas.Message;

import java.util.HashMap;

public class ActivityMain extends AppCompatActivity {

    MyServiceMsgIOCenter.MyBinderMsgIO myBinder;
    ServiceConnection conn;
    MyPubsubProviderClient myPubsubProviderClient;
    AppPubsubCallback appPubsubCallback;

    TextView tvDisplay;
    EditText etInput;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = (TextView) findViewById(R.id.tv_display);
        etInput = (EditText) findViewById(R.id.et_input);
        btnSend = (Button) findViewById(R.id.btn_send);

        myPubsubProviderClient = new MyPubsubProviderClient(new Pubnub("pub-c-af13868a-beb9-4719-82fc-8518ddfacea8", "sub-c-48ef81b4-f118-11e5-8f88-0619f8945a4f"));
        appPubsubCallback = new AppPubsubCallback(this, "ActivityMain");

        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (MyServiceMsgIOCenter.MyBinderMsgIO) service;
                myBinder.setPubsubProviderClient(myPubsubProviderClient);
                myBinder.setPubsubCallback(appPubsubCallback);
                myBinder.setSubChannel("testChannelA");
                myBinder.subToChannel();
                myBinder.setPubChannel("testChannelB");

                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> header = new HashMap<>();
                        header.put("source", "User A");
                        Message myMessage = new Message(header, etInput.getText().toString());
                        myBinder.pubToChannel(myMessage);
                        etInput.setText("");
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(new Intent(ActivityMain.this, MyServiceMsgIOCenter.class), conn, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(myBinder != null && myBinder.isBinderAlive())
            unbindService(conn);
    }
}
