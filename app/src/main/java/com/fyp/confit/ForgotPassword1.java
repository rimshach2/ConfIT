package com.fyp.confit;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import java.util.Random;

public class ForgotPassword1 extends AppCompatActivity {
   // private SmsBroadcastReceiver smsBroadcastReceiver;

    ProgressDialog pd;
    String email;
    String phoneNo;
    String random_num;
    String user_token;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password1);
        pd = new ProgressDialog(this);


        Intent i1= getIntent();
        Bundle b= i1.getExtras();

        email= (String) b.getSerializable("email");
        user_token= (String) b.getSerializable("user_token");
        phoneNo=(String) b.getSerializable("phone");


        Random random= new Random();
        int num= random.nextInt(999999);
        // this will convert any number sequence into 6 character.
        random_num= String.format("%06d", num);
        Toast.makeText(this, random_num, Toast.LENGTH_LONG).show();


        sendSMSMessage();



    /*    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("ConfIT Reset Code");
        mBuilder.setContentText(random_num+" is your verification code");*/

        final PinEntryEditText pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.toString().equals(random_num)) {
                        Toast.makeText(ForgotPassword1.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(ForgotPassword1.this, ResetPassword.class);
                        Bundle b = new Bundle(  );
                        b.putSerializable( "user_token", user_token);
                        b.putSerializable( "email", email );
                        intent.putExtras( b );


                        startActivity(intent);
                    } else {
                        Toast.makeText(ForgotPassword1.this, "incorrect code", Toast.LENGTH_SHORT).show();
                        pinEntry.setText(null);
                        return;
                    }
                }
            });
        }
    }


    public void goBack1(View v)
    {
        Intent intent = new Intent( getApplicationContext(), ForgotPassword.class );

        startActivity( intent );
    }

    protected void sendSMSMessage() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+92"+phoneNo, null, "Your verification code is "+ random_num, null, null);
              //  Toast.makeText(getApplicationContext(), "SMS sent.",
             //           Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            }
        }
        else
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+92"+phoneNo, null, "Your verification code is "+ random_num, null, null);
        //    Toast.makeText(getApplicationContext(), "SMS sent.",
       //             Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+92"+phoneNo, null, "Your verification code is "+ random_num, null, null);

               //     Toast.makeText(getApplicationContext(), "SMS sent.",
                //            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
