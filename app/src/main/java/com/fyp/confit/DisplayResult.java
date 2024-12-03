package com.fyp.confit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Presentation;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.os.FileUtils.copy;

public class DisplayResult extends AppCompatActivity {
    
   // private DashboardViewModel dashboardViewModel;
    private BarChart b;
    private PieChart p1, p2, p3, p4;
    TextView t1, t2, t3, t4;
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private ProgressDialog progress;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    double avg_loudness;
    double percentage_mistakes;
    String fearful;
    String serious;
    String happy;
    String sad;
    double speechrate=0.0;
    double noOfPauses=0.0;
    double pauseDuration=0.0;
    double totalDuration=0.0;

    extra e= new extra();

    //private RecordButton recordButton = null;
    private MediaRecorder recorder = null;

    //private PlayButton playButton = null;
    private MediaPlayer player = null;

    User sessionUser;
    PresentationClass presentation;
    Uri slides;
    Boolean slidesExist;
    int audienceEnum,genreEnum,envEnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);

        progress = progress = new ProgressDialog( this );

        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/presentation.wav";
        Intent i= getIntent();
        Bundle b= new Bundle();
        b= i.getExtras();
        sessionUser=(User) b.getSerializable("sessionUser");
        presentation= (PresentationClass) b.getSerializable("presentation");
        slidesExist= (Boolean) b.getSerializable("slidesExist");
        if(slidesExist)
            slides = (Uri) Uri.parse( (String) b.getSerializable("slides"));

        progress.setMessage("Uploading presentation for processing");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

       // SystemClock.sleep(5000);
        //startPlaying();
        uploadToServer(fileName);

    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            Toast.makeText(getApplicationContext(), "playing", Toast.LENGTH_LONG).show();;
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

        public static File from(Context context, Uri uri) throws IOException {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            String fileName = getFileName(context, uri);
            String[] splitName = splitFileName(fileName);
            File tempFile = File.createTempFile(splitName[0], splitName[1]);
            tempFile = rename(tempFile, fileName);
            tempFile.deleteOnExit();
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(tempFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (inputStream != null) {
                copy(inputStream, out);
                inputStream.close();
            }

            if (out != null) {
                out.close();
            }
            return tempFile;
        }

        private static String[] splitFileName(String fileName) {
            String name = fileName;
            String extension = "";
            int i = fileName.lastIndexOf(".");
            if (i != -1) {
                name = fileName.substring(0, i);
                extension = fileName.substring(i);
            }

            return new String[]{name, extension};
        }

        private static String getFileName(Context context, Uri uri) {
            String result = null;
            if (uri.getScheme().equals("content")) {
                Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            if (result == null) {
                result = uri.getPath();
                int cut = result.lastIndexOf(File.separator);
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
            return result;
        }

        private static File rename(File file, String newName) {
            File newFile = new File(file.getParent(), newName);
            if (!newFile.equals(file)) {
                if (newFile.exists() && newFile.delete()) {
                    Log.d("FileUtil", "Delete old " + newName + " file");
                }
                if (file.renameTo(newFile)) {
                    Log.d("FileUtil", "Rename file to " + newName);
                }
            }
            return newFile;
        }

        private static long copy(InputStream input, OutputStream output) throws IOException {
            long count = 0;
            int n;
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            while (EOF != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
            return count;
        }



    private void uploadToServer(String filePath) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path
        File file = new File(filePath);



        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("audio/wav"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("audio", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "audio/x-wav");

        Call<extra> call;
        if(slidesExist && slides!=null) {

            File script = null;
            try {
                script = from(this, slides);
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "cant read file", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }


            Toast.makeText(this, slides.getPath(), Toast.LENGTH_LONG).show();

            // Create a request body with file and image media type
            RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("text/plain"), script);
            // Create MultipartBody.Part using file request-body,file name and part name
            MultipartBody.Part part1 = MultipartBody.Part.createFormData("script", script.getName(), fileReqBody1);

            call = uploadAPIs.uploadAudioAndScript(part, part1/*, description*/);
        }
        else {
             call = uploadAPIs.uploadAudio(part/*, description*/);
        }
        call.enqueue(new Callback<extra>() {
            @Override
            public void onResponse(Call<extra> call, Response<extra> response) {

                //progress.setMessage(response.toString());
                //progress.setCanceledOnTouchOutside(true);
                progress.dismiss();
              //  Toast.makeText(DisplayResult.this, response.body().toString(), Toast.LENGTH_LONG).show();

                if(response.isSuccessful())
                {
                    //JsonObject post = new JsonObject().get(response.body().toString()).getAsJsonObject();

                    try {

                        JSONObject data = new JSONObject(response.body().getData().toString());
                        e.setLoudness_score(data.getJSONObject("loudness").getString("score"));
                        avg_loudness = data.getJSONObject("loudness").getDouble("average_loudness");

                        e.setClarity_score(data.getJSONObject("clarity").getString("clarity_score"));
                        percentage_mistakes = (data.getJSONObject("clarity").getDouble("mistakes"));

                        fearful = (data.getJSONObject("emotional_appropriateness").getString("fearful"));
                        serious = (data.getJSONObject("emotional_appropriateness").getString("serious"));
                        happy = (data.getJSONObject("emotional_appropriateness").getString("happy"));
                        sad = (data.getJSONObject("emotional_appropriateness").getString("sad"));

                        getEmoScore();

                        //here get fluency things and calculate them
                        speechrate= (data.getJSONObject("fluency").getDouble("speech rate"));
                        pauseDuration= (data.getJSONObject("fluency").getDouble("total_pause_duration"));
                        noOfPauses= (data.getJSONObject("fluency").getDouble("total_pause_count"));
                        totalDuration= (data.getJSONObject("fluency").getDouble("total_speech_duration"));

                       getFluencyScore();

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                    /*e.setClarity_score( response.body().getData().toString());
                    e.setClarity_score( response.body().getClarity_score());
                    e.setLoudness_score( response.body().getLoudness_score());
                    e.setEmotional_app_score( response.body().getEmotional_app_score());
                    e.setFluency_score( response.body().getFluency_score());*/

                    setPresentationScores();

                   // Toast.makeText(DisplayResult.this, response.body().getLoudness_score().toString(), Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(DisplayResult.this,"Unsuccessful", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<extra> call, Throwable t) {
                progress.setMessage(t.toString());
                progress.setCanceledOnTouchOutside(true);
                //Toast.makeText(startPresentationActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    void getFluencyScore(){

        int spr, pus;
        if(speechrate>140 && speechrate<150)
        {
            spr= 45;
        }else if (speechrate<=140 && speechrate>130) {
            spr = 40;
        }else if (speechrate<=130 && speechrate>120)
        {
            spr=35;
        }else if (speechrate<=120 && speechrate>110)
        {
            spr=30;
        }else if (speechrate<=110 && speechrate>100)
        {
            spr=25;
        }else if (speechrate>=150 && speechrate<170)
        {
            spr=35;
        }else{
            spr=20;
        }

        double pause_per_min= (noOfPauses/totalDuration)*60;

        //total on average is 10

        if(pause_per_min<=7)
        {
            pus=50;
        }else if(pause_per_min>7 && pause_per_min<=10)
        {
            pus=45;
        }else if(pause_per_min>10 && pause_per_min<=15)
        {
            pus=40;
        }else if(pause_per_min>15 && pause_per_min<=20)
        {
            pus=35;
        }else if(pause_per_min>20 && pause_per_min<=25)
        {
            pus=25;
        }else{
            pus=15;
        }

        int tot=spr+pus;
        e.setFluency_score(String.valueOf(tot));

    }

    void getEmoScore() {
        if (genreEnum == 0)  //happy
        {
            e.setEmotional_app_score(happy);
        } else if (genreEnum == 1) {  //serious
            e.setEmotional_app_score(serious);
        } else if (genreEnum == 2) {  //emotional
            e.setEmotional_app_score(sad);
        }
    }

    public int findScore(String a) {
        if (a != null) {
            float b = Float.parseFloat(a);
            float c = (b / 100) * 25;
            return Math.round(c);
        }
        else
            return 0;
    }

    public int roundOf(String a) {

            float b = Float.parseFloat(a);
            return Math.round(b);
    }

    public void setPresentationScores()
    {
        int a=findScore(e.getLoudness_score());
        presentation.setLoudness_score(String.valueOf(a));
        a=findScore(e.getFluency_score());
        presentation.setFluency_score(String.valueOf(a));
        a=findScore(e.getEmotional_app_score());
        presentation.setEmotional_app_score(String.valueOf(a));
        a=findScore(e.getClarity_score());
        presentation.setClarity_score(String.valueOf(a));

        buildUI();
    }

    public void buildUI()
    {

        TextView a= findViewById(R.id.AvgLoudness);
        a.setText(String.valueOf(avg_loudness)+"dbFS");

        a=findViewById(R.id.AvgUnclarity);
        a.setText(String.valueOf(percentage_mistakes));

        a=findViewById(R.id.AvgApp);
        a.setText(fearful+"% of the total time");

        a=findViewById(R.id.numOfPauses);
        a.setText(String.format("%.2f", noOfPauses));
        a=findViewById(R.id.pauseDuration);
        a.setText(String.format("%.2f", pauseDuration)+"sec");
        a=findViewById(R.id.speechRate);
        a.setText(String.format("%.2f", speechrate)+" words/minute");



        // dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        t1 = findViewById(R.id.DashboardConfidenceTextView);
        t2 = findViewById(R.id.DashboardClarityTextView);
        t3 = findViewById(R.id.DashboardFluencyTextView);
        t4 = findViewById(R.id.DashboardLoudnessTextView);


        p1 = findViewById(R.id.DashboardConfidencePieChart);
        p2 = findViewById(R.id.DashboardClarityPieChart);
        p3 = findViewById(R.id.DashboardFluencyPieChart);
        p4 = findViewById(R.id.DashboardLoudnessPieChart);

        p1.setNoDataText("Click to see your Emotion Appropriateness Result");
        p2.setNoDataText("Click to see your Clarity Result");
        p3.setNoDataText("Click to see your Fluency Result");
        p4.setNoDataText("Click to see your Loudness Result");


        t1.setText("Emotion Appropriateness");
        t2.setText("Clarity");
        t3.setText("Fluency");
        t4.setText("Loudness");

        ArrayList <Integer> ConfidenceVal = new ArrayList<>();
        ArrayList <Integer> ClarityVal = new ArrayList<>();
        ArrayList <Integer> FluencyVal = new ArrayList<>();
        ArrayList <Integer> LoudnessVal = new ArrayList<>();



        ConfidenceVal.add(roundOf(e.getEmotional_app_score()));
        ConfidenceVal.add(100-roundOf(e.getEmotional_app_score()));

        ClarityVal.add(roundOf(e.getClarity_score()));
        ClarityVal.add(100-roundOf(e.getClarity_score()));

        FluencyVal.add(roundOf(e.getFluency_score()));
        FluencyVal.add(100-roundOf(e.getFluency_score()));

        LoudnessVal.add(roundOf(e.getLoudness_score()));
        LoudnessVal.add(100-roundOf(e.getLoudness_score()));

        ArrayList <String> ConfidenceKey = new ArrayList<>();
        ArrayList <String> ClarityKey = new ArrayList<>();
        ArrayList <String> FluencyKey = new ArrayList<>();
        ArrayList <String> LoudnessKey = new ArrayList<>();


        ConfidenceKey.add("% relevant");
        ConfidenceKey.add("% not relevant");

        ClarityKey.add("% clear");
        ClarityKey.add("% not clear");

        FluencyKey.add("% fluent");
        FluencyKey.add("% not fluent");

        LoudnessKey.add("% loud");
        LoudnessKey.add("%not loud");

        int[] colorsP1 = {
                R.color.c1,
                R.color.grey};

        int[] colorsP2 = {
                R.color.c2,
                R.color.grey};

        int[] colorsP3 = {
                R.color.c3,
                R.color.grey};

        int[] colorsP4 = {
                R.color.c4,
                R.color.grey};

        drawPieChart.drawChart(this,p1,ConfidenceVal,ConfidenceKey,colorsP1);
        drawPieChart.drawChart(this,p2,ClarityVal,ClarityKey,colorsP2);
        drawPieChart.drawChart(this,p3,FluencyVal,FluencyKey,colorsP3);
        drawPieChart.drawChart(this,p4,LoudnessVal,LoudnessKey,colorsP4);

        p1.invalidate();p2.invalidate();p3.invalidate();p4.invalidate();
        UpdatePresentation();
    }


    void totalScore()
    {

    }

   public void UpdatePresentation()
    {

        findEnums();
        JSONObject params = new JSONObject();
        JSONObject header = new JSONObject();

        try {
            params.put("user_token", sessionUser.getAuthentication_token());
            params.put("title", presentation.getTitle());
            params.put("genre", genreEnum);
            params.put("env_type",envEnum);
            params.put("audience_type", audienceEnum);
            params.put("loudness_score", presentation.getLoudness_score());
            params.put("clarity_score", presentation.getClarity_score());
            params.put("emotional_app_score", presentation.getEmotional_app_score());
            params.put("fluency_score", presentation.getFluency_score());
            params.put("total_score", "0.0");
            params.put("avg_loudness", avg_loudness);
            params.put("percentage_mistakes", percentage_mistakes);
            params.put("fearful_scr", fearful);
            params.put("serious_scr", serious);
            params.put("happy_scr", happy);
            params.put("sad_scr", sad);
            params.put("speech_rate", speechrate);
            params.put("num_of_pauses", noOfPauses);
            params.put("pause_duration", pauseDuration);
            params.put("total_duration", totalDuration);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progress.setMessage("Updating, Please Wait..");
        progress.show();
        progress.setCanceledOnTouchOutside(false);

        HTTPrequest.callAPI("Post", (getResources().getString(R.string.url) + "presentations"), params, header, new HTTPrequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject res = new JSONObject(result);

                    Boolean success = res.getBoolean("success");
                    if (success){
                        progress.dismiss();
                        Toast.makeText(DisplayResult.this, "Successfully updated presentation scores", Toast.LENGTH_SHORT).show();
                        MainActivity a= new MainActivity();
                        a.sessionUser=sessionUser;
                    }

                    if (!success) {
                        progress.dismiss();
                        Toast.makeText(DisplayResult.this,"Unsuccessful attempt", Toast.LENGTH_SHORT).show();
                        Log.i("Failed", "you Failed");
                    }

                } catch (JSONException e) {
                    progress.dismiss();
                    System.out.println("you had" + e);
                }
            }

            @Override
            public void onFaliure(String faliure) {
                Toast.makeText(DisplayResult.this, "Failure", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                return;
            }
        },this);


    }

    public void findEnums()
    {
        if(presentation.getEnv().equals("Class Room"))
        {
            envEnum=0;
        } else if(presentation.getEnv().equals("Auditorium"))
        {
            envEnum=2;
        }

        if(presentation.getGenre().equals("Humorous"))
        {
            genreEnum=0;
        } else if(presentation.getGenre().equals("Serious"))
        {
            genreEnum=1;
        } else if(presentation.getGenre().equals("Emotional"))
        {
            genreEnum=2;
        }

        if(presentation.getAudience().equals("Noisy (Indecent)"))
        {
            audienceEnum=1;
        } else if(presentation.getAudience().equals("Serious (Very Responsive)"))
        {
            audienceEnum=0;
        }else if(presentation.getAudience().equals("Moderate (Decent)"))
        {
            audienceEnum=2;
        }else if(presentation.getAudience().equals("Random (Any Audience)"))
        {
            Random rand= new Random();
            int randomNum = rand.nextInt((3 - 0) );

            if(randomNum<3)
            {
                audienceEnum=randomNum;
            }
            else
            {
                audienceEnum=0;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(this, MainActivity.class);
        Bundle b= new Bundle();
        b.putSerializable("sessionUser", sessionUser);
        i.putExtras(b);
        startActivity(i);
        //super.onBackPressed();
    }
}
