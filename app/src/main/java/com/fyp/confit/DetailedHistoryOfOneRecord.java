package com.fyp.confit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class DetailedHistoryOfOneRecord extends AppCompatActivity {

    PresentationClass presentationClass;
     PieChart p1_loud, p2_flu, p3_cla, p4_emo;
    TextView t1, t2, t3, t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_history_of_one_record);

        Intent i= getIntent();
        Bundle b= new Bundle();
        b= i.getExtras();
        presentationClass=(PresentationClass) b.getSerializable("present");


        t1= findViewById(R.id.totalScore);
        t2= findViewById(R.id.genreSel);
        t3=findViewById(R.id.audience_typeSel);
        t4=findViewById(R.id.environment_typeSel);

        t1.setText("Your total score is: "+ presentationClass.getTotal_score()+"/100");
        t2.setText(presentationClass.getGenre());
        t3.setText(presentationClass.getAudience());
        t4.setText(presentationClass.getEnv());

        p1_loud= findViewById(R.id.DashboardLoudnessPieChart1);
        p2_flu= findViewById(R.id.DashboardFluencyPieChart1);
        p3_cla= findViewById(R.id.DashboardClarityPieChart1);
        p4_emo= findViewById(R.id.DashboardConfidencePieChart1);

        TextView a= findViewById(R.id.AvgLoudness1);
        a.setText(String.valueOf(presentationClass.avg_loudness)+"dbFS");

        a=findViewById(R.id.AvgUnclarity1);
        a.setText(String.valueOf(presentationClass.percentage_mistakes));

        a=findViewById(R.id.AvgApp1);
        a.setText(presentationClass.fearful+"% of the total time");

        a=findViewById(R.id.numOfPauses1);
        a.setText( presentationClass.noOfPauses);
        a=findViewById(R.id.pauseDuration1);
        a.setText( presentationClass.pauseDuration+"sec");
        a=findViewById(R.id.speechRate1);
        a.setText(presentationClass.speechrate+" wrd/min");

        setting();

    }

    public int getPercentage(String sq)
    {
        int s=Math.round( Float.parseFloat(sq));
        int q=((s*100)/25);
        return q;
    }
    public void setting()
    {
        ArrayList<Integer> ConfidenceVal = new ArrayList<>();
        ArrayList <Integer> ClarityVal = new ArrayList<>();
        ArrayList <Integer> FluencyVal = new ArrayList<>();
        ArrayList <Integer> LoudnessVal = new ArrayList<>();

        int a=getPercentage(presentationClass.getEmotional_app_score());
        int b=100-a;
        ConfidenceVal.add (a);
        ConfidenceVal.add(b);

        a=getPercentage(presentationClass.getClarity_score());
        b=100-a;
        ClarityVal.add(a);

        ClarityVal.add(b);

        a=getPercentage(presentationClass.getFluency_score());
        b=100-a;
        FluencyVal.add(a);
        FluencyVal.add(b);

        a=getPercentage(presentationClass.getLoudness_score());
        b=100-a;
        LoudnessVal.add(a);
        LoudnessVal.add(b);

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

        drawPieChart.drawChart(this,p4_emo,ConfidenceVal,ConfidenceKey,colorsP1);
        drawPieChart.drawChart(this,p3_cla,ClarityVal,ClarityKey,colorsP2);
        drawPieChart.drawChart(this,p2_flu,FluencyVal,FluencyKey,colorsP3);
        drawPieChart.drawChart(this,p1_loud,LoudnessVal,LoudnessKey,colorsP4);

        p1_loud.invalidate(); p2_flu.invalidate(); p3_cla.invalidate(); p4_emo.invalidate();

    }
}
