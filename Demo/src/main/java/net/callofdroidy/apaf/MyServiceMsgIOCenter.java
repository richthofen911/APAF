package net.callofdroidy.apaf;

import android.content.Intent;
import android.os.IBinder;

import net.callofdroidy.apas.ServiceMsgIOCenter;

public class MyServiceMsgIOCenter extends ServiceMsgIOCenter<MyPubsubProviderClient, AppPubsubCallback> {
    public MyServiceMsgIOCenter() {
    }

    public void onCreate(){
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        super.onBind(intent);

        return new MyBinderMsgIO();
    }

    public class MyBinderMsgIO extends ServiceMsgIOCenter.BinderMsgIO{
        public MyBinderMsgIO(){
            super();
        }
    }
}
