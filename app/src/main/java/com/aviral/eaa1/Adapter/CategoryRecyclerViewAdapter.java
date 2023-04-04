package com.aviral.eaa1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.eaa1.Models.Category;
import com.aviral.eaa1.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter
        extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Category> categoryList;

    public CategoryRecyclerViewAdapter(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_layout_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.categoryName.setText(categoryList.get(position).getCategoryName());

        Glide.with(holder.itemView.getContext())
                .load(categoryList.get(position).getCategoryImage())
                .into(holder.categoryImage);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView categoryImage;
        private final TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}
