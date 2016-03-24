package net.callofdroidy.apaf;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ServiceMsgIOCenter<T extends PubsubProviderClient, S extends GeneralPubsubCallback> extends Service {
    private static final String TAG = "ServiceMsgIOCenter";

    protected T t;

    protected String subChannelName;
    protected String pubChannelName;

    public ServiceMsgIOCenter() {
    }

    public void onCreate(){
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new BinderMsgIO();
    }

    protected void setPubsubProvider(T pubsubProviderClient){
        t = pubsubProviderClient;
    }

    protected void setPublishChannel(String pubChannelName){
        this.pubChannelName = pubChannelName;
    }

    protected void setSubscribeChannel(String subChannelName){
        this.subChannelName = subChannelName;
    }

    protected void subscribeToChannel(String channelName, S appCallback){
        t.subscribeToChannel(channelName, appCallback);
    }

    protected void publishToChannel(String channelName, Message message, S appCallback){
        t.publishToChannel(channelName, message, appCallback);
    }


    public class BinderMsgIO extends Binder{

        public void setPubsubProviderClient(T pubsubProviderClient){
            setPubsubProvider(pubsubProviderClient);
        }

        public void setSubChannel(String channelName){
            setSubscribeChannel(channelName);
        }

        public void setPubChannel(String channelName){
            setPublishChannel(channelName);
        }

        public void subToChannel(String channelName, S appCallBack){
            subscribeToChannel(channelName, appCallBack);
        }

        public void pubToChannel(String channelName, Message message, S appCallback){
            publishToChannel(channelName, message, appCallback);
        }
    }
}
