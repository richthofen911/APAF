package net.callofdroidy.apaf;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;

import net.callofdroidy.apas.GeneralPubsubCallback;
import net.callofdroidy.apas.Message;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;

/**
 * Created by admin on 23/03/16.
 */
public class AppPubsubCallback extends Callback implements GeneralPubsubCallback {
    private Gson gson;
    private Activity activity;
    private Handler handler;
    private String TAG;
    private TextView tvDisplay;


    public AppPubsubCallback(Activity activity, @NonNull String TAG){
        super();
        this.activity = activity;
        this.handler = new Handler(activity.getMainLooper());
        this.TAG = TAG;
        gson = new Gson();
        tvDisplay = (TextView) activity.findViewById(R.id.tv_display);
    }

    @Override
    public void connectCallback(String channel, Object message) {
        Log.e("SUBSCRIBE", "CONNECT on channel:" + channel
                + " : " + message.getClass() + " : "
                + message.toString());
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

    // this method is called when either successfully receive/publish a message
    @Override
    public void successCallback(String channel, Object message) {
        try{
            JsonElement msgInJsonElement = gson.fromJson(message.toString(), JsonElement.class);
            onSuccessReceive(gson.fromJson(msgInJsonElement, Message.class));
        }catch (JsonSyntaxException e){
            try{
                JSONArray jsonArray = (JSONArray)message;
                if(jsonArray.get(0) == 1)
                    onSuccessPublish("Msg sent");
            }catch (JSONException x){
                Log.e(TAG, "get a message, but not an expected one and no need to process");
            }
        }
    }

    @Override
    public void errorCallback(String channel, PubnubError error) {
        onFailReceive(error.getErrorString());
        onFailPublish(error.getErrorString());
    }

    // this method is from GeneralPubsubCallback
    @Override
    public void onSuccessReceive(final Message message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String msg = "source: " + message.getHeaders().get("source") + "\n" + "body: " + message.getBody();
                tvDisplay.setText(msg);
            }
        });
    }

    // this method is from GeneralPubsubCallback
    @Override
    public void onFailReceive(Object object) {
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
                Toast.makeText(activity, "Callback info: " + result, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Callback info: " + result);
            }
        });
    }
}
