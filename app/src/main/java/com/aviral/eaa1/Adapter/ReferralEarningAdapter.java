package com.aviral.eaa1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.eaa1.Models.Friend;
import com.aviral.eaa1.R;

import java.util.ArrayList;

public class ReferralEarningAdapter
        extends RecyclerView.Adapter<ReferralEarningAdapter.ViewHolder> {

    private final ArrayList<Friend> referralEarningArrayList;

    public ReferralEarningAdapter(ArrayList<Friend> referralEarningArrayList) {
        this.referralEarningArrayList = referralEarningArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.snippet_layout_referral_earning,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        setAnimation(holder.itemView.getContext(), holder.itemView);

        holder.friendName.setText(referralEarningArrayList.get(position).getName());
        holder.referralEarning.setText(referralEarningArrayList.get(position).getBalance());

    }

    private void setAnimation(Context context, View itemView) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);

        itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return referralEarningArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView friendName;
        private final TextView referralEarning;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            friendName = itemView.findViewById(R.id.friend_name);
            referralEarning = itemView.findViewById(R.id.referral_earning);
        }
    }
}
