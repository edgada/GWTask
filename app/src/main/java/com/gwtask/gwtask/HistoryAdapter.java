package com.gwtask.gwtask;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.SkelbimasViewHolder>{

    List<Zodis> zodziai;

    public SkelbimasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item_layout, viewGroup, false);
        SkelbimasViewHolder pvh = new SkelbimasViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final SkelbimasViewHolder skelbimasViewHolder, int i) {
        skelbimasViewHolder.txtZodis.setText(zodziai.get(i).getReiksme());
        skelbimasViewHolder.txtLaikas.setText(zodziai.get(i).getPaieskosLaikas());
        skelbimasViewHolder.txtCategory.setText(zodziai.get(i).getPartOfSpeech());
        skelbimasViewHolder.id = zodziai.get(i).getReiksme();

        skelbimasViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                if (!DetectConnection.checkInternetConnection(context)) {
                    Toast.makeText(context, R.string.toast_no_internet_error, Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean pavyko = false;
                    try{
                        pavyko = new checkWordRequest().execute(skelbimasViewHolder.id).get();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(context, R.string.toast_error, Toast.LENGTH_SHORT).show();
                    }
                    if(pavyko == false) Toast.makeText(context, R.string.toast_error, Toast.LENGTH_SHORT).show();
                    else
                    {
                        Intent intent = new Intent(context, WordInfo.class);
                        context.startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return zodziai.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class SkelbimasViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView txtZodis, txtLaikas, txtCategory;
        String id;
        RelativeLayout vienasZodis;

        SkelbimasViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            txtZodis = (TextView)itemView.findViewById(R.id.zodis);
            txtLaikas = (TextView)itemView.findViewById(R.id.data);
            txtCategory = (TextView)itemView.findViewById(R.id.category);
            vienasZodis = (RelativeLayout)itemView.findViewById(R.id.vienasZodis);
            id = "";
        }
    }

    HistoryAdapter(List<Zodis> zodziai){
        this.zodziai = zodziai;
    }

}
