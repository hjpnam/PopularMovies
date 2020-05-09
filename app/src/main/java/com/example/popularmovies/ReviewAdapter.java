package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.shared.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private List<Review> mReviews;

    ReviewAdapter() {
        mReviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.review_list_item, parent, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.authorTv.setText(review.getAuthor() + " says");
        holder.contentTv.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.size();
    }

    void setReviews(List<Review> reviews) {
        mReviews = new ArrayList<>(reviews);
        notifyDataSetChanged();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView contentTv;
        TextView authorTv;

        public ReviewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTv = (TextView) itemView.findViewById(R.id.tv_review_content);
            authorTv = (TextView) itemView.findViewById(R.id.tv_review_author);
        }
    }
}
