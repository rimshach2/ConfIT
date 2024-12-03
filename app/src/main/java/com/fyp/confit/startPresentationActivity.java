package com.fyp.confit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import java.util.List;



public class startPresentationActivity extends AppCompatActivity {

    private static final String TAG = "Tag";
    private RequestQueue requestQueue;
    private byte[] dd;
    private boolean slidesExist;

    int flag;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    User sessionUser;
    PresentationClass presentation;
    Uri slides;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_presentation);

        flag = 0;

        Intent i = getIntent();
        Bundle b = new Bundle();
        b = i.getExtras();
        assert b != null;
        sessionUser = (User) b.getSerializable("sessionUser");
        presentation = (PresentationClass) b.getSerializable("presentation");
        slidesExist = (Boolean) b.getSerializable("slidesExist");
        if (slidesExist)
            slides = (Uri) Uri.parse((String) b.getSerializable("slides"));
//         ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        startService();

        //open unity here

        PackageManager packageManager = getPackageManager();
        Intent i1 = new Intent("UnityPlayerActivity.intent.action.LAUNCH");
        List<ResolveInfo> activities = packageManager.queryIntentActivities(i1, 0);

        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            if (presentation.getEnv().equals("Class Room")) {

                i1.putExtra("type", presentation.getAudience());
                startActivity(i1);
            } else if (presentation.getEnv().equals("Auditorium")) {
                i1.putExtra("type", presentation.getEnv());
                startActivity(i1);
            }

        }
        else
        {
            Intent i2= new Intent(this, NoUnityRecord.class);
            startActivity(i2);
        }
    }

    @Override
    public void onPause()
    {
        flag=1;
        super.onPause();
    }

    @Override
    public void onResume()
    {
        if(flag==1)
        {
            stopService();
        }

        flag=0;
        super.onResume();
    }

    public void startService() {
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), BackgroundService.class));
        Intent i1= new Intent(getApplicationContext(), DisplayResult.class);
        Bundle b = new Bundle();
        b.putSerializable("sessionUser", sessionUser);
        b.putSerializable("presentation", presentation);
        if(slidesExist)
            b.putSerializable("slides", slides.toString());
        b.putSerializable("slidesExist", slidesExist);
        i1.putExtras(b);
        startActivity(i1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ){
            Toast.makeText(this, "Cannot proceed without permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }

    }


 /*   @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
*/



}

