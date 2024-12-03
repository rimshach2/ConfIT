package com.fyp.confit.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fyp.confit.HTTPrequest;
import com.fyp.confit.HistoryActivity;
import com.fyp.confit.MainActivity;
import com.fyp.confit.PresentationClass;
import com.fyp.confit.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.fyp.confit.R;
import com.fyp.confit.drawBarChart;
import com.fyp.confit.drawPieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    Button viewHistoryBtn;
    User sessionUser;
    Context context;
    private DashboardViewModel dashboardViewModel;
    private PieChart p1, p2, p3, p4;
    TextView t1, t2, t3, t4;
    LineChart lineChart;
    ProgressDialog progress;
    ArrayList<Entry> inputDataValues;
    ArrayList<Float> loudnessDataValues, clarityDataValues, fluencyDataValues, emoDataValues;
    float loudness, clarity, fluency, emo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        context= (MainActivity) getActivity();
        MainActivity a = (MainActivity) getActivity();
        progress= new ProgressDialog(a);
        sessionUser=a.sessionUser;

        lineChart = root.findViewById(R.id.DashboardSummaryLineChart);

        inputDataValues = new ArrayList<>();
        loudnessDataValues = new ArrayList<>();
        clarityDataValues = new ArrayList<>();
        fluencyDataValues = new ArrayList<>();
        emoDataValues = new ArrayList<>();
        getData();

        /*inputDataValues.add(new Entry(1,22));
        inputDataValues.add(new Entry(2,18));
        inputDataValues.add(new Entry(3,26));
        inputDataValues.add(new Entry(4,24));
        inputDataValues.add(new Entry(5,30));
        inputDataValues.add(new Entry(6,30));
        inputDataValues.add(new Entry(7,31));
        inputDataValues.add(new Entry(8,35));
        inputDataValues.add(new Entry(9,28));

        LineDataSet dataSet = new LineDataSet(inputDataValues, "Presentation x Score Illustration");

        dataSet.setHighlightEnabled(true);
        dataSet.setLineWidth(3);
        dataSet.setCircleColor(a.getResources().getColor(R.color.eventCardsColor));
        dataSet.setCircleRadius(6);
        dataSet.setCircleHoleRadius(3);
        dataSet.setDrawHighlightIndicators(true);
        dataSet.setHighLightColor(Color.RED);

        LineData Data = new LineData();
        Data.addDataSet(dataSet);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setData(Data);
        lineChart.invalidate();
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.animateY(700);*/

        t1 = root.findViewById(R.id.DashboardLoudnessTextView);
        t2 = root.findViewById(R.id.DashboardClarityTextView);
        t3 = root.findViewById(R.id.DashboardEmotionTextView);
        t4 = root.findViewById(R.id.DashboardFluencyTextView);


        p1 = root.findViewById(R.id.DashboardLoudnessPieChart);
        p2 = root.findViewById(R.id.DashboardClarityPieChart);
        p3 = root.findViewById(R.id.DashboardEmotionPieChart);
        p4 = root.findViewById(R.id.DashboardFluencyPieChart);

        t1.setText("Emotion");
        t2.setText("Clarity");
        t3.setText("Fluency");
        t4.setText("Loudness");


        viewHistoryBtn = root.findViewById(R.id.viewHistoryBtn);
        viewHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHistory(v);
            }
        });

        return root;
    }

    public int roundOf(Float a) {
        return Math.round(a);
    }

    public void createPieCharts()
    {
        loudness=0;clarity=0;emo=0; fluency=0;
        for(int i=0; i<loudnessDataValues.size(); i++)
        {
            loudness= loudness+ loudnessDataValues.get(i);
            clarity+=clarityDataValues.get(i);
            fluency+=fluencyDataValues.get(i);
            emo+=emoDataValues.get(i);
        }
        System.out.println(loudness+"  "+clarity+"  "+fluency+"  "+emo);

        loudness=(loudness/(loudnessDataValues.size()*25))*100;
        clarity=(clarity/(clarityDataValues.size()*25))*100;
        fluency=(fluency/(fluencyDataValues.size()*25))*100;
        emo=(emo/(emoDataValues.size()*25))*100;

        ArrayList <Integer> LoudnessVal = new ArrayList<>();
        ArrayList <Integer> ClarityVal = new ArrayList<>();
        ArrayList <Integer> FluencyVal = new ArrayList<>();
        ArrayList <Integer> EmotionVal = new ArrayList<>();

        EmotionVal.add(roundOf(emo));
        EmotionVal.add(100-roundOf(emo));

        ClarityVal.add(roundOf(clarity));
        ClarityVal.add(100-roundOf(clarity));

        FluencyVal.add(roundOf(fluency));
        FluencyVal.add(100-roundOf(fluency));

        LoudnessVal.add(roundOf(loudness));
        LoudnessVal.add(100-roundOf(loudness));

        ArrayList <String> LoudnessKey = new ArrayList<>();
        ArrayList <String> ClarityKey = new ArrayList<>();
        ArrayList <String> FluencyKey = new ArrayList<>();
        ArrayList <String> EmotionKey = new ArrayList<>();


        EmotionKey.add("% Appropriate Emotion");
        EmotionKey.add("% Inappropriate");

        ClarityKey.add("% Clear");
        ClarityKey.add("% Unclear");

        FluencyKey.add("% Fluent");
        FluencyKey.add("% Not fluent");

        LoudnessKey.add("% Loud");
        LoudnessKey.add("% Not loud enough");

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

        drawPieChart.drawChart(getContext(),p4,LoudnessVal,LoudnessKey,colorsP4);
        drawPieChart.drawChart(getContext(),p2,ClarityVal,ClarityKey,colorsP2);
        drawPieChart.drawChart(getContext(),p1,EmotionVal,EmotionKey,colorsP1);
        drawPieChart.drawChart(getContext(),p3,FluencyVal,FluencyKey,colorsP3);

        p1.invalidate(); p2.invalidate(); p3.invalidate(); p4.invalidate();
        p1.setEntryLabelColor(R.color.c4);
        p2.setEntryLabelColor(R.color.c4);
        p3.setEntryLabelColor(R.color.c4);
        p4.setEntryLabelColor(R.color.c4);



    }

    public void getData()
    {
        progress.setMessage("Loading Presentations. Please Wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        HTTPrequest.placeRequest((getResources().getString( R.string.url)) + "presentations?user_token="+sessionUser.getAuthentication_token(), "Get", params, headers, new HTTPrequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject data = new JSONObject(result);
                    Boolean success = data.getBoolean("success");

                    if (success){
                        JSONObject listData = data.getJSONObject("data");

                        JSONArray Data= listData.getJSONArray("presentations");

                        inputDataValues.clear();

                        for(int i=0; i < Data.length() ; i++){
                            JSONObject classGotData = Data.getJSONObject(i);

                            inputDataValues.add(new Entry(i,(float)classGotData.getDouble("total_score")));
                            loudnessDataValues.add((float)classGotData.getDouble("loundess_score"));
                            clarityDataValues.add((float)classGotData.getDouble("clarity_score"));
                            fluencyDataValues.add((float)classGotData.getDouble("fluency_score"));
                            emoDataValues.add((float)classGotData.getDouble("emotional_app_score"));


                        }


                        progress.dismiss();

                        LineDataSet dataSet = new LineDataSet(inputDataValues, "Presentation x Score Illustration");

                        dataSet.setHighlightEnabled(true);
                        dataSet.setLineWidth(3);
                        dataSet.setCircleColor(getContext().getResources().getColor(R.color.eventCardsColor));
                        dataSet.setCircleRadius(6);
                        dataSet.setCircleHoleRadius(3);
                        dataSet.setDrawHighlightIndicators(true);
                        dataSet.setHighLightColor(Color.RED);

                        LineData lineData = new LineData();
                        lineData.addDataSet(dataSet);
                        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        lineChart.setData(lineData);
                        lineChart.invalidate();
                        lineChart.setDragEnabled(true);
                        lineChart.setScaleEnabled(true);
                        lineChart.animateY(700);

                        createPieCharts();

                    }

                    else if (!success) {
                        progress.dismiss();
                        Toast.makeText( getContext(), "No Presentations data retrieved", Toast.LENGTH_SHORT ).show( );
                    }

                }catch(JSONException e){
                    System.out.println("you had"+e);
                    progress.dismiss();
                    Toast.makeText( getContext(), "An Exception Occurred while Retrieving Presentations", Toast.LENGTH_SHORT ).show( );
                }

                System.out.println(result);

            }

            @Override
            public void onFaliure(String faliure) {
                progress.dismiss();
                //createView();
                Toast.makeText( getContext(), "An Error Occurred while Retrieving Presentations", Toast.LENGTH_SHORT ).show( );
                System.out.println("failed bro");
            }
        },getActivity());
    }

    public void viewHistory(View v)
    {
        Intent i= new Intent(getActivity(), HistoryActivity.class);
        Bundle b= new Bundle();
        b.putSerializable("sessionUser", sessionUser);
        i.putExtras(b);
        startActivity(i);
    }

}