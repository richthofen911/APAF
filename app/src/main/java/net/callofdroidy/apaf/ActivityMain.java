package net.callofdroidy.apaf;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pubnub.api.Pubnub;

public class ActivityMain extends AppCompatActivity {

    MyServiceMsgIOCenter.MyBinderMsgIO myBinder;
    ServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (MyServiceMsgIOCenter.MyBinderMsgIO) service;
                myBinder.setPubsubProviderClient(new MyPubsubProviderClient(new Pubnub("pub-c-af13868a-beb9-4719-82fc-8518ddfacea8", "sub-c-48ef81b4-f118-11e5-8f88-0619f8945a4f")));
                myBinder.setSubChannel("testChannel");
                myBinder.subToChannel("testChannel", new AppPubsubCallback(ActivityMain.this, "ActivityMain"));
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
