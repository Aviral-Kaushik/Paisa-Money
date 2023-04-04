package com.aviral.eaa1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aviral.eaa1.Adapter.CategoryRecyclerViewAdapter;
import com.aviral.eaa1.Adapter.OptionsRecyclerViewAdapter;
import com.aviral.eaa1.Models.Category;
import com.aviral.eaa1.Models.Options;
import com.aviral.eaa1.Utils.RecyclerViewMargin;
import com.aviral.eaa1.databinding.ActivityMainBinding;
import com.aviral.eaa1.databinding.LayoutBottomNavigationBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpCategoryAdapter();
        setUpOptionAdapter();
        setUpBottomNavigation();

    }


    private void setUpOptionAdapter() {
        ArrayList<Options> optionsArrayList = new ArrayList<>();

        optionsArrayList.add(new Options(
                R.drawable.ic_rate,
                "Rate 5 Star & Earn Money",
                1,
                "₹10.00"
        ));

        optionsArrayList.add(new Options(
                R.drawable.ic_daily,
                "Daily Bonus",
                4,
                "₹0.25"
        ));

        optionsArrayList.add(new Options(
                R.drawable.ic_collect,
                "Collect Rewards",
                0,
                "₹0.20"
        ));

        optionsArrayList.add(new Options(
                R.drawable.ic_watch_videos,
                "Watch Videos",
                10,
                "₹0.50"
        ));

        optionsArrayList.add(new Options(
                R.drawable.ic_gold_points,
                "Gold Points",
                1,
                "₹0.50"
        ));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        binding.optionsRecyclerView.setLayoutManager(linearLayoutManager);

        OptionsRecyclerViewAdapter optionsRecyclerViewAdapter =
                new OptionsRecyclerViewAdapter(optionsArrayList);

        RecyclerViewMargin recyclerViewMargin = new RecyclerViewMargin(3);
        binding.optionsRecyclerView.addItemDecoration(recyclerViewMargin);

        binding.optionsRecyclerView.setAdapter(optionsRecyclerViewAdapter);

    }

    private void setUpCategoryAdapter() {
        ArrayList<Category> categoryList = new ArrayList<>();

        categoryList.add(new Category(
                R.drawable.ic_ludo,
                "Ludo"
        ));

        categoryList.add(new Category(
                R.drawable.ic_chess,
                "Chess"
        ));

        categoryList.add(new Category(
                R.drawable.ic_pubg,
                "Pubg"
        ));

        categoryList.add(new Category(
                R.drawable.ic_puzzle,
                "Puzzle"
        ));

        categoryList.add(new Category(
                R.drawable.ic_card,
                "Card"
        ));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);

        binding.categoriesRecyclerView.setLayoutManager(linearLayoutManager);

        CategoryRecyclerViewAdapter categoryRecyclerViewAdapter =
                new CategoryRecyclerViewAdapter(categoryList);

        RecyclerViewMargin recyclerViewMargin = new RecyclerViewMargin(5);
        binding.categoriesRecyclerView.addItemDecoration(recyclerViewMargin);

        binding.categoriesRecyclerView.setAdapter(categoryRecyclerViewAdapter);

    }

    public void setUpBottomNavigation() {
        LinearLayout btnScratchCard = findViewById(R.id.btnScratchCard);
        LinearLayout btnSpin = findViewById(R.id.btnSpin);
        LinearLayout btnOthers = findViewById(R.id.btnOthers);
        LinearLayout btnProfile = findViewById(R.id.btnProfile);

        btnSpin.setOnClickListener(view -> startActivity(
                new Intent(this, SpinActivity.class)));

        btnScratchCard.setOnClickListener(view -> startActivity(
                new Intent(this, ScratchCardActivity.class)));
    }
}