package com.fyp.confit;

import android.Manifest;
import android.app.Service;
import android.content.*;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.*;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    //private RecordButton recordButton = null;
    private Recorder recorder = null;
    private static String fileName = null;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        //Log.e(LOG_TAG, "STORAGE "+ Environment.getExternalStorageState());
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/presentation.wav";
       /* recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();

            Toast.makeText(getApplicationContext(), "prepared", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed "+ e);
            Toast.makeText(this, "Recording.prepared failed ", Toast.LENGTH_LONG).show();

        }
*/
        //recorder.start();
        //Toast.makeText(this, "Recording has started", Toast.LENGTH_LONG).show();

        recorder = OmRecorder.wav(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override public void onAudioChunkPulled(AudioChunk audioChunk) {
                        //animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
                    }
                }), file());

        recorder.startRecording();
        Toast.makeText(this, "Recording has started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @NonNull
    private File file() {
        return new File(getExternalCacheDir(), "presentation.wav");
    }

    private PullableSource mic()
    {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        );
    }
    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        if (recorder != null) {
            try {
                recorder.stopRecording();
            } catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "unable to stop recording", Toast.LENGTH_LONG).show();
            }

            //recorder.reset();
            //recorder.release();
            recorder = null;
            Toast.makeText(getApplicationContext(), "Recording has stopped", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }
}