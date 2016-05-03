package georgia.fr.blocksms;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by kaki on 5/3/16.
 */
public class UpdateView extends Service {
    public static boolean shouldContinue =true;



    String TAG = "FuterRobotics:";
    int statNum = blockSMS.smsdb.numberOfRows("stats");
    Cursor res = blockSMS.smsdb.getAllDataDesc("stats");


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
                    res.moveToFirst();
                   while(shouldContinue){
                       //ბოლოს დაბლოკილ
                       //დაბლოკილი სმს
                       blockSMS.totalBlocked.setText("დაბლოკილი სმს ("+statNum+")");
                       blockSMS.lastBlocked.setText("ბოლოს დაბლოკილი\n"+
                       "tel: "+res.getString(1) + "\n"+
                       "sms: "+res.getString(2) + "\n");
                       wait(2000);
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



        Thread thrd = new Thread(new WorkerThread(startID));
        thrd.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
