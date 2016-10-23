package com.willnjames.android.morgbook.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.willnjames.android.morgbook.Model.Person;
import com.willnjames.android.morgbook.ProgressActivity;
import com.willnjames.android.morgbook.R;

import java.util.List;


/**
 * Created by wsoeh on 20/09/2016.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.studentViewHolder> {

    //Create a ViewHolder for the elements in the card
    public static class studentViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView studentName;
        TextView zID;

        studentViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            studentName = (TextView)itemView.findViewById(R.id.student_name);
            zID = (TextView)itemView.findViewById(R.id.student_id);
        }

    }

    List<Person> personsList;

    public RVAdapter(List<Person> personsList){
        this.personsList = personsList;
    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }

    @Override
    public studentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_layout, viewGroup, false);
        studentViewHolder svh = new studentViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(final studentViewHolder studentViewHolder, final int i) {
        String fullName = personsList.get(i).getLName().toUpperCase()+", "+personsList.get(i).getFName();
        studentViewHolder.studentName.setText(fullName);
        studentViewHolder.zID.setText("z"+personsList.get(i).getZ_ID());

        studentViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int zID = personsList.get(i).getZ_ID();
                try{
                    ((ProgressActivity) view.getContext()).setProgressSelection(zID);
                    Log.d("TEST6", "Student Clicked");
                } catch (Exception e){
                    Log.d("EXCEPTION", "Adapter to ProgressActivity method call"+e.toString());
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
