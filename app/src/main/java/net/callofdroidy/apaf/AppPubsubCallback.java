package net.callofdroidy.apaf;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;

/**
 * Created by admin on 23/03/16.
 */
public class AppPubsubCallback extends Callback implements GeneralPubsubCallback {
    //private Pubnub pubnub;
    private Gson gson;
    private Context context;
    private Handler handler;
    private String TAG;

    public AppPubsubCallback(Context context, String TAG){
        super();
        //this.pubnub = pubnub;
        this.context = context;
        this.handler = new Handler(context.getMainLooper());
        this.TAG = TAG;
        gson = new Gson();
    }

    @Override
    public void connectCallback(String channel, Object message) {
        Log.e("SUBSCRIBE", "CONNECT on channel:" + channel
                + " : " + message.getClass() + " : "
                + message.toString());
        /*
        HashMap<String, String> header = new HashMap<>();
        header.put("name", "yichao");
        Message myMessage = new Message(header, "hello");
2
        pubnub.publish("Channel-a28z6ck8r", gson.toJson(myMessage), new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        });
        */
    }

    @Override
    public void disconnectCallback(String channel, Object message) {
        Log.e("SUBSCRIBE", "DISCONNECT on channel:" + channel
                + " : " + message.getClass() + " : "
                + message.toString());
    }

    public void reconnectCallback(String channel, Object message) {
        Log.e("SUBSCRIBE", "RECONNECT on channel:" + channel
                + " : " + message.getClass() + " : "
                + message.toString());
    }

    // this is the method to get published msg
    @Override
    public void successCallback(String channel, Object message) {
        Log.e("raw recv", message.toString());

        JsonElement msgInJsonElement = gson.fromJson(message.toString(), JsonElement.class);
        onSuccessReceive(gson.fromJson(msgInJsonElement, Message.class));
    }

    @Override
    public void errorCallback(String channel, PubnubError error) {
        onFailReceive(error);
    }

    // this method is from GeneralPubsubCallback
    @Override
    public void onSuccessReceive(Message message){
        Log.e("recv msg", message.getBody());
    }

    // this method is from GeneralPubsubCallback
    @Override
    public void onFailReceive(Object object){
        Log.e("fail recv", object.toString());
        toastCallbackResult(object.toString());
    }

    // this method is from GeneralPubsubCallback
    @Override
    public void onSuccessPublish(Object object){
        Log.e("success pub", object.toString());
    }

    // this method is from GeneralPubsubCallback
    @Override
    public void onFailPublish(Object object){
        Log.e("fail pub", object.toString());
        toastCallbackResult(object.toString());
    }

    protected void toastCallbackResult(final String result){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Callback info: " + result, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Callback info: " + result);
            }
        });
    }
}
