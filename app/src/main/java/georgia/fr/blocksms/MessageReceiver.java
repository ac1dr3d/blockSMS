package georgia.fr.blocksms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MessageReceiver extends BroadcastReceiver {
    public static final String TAG = "georgia.fr.blocksms";
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
        Log.i(TAG, messages.getMessageBody());
        DBHelper smsdb;
        smsdb = new DBHelper(context);


        Cursor res = smsdb.getAllData("keywords");

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            if(messages.getMessageBody().toLowerCase().contains(res.getString(1).toLowerCase())||
                    messages.getOriginatingAddress().toLowerCase().contains(res.getString(1).toLowerCase())) {
                smsdb.insertEvent(messages.getOriginatingAddress(),messages.getMessageBody());
                abortBroadcast();
            }
        }
    }
}