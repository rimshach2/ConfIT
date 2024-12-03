package com.fyp.confit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<PresentationClass> presentation_list = new ArrayList<>();
    User sessionUser;
    HistoryAdapter adapter;
    RecyclerView HistoryRecyclerView;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        HistoryRecyclerView = findViewById(R.id.historyRecyclerView);
        progress= new ProgressDialog(this);
        Intent i= getIntent();
        Bundle b= new Bundle();
        b= i.getExtras();
        sessionUser=(User) b.getSerializable("sessionUser");
        presentation_list= new ArrayList<>();

        getData();

        renderHistory();
    }

    public void getData()
    {
        progress.setMessage("Loading Presentations. Please Wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        HTTPrequest.placeRequest((getResources().getString( R.string.url)) + "presentations?user_token="+sessionUser.authentication_token, "Get", params, headers, new HTTPrequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject data = new JSONObject(result);
                    Boolean success = data.getBoolean("success");

                    if (success){
                        JSONObject listData = data.getJSONObject("data");

                        JSONArray Data= listData.getJSONArray("presentations");

                        presentation_list.clear();

                        for(int i=0; i < Data.length() ; i++){
                            JSONObject classGotData = Data.getJSONObject(i);

                            presentation_list.add(
                                    new PresentationClass(
                                            classGotData.getInt("id"),
                                            classGotData.getString("title"),
                                            classGotData.getString("genre"),
                                            classGotData.getString("env"),
                                            classGotData.getString("audience"),
                                            classGotData.getString("loundess_score"),
                                            classGotData.getString("clarity_score"),
                                            classGotData.getString("emotional_app_score"),
                                            classGotData.getString("fluency_score"),
                                            classGotData.getString("total_score"),
                                            classGotData.getString("avg_loudness"),
                                            classGotData.getString("percentage_mistakes"),
                                            classGotData.getString("fearful_scr"),
                                            classGotData.getString("serious_scr"),
                                            classGotData.getString("happy_scr"),
                                            classGotData.getString("sad_scr"),
                                            classGotData.getString("speech_rate"),
                                            classGotData.getString("num_of_pauses"),
                                            classGotData.getString("pause_duration"),
                                            classGotData.getString("total_duration")


                                            )
                            );
                        }
                        progress.dismiss();
                        renderHistory();

                    }

                    else if (!success) {
                        progress.dismiss();
                        Toast.makeText( HistoryActivity.this, "No Presentations data retrieved", Toast.LENGTH_SHORT ).show( );
                    }

                }catch(JSONException e){
                    System.out.println("you had"+e);
                    progress.dismiss();
                    Toast.makeText( HistoryActivity.this, "An Exception Occurred while Retrieving Presentations", Toast.LENGTH_SHORT ).show( );
                }

                System.out.println(result);

            }

            @Override
            public void onFaliure(String faliure) {
                progress.dismiss();
                //createView();
                Toast.makeText( HistoryActivity.this, "An Error Occurred while Retrieving Presentations", Toast.LENGTH_SHORT ).show( );
                System.out.println("failed bro");
            }
        },HistoryActivity.this);
    }

    private void renderHistory() {
        // Create adapter passing in the sample user data
        adapter = new HistoryAdapter ( this, presentation_list );
        // Attach the adapter to the recyclerview to populate items
        HistoryRecyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        HistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }
}
