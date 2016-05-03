package georgia.fr.blocksms;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;


/**
 * Created by root on 5/2/16.
 */
public class Execute extends Service {

    String TAG = "FuterRobotics:";
    public static String method;
    public static String option1;
    public static String option2;
    public static String option3;
    public static String option4;

    final class WorkerThread implements Runnable{
        int serviceID;

        WorkerThread(int ID)
        {
            serviceID=ID;
        }

        @Override
        public void run() {
            synchronized (this)
            {
                try{

                    switch (method){
                        case "add_kw":
                            blockSMS.smsdb.insertKeyword(option1);
                            break;
                        case "delete_kw":
                            blockSMS.smsdb.deleteContact(Integer.parseInt(option1));
                            break;
                    }




                }
                catch (Exception e)
                {
                    Log.i(TAG, ""+e);
                }

                stopSelf(serviceID);
            }

        }
    }


    @Override
    public void onCreate(){super.onCreate(); }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startID)
    {

       method = intent.getStringExtra("method");
        option1 = intent.getStringExtra("option1");
        option2 = intent.getStringExtra("option2");
        option3 = intent.getStringExtra("option3");
        option4 = intent.getStringExtra("option4");

        Thread thrd = new Thread(new WorkerThread(startID));
        thrd.start();
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
