package com.fyp.confit;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class drawPieChart {

    public static void drawChart(Context context, PieChart pieChart, ArrayList<Integer> values, ArrayList<String> keys, int[] colors) {

        if (values.size() == keys.size() && keys.size() == colors.length) {
            pieChart.setUsePercentValues(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.setExtraOffsets(5, 10, 5, 5);

            pieChart.setDragDecelerationFrictionCoef(0.95f);

            pieChart.setDrawHoleEnabled(true);

            pieChart.setHoleColor(Color.WHITE);

            pieChart.setTransparentCircleRadius(60f);
            ArrayList<PieEntry> yValues = new ArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                if (!values.get(i).equals(0))
                    yValues.add(new PieEntry(values.get(i), keys.get(i)));
            }

            PieDataSet dataSet = new PieDataSet(yValues, "");
            dataSet.setSliceSpace(6f);

            dataSet.setSelectionShift(15f);
            dataSet.setColors(colors, context);
            PieData data = new PieData((dataSet));

            data.setValueTextSize(13f);

            data.setValueTextColor(Color.BLACK);

            pieChart.setData(data);
        }else
            Toast.makeText(context, "Wrong Inputs for Pie Chart!", Toast.LENGTH_SHORT).show();

    }

}
