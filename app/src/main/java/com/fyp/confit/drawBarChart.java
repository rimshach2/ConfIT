package com.fyp.confit;

import android.content.Context;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class drawBarChart {
    private static BarChart barChart;
    public static void drawChart(Context context, BarChart barChart, ArrayList<Integer> values, ArrayList<String> keys, int[] colors) {
        drawBarChart.barChart = barChart;
        if (values.size() == keys.size() && keys.size() == colors.length) {

            barChart.getDescription().setEnabled(false);


            //setData(values.size());
            int count = values.size();
            ArrayList<BarEntry> yVals = new ArrayList<>();
            for (int i = 0; i < count; i++)
            {
                //float value = (float) (Math.random()*100);
                yVals.add(new BarEntry(i, (int) values.get(i),keys.get(i)/*value*/));
            }
            BarDataSet set = new BarDataSet(yVals, "Overall Progress");
            //set.setColors(ColorTemplate.MATERIAL_COLORS);
            set.setColors(colors,context);
            //set.setDrawValues(true);

            BarData data = new BarData(set);

            barChart.setData(data);

            barChart.setFitBars(true);

        }else
            Toast.makeText(context, "Wrong Inputs for BarChart!", Toast.LENGTH_SHORT).show();

    }
    private static void setData(int count){
        ArrayList<BarEntry> yVals = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            float value = (float) (Math.random()*100);
            yVals.add(new BarEntry(i, (int) value));
        }
        BarDataSet set = new BarDataSet(yVals, "Data Set");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);

        barChart.setData(data);
    }
}