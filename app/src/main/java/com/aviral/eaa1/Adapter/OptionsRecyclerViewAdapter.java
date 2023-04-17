package com.aviral.eaa1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.eaa1.Models.Options;
import com.aviral.eaa1.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OptionsRecyclerViewAdapter
        extends RecyclerView.Adapter<OptionsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Options> optionList;

    public OptionsRecyclerViewAdapter(ArrayList<Options> optionList) {
        this.optionList = optionList;
    }

    @NonNull
    @Override
    public OptionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_layout_options,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsRecyclerViewAdapter.ViewHolder holder, int position) {

        setAnimation(holder.itemView, holder.itemView.getContext());

        if (optionList.get(position).getChances() <= 0) {
            holder.chances.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(R.color.chances_not_left));

            holder.optionButton.setBackground(AppCompatResources.getDrawable(
                    holder.itemView.getContext(),
                    R.drawable.option_price_button_backround_grey));

            holder.chances.setText(R.string.reward_completed);

        }  else {
            holder.chances.setText(optionList.get(position).getChances() + " Chance left");
        }

        holder.optionTitle.setText(optionList.get(position).getOptionDetails());
        holder.optionButton.setText(optionList.get(position).getOptionBalance());

        Glide.with(holder.itemView.getContext())
                .load(optionList.get(position).getOptionImage())
                .into(holder.optionImage);
    }

    private void setAnimation(View itemView, Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);

        itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView optionImage;
        private final TextView optionTitle;
        private final TextView chances;
        private final TextView optionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            optionImage = itemView.findViewById(R.id.option_image);
            optionTitle = itemView.findViewById(R.id.option_title);
            chances = itemView.findViewById(R.id.chances);
            optionButton = itemView.findViewById(R.id.price_button);

        }
    }
}
