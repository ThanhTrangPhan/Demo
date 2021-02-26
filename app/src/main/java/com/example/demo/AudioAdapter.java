package com.example.demo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.viewHolder> {

    Context context;
    ArrayList<modelAudio> audioArrayList;
    public OnItemClickListener onItemClickListener;

    public AudioAdapter(Context context, ArrayList<modelAudio> audioArrayList) {
        this.context = context;
        this.audioArrayList = audioArrayList;
    }

    @Override
    public AudioAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_song, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AudioAdapter.viewHolder holder, final int i) {
        holder.title.setText(audioArrayList.get(i).getaudioTitle());
        holder.artist.setText(audioArrayList.get(i).getaudioArtist());
    }

    @Override
    public int getItemCount() {
        return audioArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView) itemView.findViewById(R.id.artist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}