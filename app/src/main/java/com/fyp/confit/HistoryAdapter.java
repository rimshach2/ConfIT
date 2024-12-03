package com.fyp.confit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends
        RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    ArrayList<Integer> values = new ArrayList<>();
    ArrayList<String> keys = new ArrayList<>();


    int[] colors;

    public HistoryAdapter() {
    }

    ArrayList<PresentationClass > historyAdapterData = new ArrayList<>();

    // Pass in the array into the constructor
    public HistoryAdapter( Context context, ArrayList<PresentationClass> historyAdapterData ) {
        this.historyAdapterData = historyAdapterData;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        TextView loudnessTxt;
        TextView fluencyTxt;
        TextView clarityTxt;
        TextView emotionTxt;
        TextView title;
        LinearLayout ee;
        ProgressBar loudness, emotion, clarity, fluency;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder( View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title= itemView.findViewById(R.id.presentation_title);
            loudnessTxt = (TextView) itemView.findViewById( R.id.loudnessProgressScore);
            fluencyTxt = (TextView) itemView.findViewById( R.id.fluencyProgressScore );
            clarityTxt = (TextView) itemView.findViewById( R.id.clarityProgressScore );
            emotionTxt = (TextView) itemView.findViewById( R.id.emotionProgressScore );
            ee=itemView.findViewById(R.id.one);

            loudness=itemView.findViewById(R.id.loudnessProgress);
            clarity=itemView.findViewById(R.id.clarityProgress);
            emotion=itemView.findViewById(R.id.emotionProgress);
            fluency=itemView.findViewById(R.id.fluencyProgress);


        }
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.one_history_card, parent, false);

        // Return a new holder instance
        return new HistoryAdapter.ViewHolder(view);
    }
    //pieChartClassName
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder( final HistoryAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final PresentationClass onePracticeData = historyAdapterData.get(position);

        PresentationClass current = historyAdapterData.get(position);

        viewHolder.title.setText(current.getTitle());
        viewHolder.loudnessTxt.setText(current.getLoudness_score());
        viewHolder.clarityTxt.setText(current.getClarity_score());
        viewHolder.fluencyTxt.setText(current.getFluency_score());
        viewHolder.emotionTxt.setText(current.getEmotional_app_score());

        viewHolder.fluency.setProgress(Math.round(Float.parseFloat(current.getFluency_score())));
        viewHolder.loudness.setProgress(Math.round(Float.parseFloat(current.getLoudness_score())));
        viewHolder.clarity.setProgress(Math.round(Float.parseFloat(current.getClarity_score())));
        viewHolder.emotion.setProgress(Math.round(Float.parseFloat(current.getEmotional_app_score())));


        viewHolder.ee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b= new Bundle();
                b.putSerializable("present", onePracticeData);
                Intent i= new Intent(context, DetailedHistoryOfOneRecord.class);
                i.putExtras(b);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyAdapterData.size();
    }
}