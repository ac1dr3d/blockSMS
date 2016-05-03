package georgia.fr.blocksms;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class blockSMS extends AppCompatActivity {
    String TAG = "FutureRobotics";
    public static Button viewAll;
    public static DBHelper smsdb;
    public static TextView lastBlocked;
    public static TextView totalBlocked;

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewAll = (Button) findViewById(R.id.viewAll);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_sms);
        smsdb = new DBHelper(this);
        if(!isMyServiceRunning(UpdateView.class)){
            startService(new Intent(this, UpdateView.class));
        }
        setTitle("სმს ფილტრი (ბეტა 1.0)");

        lastBlocked = (TextView) findViewById(R.id.lastBlocked);
        totalBlocked = (TextView) findViewById(R.id.blockedSMSNum);


    }


    public void viewAll(View v){
        Cursor res = smsdb.getAllDataDesc("keywords");
        if(res.getCount()==0)
        {
            //no data
            showMessage("ERROR", "ჩანაწერები არ არის ნაპოვნი!");
            return;
        }
        else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("ID: " + res.getString(0) + "\n");
                buffer.append("ჩ: " + res.getString(1) + "\n\n");
            }
            //show results

            showMessage("Data", buffer.toString());
        }

    }

    public void showMessage(String tittle, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(tittle);
        builder.setMessage(message);
        builder.show();
    }

    public void inputKeyword(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("სიტყვა / ტელ.ნომერი");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT ); // | InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //m_Text = input.getText().toString();
                //smsdb.insertKeyword(input.getText().toString())[
                execMethod("add_kw", input.getText().toString());

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void deleteKeyword(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ჩანაწერის ID");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT ); // | InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //m_Text = input.getText().toString();
                if(input.getText().toString().matches("[0-9]+")){
                    int ID = Integer.parseInt(input.getText().toString());
                    execMethod("delete_kw", input.getText().toString());
                }

                //smsdb.deleteContact(ID);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void execMethod(String method, String... option){
        Intent serviceIntent = new Intent(blockSMS.this,Execute.class);
        try{
            serviceIntent.putExtra("method", method);
            for (int i=0; i< option.length;i++)
            {
                serviceIntent.putExtra("option"+(i+1), option[i]);
            }
        }
        catch(Exception e){
            Log.i(TAG, ""+e);
        }

        startService(serviceIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
