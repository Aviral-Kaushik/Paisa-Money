package com.aviral.eaa1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.aviral.eaa1.Adapter.OptionsRecyclerViewAdapter;
import com.aviral.eaa1.Adapter.ReferralEarningAdapter;
import com.aviral.eaa1.Models.ReferralEarning;
import com.aviral.eaa1.Utils.RecyclerViewMargin;
import com.aviral.eaa1.databinding.ActivityReferralEarningBinding;

import java.util.ArrayList;

public class ReferralEarningActivity extends AppCompatActivity {

    private ActivityReferralEarningBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferralEarningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpReferralEarningAdapter();
    }

    private void setUpReferralEarningAdapter() {
        ArrayList<ReferralEarning> referralEarnings = new ArrayList<>();

        referralEarnings.add(new ReferralEarning(
                "Ravi Anand",
                    "₹" + "0.025"
        ));

        referralEarnings.add(new ReferralEarning(
                "",
                ""
        ));

        referralEarnings.add(new ReferralEarning(
                "",
                ""
        ));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        binding.referralEarningRecyclerView.setLayoutManager(linearLayoutManager);

        ReferralEarningAdapter referralEarningAdapter =
                new ReferralEarningAdapter(referralEarnings);

        binding.referralEarningRecyclerView.setAdapter(referralEarningAdapter);

    }
}