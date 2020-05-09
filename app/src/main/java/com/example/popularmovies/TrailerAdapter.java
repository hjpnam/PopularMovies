package com.example.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.shared.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private List<Trailer> mTrailers;

    TrailerAdapter() { mTrailers = new ArrayList<>(); }

    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.trailer_list_item, parent, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);
        holder.playIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openVideoPlayerIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("vnd.youtube:" + trailer.getKey()));
                Intent openWebBrowserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey()));
                Context context = v.getContext();
                try {
                    context.startActivity(openVideoPlayerIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(openWebBrowserIntent);
                }
            }
        });
        holder.trailerNameTv.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) return 0;
        return mTrailers.size();
    }

    void setTrailers(List<Trailer> trailers) {
        mTrailers = new ArrayList<>(trailers);
        notifyDataSetChanged();
    }


    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageButton playIb;
        TextView trailerNameTv;

        public TrailerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            playIb = (ImageButton) itemView.findViewById(R.id.ib_play_trailer);
            trailerNameTv = (TextView) itemView.findViewById(R.id.tv_trailer_name);
        }
    }
}
