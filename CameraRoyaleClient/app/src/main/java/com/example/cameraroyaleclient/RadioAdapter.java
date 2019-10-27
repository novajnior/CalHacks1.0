package com.example.cameraroyaleclient;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * SOURCE: https://stackoverflow.com/questions/28972049/single-selection-in-recyclerview
 * Created by subrahmanyam on 28-01-2016, 04:02 PM.
 */
public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {

    private List<String> games;
    private int lastCheckedPosition = -1;

    //for jury rigging in new code from outside this class; namely, see usage in onCreate in AppMainActivity
    public View.OnClickListener auxiliaryListener;

    public RadioAdapter(List<String> list) {
        games = list;
    }
    public String getCurrentSelected() {
        if (lastCheckedPosition < 0) {
            return null;
        }
        return games.get(lastCheckedPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycleritemview, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.radioButton.setText(games.get(position));
        holder.radioButton.setChecked(position == lastCheckedPosition);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    if (auxiliaryListener != null) {
                        auxiliaryListener.onClick(v);
                    }
                }
            });
        }
    }
}